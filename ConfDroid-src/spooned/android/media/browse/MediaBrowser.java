/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.media.browse;


/**
 * Browses media content offered by a link MediaBrowserService.
 * <p>
 * This object is not thread-safe. All calls should happen on the thread on which the browser
 * was constructed.
 * </p>
 * <h3>Standard Extra Data</h3>
 *
 * <p>These are the current standard fields that can be used as extra data via
 * {@link #subscribe(String, Bundle, SubscriptionCallback)},
 * {@link #unsubscribe(String, SubscriptionCallback)}, and
 * {@link SubscriptionCallback#onChildrenLoaded(String, List, Bundle)}.
 *
 * <ul>
 *     <li> {@link #EXTRA_PAGE}
 *     <li> {@link #EXTRA_PAGE_SIZE}
 * </ul>
 */
public final class MediaBrowser {
    private static final java.lang.String TAG = "MediaBrowser";

    private static final boolean DBG = false;

    /**
     * Used as an int extra field to denote the page number to subscribe.
     * The value of {@code EXTRA_PAGE} should be greater than or equal to 0.
     *
     * @see #EXTRA_PAGE_SIZE
     */
    public static final java.lang.String EXTRA_PAGE = "android.media.browse.extra.PAGE";

    /**
     * Used as an int extra field to denote the number of media items in a page.
     * The value of {@code EXTRA_PAGE_SIZE} should be greater than or equal to 1.
     *
     * @see #EXTRA_PAGE
     */
    public static final java.lang.String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";

    private static final int CONNECT_STATE_DISCONNECTED = 0;

    private static final int CONNECT_STATE_CONNECTING = 1;

    private static final int CONNECT_STATE_CONNECTED = 2;

    private static final int CONNECT_STATE_SUSPENDED = 3;

    private final android.content.Context mContext;

    private final android.content.ComponentName mServiceComponent;

    private final android.media.browse.MediaBrowser.ConnectionCallback mCallback;

    private final android.os.Bundle mRootHints;

    private final android.os.Handler mHandler = new android.os.Handler();

    private final android.util.ArrayMap<java.lang.String, android.media.browse.MediaBrowser.Subscription> mSubscriptions = new android.util.ArrayMap<>();

    private int mState = android.media.browse.MediaBrowser.CONNECT_STATE_DISCONNECTED;

    private android.media.browse.MediaBrowser.MediaServiceConnection mServiceConnection;

    private android.service.media.IMediaBrowserService mServiceBinder;

    private android.service.media.IMediaBrowserServiceCallbacks mServiceCallbacks;

    private java.lang.String mRootId;

    private android.media.session.MediaSession.Token mMediaSessionToken;

    private android.os.Bundle mExtras;

    /**
     * Creates a media browser for the specified media browse service.
     *
     * @param context
     * 		The context.
     * @param serviceComponent
     * 		The component name of the media browse service.
     * @param callback
     * 		The connection callback.
     * @param rootHints
     * 		An optional bundle of service-specific arguments to send
     * 		to the media browse service when connecting and retrieving the root id
     * 		for browsing, or null if none. The contents of this bundle may affect
     * 		the information returned when browsing.
     * @see android.service.media.MediaBrowserService.BrowserRoot#EXTRA_RECENT
     * @see android.service.media.MediaBrowserService.BrowserRoot#EXTRA_OFFLINE
     * @see android.service.media.MediaBrowserService.BrowserRoot#EXTRA_SUGGESTED
     */
    public MediaBrowser(android.content.Context context, android.content.ComponentName serviceComponent, android.media.browse.MediaBrowser.ConnectionCallback callback, android.os.Bundle rootHints) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context must not be null");
        }
        if (serviceComponent == null) {
            throw new java.lang.IllegalArgumentException("service component must not be null");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("connection callback must not be null");
        }
        mContext = context;
        mServiceComponent = serviceComponent;
        mCallback = callback;
        mRootHints = (rootHints == null) ? null : new android.os.Bundle(rootHints);
    }

    /**
     * Connects to the media browse service.
     * <p>
     * The connection callback specified in the constructor will be invoked
     * when the connection completes or fails.
     * </p>
     */
    public void connect() {
        if (mState != android.media.browse.MediaBrowser.CONNECT_STATE_DISCONNECTED) {
            throw new java.lang.IllegalStateException(("connect() called while not disconnected (state=" + android.media.browse.MediaBrowser.getStateLabel(mState)) + ")");
        }
        // TODO: remove this extra check.
        if (android.media.browse.MediaBrowser.DBG) {
            if (mServiceConnection != null) {
                throw new java.lang.RuntimeException("mServiceConnection should be null. Instead it is " + mServiceConnection);
            }
        }
        if (mServiceBinder != null) {
            throw new java.lang.RuntimeException("mServiceBinder should be null. Instead it is " + mServiceBinder);
        }
        if (mServiceCallbacks != null) {
            throw new java.lang.RuntimeException("mServiceCallbacks should be null. Instead it is " + mServiceCallbacks);
        }
        mState = android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTING;
        final android.content.Intent intent = new android.content.Intent(android.service.media.MediaBrowserService.SERVICE_INTERFACE);
        intent.setComponent(mServiceComponent);
        final android.content.ServiceConnection thisConnection = mServiceConnection = new android.media.browse.MediaBrowser.MediaServiceConnection();
        boolean bound = false;
        try {
            bound = mContext.bindService(intent, mServiceConnection, android.content.Context.BIND_AUTO_CREATE);
        } catch (java.lang.Exception ex) {
            android.util.Log.e(android.media.browse.MediaBrowser.TAG, "Failed binding to service " + mServiceComponent);
        }
        if (!bound) {
            // Tell them that it didn't work. We are already on the main thread,
            // but we don't want to do callbacks inside of connect(). So post it,
            // and then check that we are on the same ServiceConnection. We know
            // we won't also get an onServiceConnected or onServiceDisconnected,
            // so we won't be doing double callbacks.
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    // Ensure that nobody else came in or tried to connect again.
                    if (thisConnection == mServiceConnection) {
                        forceCloseConnection();
                        mCallback.onConnectionFailed();
                    }
                }
            });
        }
        if (android.media.browse.MediaBrowser.DBG) {
            android.util.Log.d(android.media.browse.MediaBrowser.TAG, "connect...");
            dump();
        }
    }

    /**
     * Disconnects from the media browse service.
     * After this, no more callbacks will be received.
     */
    public void disconnect() {
        // It's ok to call this any state, because allowing this lets apps not have
        // to check isConnected() unnecessarily. They won't appreciate the extra
        // assertions for this. We do everything we can here to go back to a sane state.
        if (mServiceCallbacks != null) {
            try {
                mServiceBinder.disconnect(mServiceCallbacks);
            } catch (android.os.RemoteException ex) {
                // We are disconnecting anyway. Log, just for posterity but it's not
                // a big problem.
                android.util.Log.w(android.media.browse.MediaBrowser.TAG, "RemoteException during connect for " + mServiceComponent);
            }
        }
        forceCloseConnection();
        if (android.media.browse.MediaBrowser.DBG) {
            android.util.Log.d(android.media.browse.MediaBrowser.TAG, "disconnect...");
            dump();
        }
    }

    /**
     * Null out the variables and unbind from the service. This doesn't include
     * calling disconnect on the service, because we only try to do that in the
     * clean shutdown cases.
     * <p>
     * Everywhere that calls this EXCEPT for disconnect() should follow it with
     * a call to mCallback.onConnectionFailed(). Disconnect doesn't do that callback
     * for a clean shutdown, but everywhere else is a dirty shutdown and should
     * notify the app.
     */
    private void forceCloseConnection() {
        if (mServiceConnection != null) {
            mContext.unbindService(mServiceConnection);
        }
        mState = android.media.browse.MediaBrowser.CONNECT_STATE_DISCONNECTED;
        mServiceConnection = null;
        mServiceBinder = null;
        mServiceCallbacks = null;
        mRootId = null;
        mMediaSessionToken = null;
    }

    /**
     * Returns whether the browser is connected to the service.
     */
    public boolean isConnected() {
        return mState == android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED;
    }

    /**
     * Gets the service component that the media browser is connected to.
     */
    @android.annotation.NonNull
    public android.content.ComponentName getServiceComponent() {
        if (!isConnected()) {
            throw new java.lang.IllegalStateException((("getServiceComponent() called while not connected" + " (state=") + mState) + ")");
        }
        return mServiceComponent;
    }

    /**
     * Gets the root id.
     * <p>
     * Note that the root id may become invalid or change when the
     * browser is disconnected.
     * </p>
     *
     * @throws IllegalStateException
     * 		if not connected.
     */
    @android.annotation.NonNull
    public java.lang.String getRoot() {
        if (!isConnected()) {
            throw new java.lang.IllegalStateException(("getRoot() called while not connected (state=" + android.media.browse.MediaBrowser.getStateLabel(mState)) + ")");
        }
        return mRootId;
    }

    /**
     * Gets any extras for the media service.
     *
     * @throws IllegalStateException
     * 		if not connected.
     */
    @android.annotation.Nullable
    public android.os.Bundle getExtras() {
        if (!isConnected()) {
            throw new java.lang.IllegalStateException(("getExtras() called while not connected (state=" + android.media.browse.MediaBrowser.getStateLabel(mState)) + ")");
        }
        return mExtras;
    }

    /**
     * Gets the media session token associated with the media browser.
     * <p>
     * Note that the session token may become invalid or change when the
     * browser is disconnected.
     * </p>
     *
     * @return The session token for the browser, never null.
     * @throws IllegalStateException
     * 		if not connected.
     */
    @android.annotation.NonNull
    public android.media.session.MediaSession.Token getSessionToken() {
        if (!isConnected()) {
            throw new java.lang.IllegalStateException(("getSessionToken() called while not connected (state=" + mState) + ")");
        }
        return mMediaSessionToken;
    }

    /**
     * Queries for information about the media items that are contained within
     * the specified id and subscribes to receive updates when they change.
     * <p>
     * The list of subscriptions is maintained even when not connected and is
     * restored after the reconnection. It is ok to subscribe while not connected
     * but the results will not be returned until the connection completes.
     * </p>
     * <p>
     * If the id is already subscribed with a different callback then the new
     * callback will replace the previous one and the child data will be
     * reloaded.
     * </p>
     *
     * @param parentId
     * 		The id of the parent media item whose list of children
     * 		will be subscribed.
     * @param callback
     * 		The callback to receive the list of children.
     */
    public void subscribe(@android.annotation.NonNull
    java.lang.String parentId, @android.annotation.NonNull
    android.media.browse.MediaBrowser.SubscriptionCallback callback) {
        subscribeInternal(parentId, null, callback);
    }

    /**
     * Queries with service-specific arguments for information about the media items
     * that are contained within the specified id and subscribes to receive updates
     * when they change.
     * <p>
     * The list of subscriptions is maintained even when not connected and is
     * restored after the reconnection. It is ok to subscribe while not connected
     * but the results will not be returned until the connection completes.
     * </p>
     * <p>
     * If the id is already subscribed with a different callback then the new
     * callback will replace the previous one and the child data will be
     * reloaded.
     * </p>
     *
     * @param parentId
     * 		The id of the parent media item whose list of children
     * 		will be subscribed.
     * @param options
     * 		A bundle of service-specific arguments to send to the media
     * 		browse service. The contents of this bundle may affect the
     * 		information returned when browsing.
     * @param callback
     * 		The callback to receive the list of children.
     */
    public void subscribe(@android.annotation.NonNull
    java.lang.String parentId, @android.annotation.NonNull
    android.os.Bundle options, @android.annotation.NonNull
    android.media.browse.MediaBrowser.SubscriptionCallback callback) {
        if (options == null) {
            throw new java.lang.IllegalArgumentException("options are null");
        }
        subscribeInternal(parentId, new android.os.Bundle(options), callback);
    }

    /**
     * Unsubscribes for changes to the children of the specified media id.
     * <p>
     * The query callback will no longer be invoked for results associated with
     * this id once this method returns.
     * </p>
     *
     * @param parentId
     * 		The id of the parent media item whose list of children
     * 		will be unsubscribed.
     */
    public void unsubscribe(@android.annotation.NonNull
    java.lang.String parentId) {
        unsubscribeInternal(parentId, null);
    }

    /**
     * Unsubscribes for changes to the children of the specified media id through a callback.
     * <p>
     * The query callback will no longer be invoked for results associated with
     * this id once this method returns.
     * </p>
     *
     * @param parentId
     * 		The id of the parent media item whose list of children
     * 		will be unsubscribed.
     * @param callback
     * 		A callback sent to the media browse service to subscribe.
     */
    public void unsubscribe(@android.annotation.NonNull
    java.lang.String parentId, @android.annotation.NonNull
    android.media.browse.MediaBrowser.SubscriptionCallback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback is null");
        }
        unsubscribeInternal(parentId, callback);
    }

    /**
     * Retrieves a specific {@link MediaItem} from the connected service. Not
     * all services may support this, so falling back to subscribing to the
     * parent's id should be used when unavailable.
     *
     * @param mediaId
     * 		The id of the item to retrieve.
     * @param cb
     * 		The callback to receive the result on.
     */
    public void getItem(@android.annotation.NonNull
    final java.lang.String mediaId, @android.annotation.NonNull
    final android.media.browse.MediaBrowser.ItemCallback cb) {
        if (android.text.TextUtils.isEmpty(mediaId)) {
            throw new java.lang.IllegalArgumentException("mediaId is empty.");
        }
        if (cb == null) {
            throw new java.lang.IllegalArgumentException("cb is null.");
        }
        if (mState != android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED) {
            android.util.Log.i(android.media.browse.MediaBrowser.TAG, "Not connected, unable to retrieve the MediaItem.");
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    cb.onError(mediaId);
                }
            });
            return;
        }
        android.os.ResultReceiver receiver = new android.os.ResultReceiver(mHandler) {
            @java.lang.Override
            protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
                if (((resultCode != 0) || (resultData == null)) || (!resultData.containsKey(android.service.media.MediaBrowserService.KEY_MEDIA_ITEM))) {
                    cb.onError(mediaId);
                    return;
                }
                android.os.Parcelable item = resultData.getParcelable(android.service.media.MediaBrowserService.KEY_MEDIA_ITEM);
                if (!(item instanceof android.media.browse.MediaBrowser.MediaItem)) {
                    cb.onError(mediaId);
                    return;
                }
                cb.onItemLoaded(((android.media.browse.MediaBrowser.MediaItem) (item)));
            }
        };
        try {
            mServiceBinder.getMediaItem(mediaId, receiver, mServiceCallbacks);
        } catch (android.os.RemoteException e) {
            android.util.Log.i(android.media.browse.MediaBrowser.TAG, "Remote error getting media item.");
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    cb.onError(mediaId);
                }
            });
        }
    }

    private void subscribeInternal(java.lang.String parentId, android.os.Bundle options, android.media.browse.MediaBrowser.SubscriptionCallback callback) {
        // Check arguments.
        if (android.text.TextUtils.isEmpty(parentId)) {
            throw new java.lang.IllegalArgumentException("parentId is empty.");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback is null");
        }
        // Update or create the subscription.
        android.media.browse.MediaBrowser.Subscription sub = mSubscriptions.get(parentId);
        if (sub == null) {
            sub = new android.media.browse.MediaBrowser.Subscription();
            mSubscriptions.put(parentId, sub);
        }
        sub.putCallback(options, callback);
        // If we are connected, tell the service that we are watching. If we aren't connected,
        // the service will be told when we connect.
        if (mState == android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED) {
            try {
                if (options == null) {
                    mServiceBinder.addSubscriptionDeprecated(parentId, mServiceCallbacks);
                }
                mServiceBinder.addSubscription(parentId, callback.mToken, options, mServiceCallbacks);
            } catch (android.os.RemoteException ex) {
                // Process is crashing. We will disconnect, and upon reconnect we will
                // automatically reregister. So nothing to do here.
                android.util.Log.d(android.media.browse.MediaBrowser.TAG, "addSubscription failed with RemoteException parentId=" + parentId);
            }
        }
    }

    private void unsubscribeInternal(java.lang.String parentId, android.media.browse.MediaBrowser.SubscriptionCallback callback) {
        // Check arguments.
        if (android.text.TextUtils.isEmpty(parentId)) {
            throw new java.lang.IllegalArgumentException("parentId is empty.");
        }
        android.media.browse.MediaBrowser.Subscription sub = mSubscriptions.get(parentId);
        if (sub == null) {
            return;
        }
        // Tell the service if necessary.
        try {
            if (callback == null) {
                if (mState == android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED) {
                    mServiceBinder.removeSubscriptionDeprecated(parentId, mServiceCallbacks);
                    mServiceBinder.removeSubscription(parentId, null, mServiceCallbacks);
                }
            } else {
                final java.util.List<android.media.browse.MediaBrowser.SubscriptionCallback> callbacks = sub.getCallbacks();
                final java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                for (int i = callbacks.size() - 1; i >= 0; --i) {
                    if (callbacks.get(i) == callback) {
                        if (mState == android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED) {
                            mServiceBinder.removeSubscription(parentId, callback.mToken, mServiceCallbacks);
                        }
                        callbacks.remove(i);
                        optionsList.remove(i);
                    }
                }
            }
        } catch (android.os.RemoteException ex) {
            // Process is crashing. We will disconnect, and upon reconnect we will
            // automatically reregister. So nothing to do here.
            android.util.Log.d(android.media.browse.MediaBrowser.TAG, "removeSubscription failed with RemoteException parentId=" + parentId);
        }
        if (sub.isEmpty() || (callback == null)) {
            mSubscriptions.remove(parentId);
        }
    }

    /**
     * For debugging.
     */
    private static java.lang.String getStateLabel(int state) {
        switch (state) {
            case android.media.browse.MediaBrowser.CONNECT_STATE_DISCONNECTED :
                return "CONNECT_STATE_DISCONNECTED";
            case android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTING :
                return "CONNECT_STATE_CONNECTING";
            case android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED :
                return "CONNECT_STATE_CONNECTED";
            case android.media.browse.MediaBrowser.CONNECT_STATE_SUSPENDED :
                return "CONNECT_STATE_SUSPENDED";
            default :
                return "UNKNOWN/" + state;
        }
    }

    private final void onServiceConnected(final android.service.media.IMediaBrowserServiceCallbacks callback, final java.lang.String root, final android.media.session.MediaSession.Token session, final android.os.Bundle extra) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Check to make sure there hasn't been a disconnect or a different
                // ServiceConnection.
                if (!isCurrent(callback, "onConnect")) {
                    return;
                }
                // Don't allow them to call us twice.
                if (mState != android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTING) {
                    android.util.Log.w(android.media.browse.MediaBrowser.TAG, ("onConnect from service while mState=" + android.media.browse.MediaBrowser.getStateLabel(mState)) + "... ignoring");
                    return;
                }
                mRootId = root;
                mMediaSessionToken = session;
                mExtras = extra;
                mState = android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTED;
                if (android.media.browse.MediaBrowser.DBG) {
                    android.util.Log.d(android.media.browse.MediaBrowser.TAG, "ServiceCallbacks.onConnect...");
                    dump();
                }
                mCallback.onConnected();
                // we may receive some subscriptions before we are connected, so re-subscribe
                // everything now
                for (java.util.Map.Entry<java.lang.String, android.media.browse.MediaBrowser.Subscription> subscriptionEntry : mSubscriptions.entrySet()) {
                    java.lang.String id = subscriptionEntry.getKey();
                    android.media.browse.MediaBrowser.Subscription sub = subscriptionEntry.getValue();
                    java.util.List<android.media.browse.MediaBrowser.SubscriptionCallback> callbackList = sub.getCallbacks();
                    java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                    for (int i = 0; i < callbackList.size(); ++i) {
                        try {
                            mServiceBinder.addSubscription(id, callbackList.get(i).mToken, optionsList.get(i), mServiceCallbacks);
                        } catch (android.os.RemoteException ex) {
                            // Process is crashing. We will disconnect, and upon reconnect we will
                            // automatically reregister. So nothing to do here.
                            android.util.Log.d(android.media.browse.MediaBrowser.TAG, "addSubscription failed with RemoteException parentId=" + id);
                        }
                    }
                }
            }
        });
    }

    private final void onConnectionFailed(final android.service.media.IMediaBrowserServiceCallbacks callback) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.util.Log.e(android.media.browse.MediaBrowser.TAG, "onConnectFailed for " + mServiceComponent);
                // Check to make sure there hasn't been a disconnect or a different
                // ServiceConnection.
                if (!isCurrent(callback, "onConnectFailed")) {
                    return;
                }
                // Don't allow them to call us twice.
                if (mState != android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTING) {
                    android.util.Log.w(android.media.browse.MediaBrowser.TAG, ("onConnect from service while mState=" + android.media.browse.MediaBrowser.getStateLabel(mState)) + "... ignoring");
                    return;
                }
                // Clean up
                forceCloseConnection();
                // Tell the app.
                mCallback.onConnectionFailed();
            }
        });
    }

    private final void onLoadChildren(final android.service.media.IMediaBrowserServiceCallbacks callback, final java.lang.String parentId, final android.content.pm.ParceledListSlice list, final android.os.Bundle options) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                // Check that there hasn't been a disconnect or a different
                // ServiceConnection.
                if (!isCurrent(callback, "onLoadChildren")) {
                    return;
                }
                if (android.media.browse.MediaBrowser.DBG) {
                    android.util.Log.d(android.media.browse.MediaBrowser.TAG, (("onLoadChildren for " + mServiceComponent) + " id=") + parentId);
                }
                // Check that the subscription is still subscribed.
                final android.media.browse.MediaBrowser.Subscription subscription = mSubscriptions.get(parentId);
                if (subscription != null) {
                    // Tell the app.
                    android.media.browse.MediaBrowser.SubscriptionCallback subscriptionCallback = subscription.getCallback(options);
                    if (subscriptionCallback != null) {
                        java.util.List<android.media.browse.MediaBrowser.MediaItem> data = (list == null) ? null : list.getList();
                        if (options == null) {
                            if (data == null) {
                                subscriptionCallback.onError(parentId);
                            } else {
                                subscriptionCallback.onChildrenLoaded(parentId, data);
                            }
                        } else {
                            if (data == null) {
                                subscriptionCallback.onError(parentId, options);
                            } else {
                                subscriptionCallback.onChildrenLoaded(parentId, data, options);
                            }
                        }
                        return;
                    }
                }
                if (android.media.browse.MediaBrowser.DBG) {
                    android.util.Log.d(android.media.browse.MediaBrowser.TAG, "onLoadChildren for id that isn't subscribed id=" + parentId);
                }
            }
        });
    }

    /**
     * Return true if {@code callback} is the current ServiceCallbacks. Also logs if it's not.
     */
    private boolean isCurrent(android.service.media.IMediaBrowserServiceCallbacks callback, java.lang.String funcName) {
        if (mServiceCallbacks != callback) {
            if (mState != android.media.browse.MediaBrowser.CONNECT_STATE_DISCONNECTED) {
                android.util.Log.i(android.media.browse.MediaBrowser.TAG, (((((funcName + " for ") + mServiceComponent) + " with mServiceConnection=") + mServiceCallbacks) + " this=") + this);
            }
            return false;
        }
        return true;
    }

    private android.media.browse.MediaBrowser.ServiceCallbacks getNewServiceCallbacks() {
        return new android.media.browse.MediaBrowser.ServiceCallbacks(this);
    }

    /**
     * Log internal state.
     *
     * @unknown 
     */
    void dump() {
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "MediaBrowser...");
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mServiceComponent=" + mServiceComponent);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mCallback=" + mCallback);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mRootHints=" + mRootHints);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mState=" + android.media.browse.MediaBrowser.getStateLabel(mState));
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mServiceConnection=" + mServiceConnection);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mServiceBinder=" + mServiceBinder);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mServiceCallbacks=" + mServiceCallbacks);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mRootId=" + mRootId);
        android.util.Log.d(android.media.browse.MediaBrowser.TAG, "  mMediaSessionToken=" + mMediaSessionToken);
    }

    /**
     * A class with information on a single media item for use in browsing media.
     */
    public static class MediaItem implements android.os.Parcelable {
        private final int mFlags;

        private final android.media.MediaDescription mDescription;

        /**
         *
         *
         * @unknown 
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef(flag = true, value = { android.media.browse.MediaBrowser.MediaItem.FLAG_BROWSABLE, android.media.browse.MediaBrowser.MediaItem.FLAG_PLAYABLE })
        public @interface Flags {}

        /**
         * Flag: Indicates that the item has children of its own.
         */
        public static final int FLAG_BROWSABLE = 1 << 0;

        /**
         * Flag: Indicates that the item is playable.
         * <p>
         * The id of this item may be passed to
         * {@link MediaController.TransportControls#playFromMediaId(String, Bundle)}
         * to start playing it.
         * </p>
         */
        public static final int FLAG_PLAYABLE = 1 << 1;

        /**
         * Create a new MediaItem for use in browsing media.
         *
         * @param description
         * 		The description of the media, which must include a
         * 		media id.
         * @param flags
         * 		The flags for this item.
         */
        public MediaItem(@android.annotation.NonNull
        android.media.MediaDescription description, @android.media.browse.MediaBrowser.MediaItem.Flags
        int flags) {
            if (description == null) {
                throw new java.lang.IllegalArgumentException("description cannot be null");
            }
            if (android.text.TextUtils.isEmpty(description.getMediaId())) {
                throw new java.lang.IllegalArgumentException("description must have a non-empty media id");
            }
            mFlags = flags;
            mDescription = description;
        }

        /**
         * Private constructor.
         */
        private MediaItem(android.os.Parcel in) {
            mFlags = in.readInt();
            mDescription = android.media.MediaDescription.CREATOR.createFromParcel(in);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(mFlags);
            mDescription.writeToParcel(out, flags);
        }

        @java.lang.Override
        public java.lang.String toString() {
            final java.lang.StringBuilder sb = new java.lang.StringBuilder("MediaItem{");
            sb.append("mFlags=").append(mFlags);
            sb.append(", mDescription=").append(mDescription);
            sb.append('}');
            return sb.toString();
        }

        public static final android.os.Parcelable.Creator<android.media.browse.MediaBrowser.MediaItem> CREATOR = new android.os.Parcelable.Creator<android.media.browse.MediaBrowser.MediaItem>() {
            @java.lang.Override
            public android.media.browse.MediaBrowser.MediaItem createFromParcel(android.os.Parcel in) {
                return new android.media.browse.MediaBrowser.MediaItem(in);
            }

            @java.lang.Override
            public android.media.browse.MediaBrowser.MediaItem[] newArray(int size) {
                return new android.media.browse.MediaBrowser.MediaItem[size];
            }
        };

        /**
         * Gets the flags of the item.
         */
        @android.media.browse.MediaBrowser.MediaItem.Flags
        public int getFlags() {
            return mFlags;
        }

        /**
         * Returns whether this item is browsable.
         *
         * @see #FLAG_BROWSABLE
         */
        public boolean isBrowsable() {
            return (mFlags & android.media.browse.MediaBrowser.MediaItem.FLAG_BROWSABLE) != 0;
        }

        /**
         * Returns whether this item is playable.
         *
         * @see #FLAG_PLAYABLE
         */
        public boolean isPlayable() {
            return (mFlags & android.media.browse.MediaBrowser.MediaItem.FLAG_PLAYABLE) != 0;
        }

        /**
         * Returns the description of the media.
         */
        @android.annotation.NonNull
        public android.media.MediaDescription getDescription() {
            return mDescription;
        }

        /**
         * Returns the media id for this item.
         */
        @android.annotation.NonNull
        public java.lang.String getMediaId() {
            return mDescription.getMediaId();
        }
    }

    /**
     * Callbacks for connection related events.
     */
    public static class ConnectionCallback {
        /**
         * Invoked after {@link MediaBrowser#connect()} when the request has successfully completed.
         */
        public void onConnected() {
        }

        /**
         * Invoked when the client is disconnected from the media browser.
         */
        public void onConnectionSuspended() {
        }

        /**
         * Invoked when the connection to the media browser failed.
         */
        public void onConnectionFailed() {
        }
    }

    /**
     * Callbacks for subscription related events.
     */
    public static abstract class SubscriptionCallback {
        android.os.Binder mToken;

        public SubscriptionCallback() {
            mToken = new android.os.Binder();
        }

        /**
         * Called when the list of children is loaded or updated.
         *
         * @param parentId
         * 		The media id of the parent media item.
         * @param children
         * 		The children which were loaded.
         */
        public void onChildrenLoaded(@android.annotation.NonNull
        java.lang.String parentId, @android.annotation.NonNull
        java.util.List<android.media.browse.MediaBrowser.MediaItem> children) {
        }

        /**
         * Called when the list of children is loaded or updated.
         *
         * @param parentId
         * 		The media id of the parent media item.
         * @param children
         * 		The children which were loaded.
         * @param options
         * 		A bundle of service-specific arguments sent to the media
         * 		browse service. The contents of this bundle may affect the
         * 		information returned when browsing.
         */
        public void onChildrenLoaded(@android.annotation.NonNull
        java.lang.String parentId, @android.annotation.NonNull
        java.util.List<android.media.browse.MediaBrowser.MediaItem> children, @android.annotation.NonNull
        android.os.Bundle options) {
        }

        /**
         * Called when the id doesn't exist or other errors in subscribing.
         * <p>
         * If this is called, the subscription remains until {@link MediaBrowser#unsubscribe}
         * called, because some errors may heal themselves.
         * </p>
         *
         * @param parentId
         * 		The media id of the parent media item whose children could
         * 		not be loaded.
         */
        public void onError(@android.annotation.NonNull
        java.lang.String parentId) {
        }

        /**
         * Called when the id doesn't exist or other errors in subscribing.
         * <p>
         * If this is called, the subscription remains until {@link MediaBrowser#unsubscribe}
         * called, because some errors may heal themselves.
         * </p>
         *
         * @param parentId
         * 		The media id of the parent media item whose children could
         * 		not be loaded.
         * @param options
         * 		A bundle of service-specific arguments sent to the media
         * 		browse service.
         */
        public void onError(@android.annotation.NonNull
        java.lang.String parentId, @android.annotation.NonNull
        android.os.Bundle options) {
        }
    }

    /**
     * Callback for receiving the result of {@link #getItem}.
     */
    public static abstract class ItemCallback {
        /**
         * Called when the item has been returned by the browser service.
         *
         * @param item
         * 		The item that was returned or null if it doesn't exist.
         */
        public void onItemLoaded(android.media.browse.MediaBrowser.MediaItem item) {
        }

        /**
         * Called when the item doesn't exist or there was an error retrieving it.
         *
         * @param itemId
         * 		The media id of the media item which could not be loaded.
         */
        public void onError(@android.annotation.NonNull
        java.lang.String itemId) {
        }
    }

    /**
     * ServiceConnection to the other app.
     */
    private class MediaServiceConnection implements android.content.ServiceConnection {
        @java.lang.Override
        public void onServiceConnected(final android.content.ComponentName name, final android.os.IBinder binder) {
            postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.media.browse.MediaBrowser.DBG) {
                        android.util.Log.d(android.media.browse.MediaBrowser.TAG, (("MediaServiceConnection.onServiceConnected name=" + name) + " binder=") + binder);
                        dump();
                    }
                    // Make sure we are still the current connection, and that they haven't called
                    // disconnect().
                    if (!isCurrent("onServiceConnected")) {
                        return;
                    }
                    // Save their binder
                    mServiceBinder = IMediaBrowserService.Stub.asInterface(binder);
                    // We make a new mServiceCallbacks each time we connect so that we can drop
                    // responses from previous connections.
                    mServiceCallbacks = getNewServiceCallbacks();
                    mState = android.media.browse.MediaBrowser.CONNECT_STATE_CONNECTING;
                    // Call connect, which is async. When we get a response from that we will
                    // say that we're connected.
                    try {
                        if (android.media.browse.MediaBrowser.DBG) {
                            android.util.Log.d(android.media.browse.MediaBrowser.TAG, "ServiceCallbacks.onConnect...");
                            dump();
                        }
                        mServiceBinder.connect(mContext.getPackageName(), mRootHints, mServiceCallbacks);
                    } catch (android.os.RemoteException ex) {
                        // Connect failed, which isn't good. But the auto-reconnect on the service
                        // will take over and we will come back. We will also get the
                        // onServiceDisconnected, which has all the cleanup code. So let that do
                        // it.
                        android.util.Log.w(android.media.browse.MediaBrowser.TAG, "RemoteException during connect for " + mServiceComponent);
                        if (android.media.browse.MediaBrowser.DBG) {
                            android.util.Log.d(android.media.browse.MediaBrowser.TAG, "ServiceCallbacks.onConnect...");
                            dump();
                        }
                    }
                }
            });
        }

        @java.lang.Override
        public void onServiceDisconnected(final android.content.ComponentName name) {
            postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (android.media.browse.MediaBrowser.DBG) {
                        android.util.Log.d(android.media.browse.MediaBrowser.TAG, (((("MediaServiceConnection.onServiceDisconnected name=" + name) + " this=") + this) + " mServiceConnection=") + mServiceConnection);
                        dump();
                    }
                    // Make sure we are still the current connection, and that they haven't called
                    // disconnect().
                    if (!isCurrent("onServiceDisconnected")) {
                        return;
                    }
                    // Clear out what we set in onServiceConnected
                    mServiceBinder = null;
                    mServiceCallbacks = null;
                    // And tell the app that it's suspended.
                    mState = android.media.browse.MediaBrowser.CONNECT_STATE_SUSPENDED;
                    mCallback.onConnectionSuspended();
                }
            });
        }

        private void postOrRun(java.lang.Runnable r) {
            if (java.lang.Thread.currentThread() == mHandler.getLooper().getThread()) {
                r.run();
            } else {
                mHandler.post(r);
            }
        }

        /**
         * Return true if this is the current ServiceConnection. Also logs if it's not.
         */
        private boolean isCurrent(java.lang.String funcName) {
            if (mServiceConnection != this) {
                if (mState != android.media.browse.MediaBrowser.CONNECT_STATE_DISCONNECTED) {
                    // Check mState, because otherwise this log is noisy.
                    android.util.Log.i(android.media.browse.MediaBrowser.TAG, (((((funcName + " for ") + mServiceComponent) + " with mServiceConnection=") + mServiceConnection) + " this=") + this);
                }
                return false;
            }
            return true;
        }
    }

    /**
     * Callbacks from the service.
     */
    private static class ServiceCallbacks extends android.service.media.IMediaBrowserServiceCallbacks.Stub {
        private java.lang.ref.WeakReference<android.media.browse.MediaBrowser> mMediaBrowser;

        public ServiceCallbacks(android.media.browse.MediaBrowser mediaBrowser) {
            mMediaBrowser = new java.lang.ref.WeakReference<android.media.browse.MediaBrowser>(mediaBrowser);
        }

        /**
         * The other side has acknowledged our connection. The parameters to this function
         * are the initial data as requested.
         */
        @java.lang.Override
        public void onConnect(java.lang.String root, android.media.session.MediaSession.Token session, final android.os.Bundle extras) {
            android.media.browse.MediaBrowser mediaBrowser = mMediaBrowser.get();
            if (mediaBrowser != null) {
                mediaBrowser.onServiceConnected(this, root, session, extras);
            }
        }

        /**
         * The other side does not like us. Tell the app via onConnectionFailed.
         */
        @java.lang.Override
        public void onConnectFailed() {
            android.media.browse.MediaBrowser mediaBrowser = mMediaBrowser.get();
            if (mediaBrowser != null) {
                mediaBrowser.onConnectionFailed(this);
            }
        }

        @java.lang.Override
        public void onLoadChildren(java.lang.String parentId, android.content.pm.ParceledListSlice list) {
            onLoadChildrenWithOptions(parentId, list, null);
        }

        @java.lang.Override
        public void onLoadChildrenWithOptions(java.lang.String parentId, android.content.pm.ParceledListSlice list, final android.os.Bundle options) {
            android.media.browse.MediaBrowser mediaBrowser = mMediaBrowser.get();
            if (mediaBrowser != null) {
                mediaBrowser.onLoadChildren(this, parentId, list, options);
            }
        }
    }

    private static class Subscription {
        private final java.util.List<android.media.browse.MediaBrowser.SubscriptionCallback> mCallbacks;

        private final java.util.List<android.os.Bundle> mOptionsList;

        public Subscription() {
            mCallbacks = new java.util.ArrayList<>();
            mOptionsList = new java.util.ArrayList<>();
        }

        public boolean isEmpty() {
            return mCallbacks.isEmpty();
        }

        public java.util.List<android.os.Bundle> getOptionsList() {
            return mOptionsList;
        }

        public java.util.List<android.media.browse.MediaBrowser.SubscriptionCallback> getCallbacks() {
            return mCallbacks;
        }

        public android.media.browse.MediaBrowser.SubscriptionCallback getCallback(android.os.Bundle options) {
            for (int i = 0; i < mOptionsList.size(); ++i) {
                if (android.media.browse.MediaBrowserUtils.areSameOptions(mOptionsList.get(i), options)) {
                    return mCallbacks.get(i);
                }
            }
            return null;
        }

        public void putCallback(android.os.Bundle options, android.media.browse.MediaBrowser.SubscriptionCallback callback) {
            for (int i = 0; i < mOptionsList.size(); ++i) {
                if (android.media.browse.MediaBrowserUtils.areSameOptions(mOptionsList.get(i), options)) {
                    mCallbacks.set(i, callback);
                    return;
                }
            }
            mCallbacks.add(callback);
            mOptionsList.add(options);
        }
    }
}

