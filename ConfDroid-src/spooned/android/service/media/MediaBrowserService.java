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
package android.service.media;


/**
 * Base class for media browse services.
 * <p>
 * Media browse services enable applications to browse media content provided by an application
 * and ask the application to start playing it. They may also be used to control content that
 * is already playing by way of a {@link MediaSession}.
 * </p>
 *
 * To extend this class, you must declare the service in your manifest file with
 * an intent filter with the {@link #SERVICE_INTERFACE} action.
 *
 * For example:
 * </p><pre>
 * &lt;service android:name=".MyMediaBrowserService"
 *          android:label="&#64;string/service_name" >
 *     &lt;intent-filter>
 *         &lt;action android:name="android.media.browse.MediaBrowserService" />
 *     &lt;/intent-filter>
 * &lt;/service>
 * </pre>
 */
public abstract class MediaBrowserService extends android.app.Service {
    private static final java.lang.String TAG = "MediaBrowserService";

    private static final boolean DBG = false;

    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";

    /**
     * A key for passing the MediaItem to the ResultReceiver in getItem.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_MEDIA_ITEM = "media_item";

    private static final int RESULT_FLAG_OPTION_NOT_HANDLED = 0x1;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.service.media.MediaBrowserService.RESULT_FLAG_OPTION_NOT_HANDLED })
    private @interface ResultFlags {}

    private final android.util.ArrayMap<android.os.IBinder, android.service.media.MediaBrowserService.ConnectionRecord> mConnections = new android.util.ArrayMap<>();

    private android.service.media.MediaBrowserService.ConnectionRecord mCurConnection;

    private final android.os.Handler mHandler = new android.os.Handler();

    private android.service.media.MediaBrowserService.ServiceBinder mBinder;

    android.media.session.MediaSession.Token mSession;

    /**
     * All the info about a connection.
     */
    private class ConnectionRecord {
        java.lang.String pkg;

        android.os.Bundle rootHints;

        android.service.media.IMediaBrowserServiceCallbacks callbacks;

        android.service.media.MediaBrowserService.BrowserRoot root;

        java.util.HashMap<java.lang.String, java.util.List<android.util.Pair<android.os.IBinder, android.os.Bundle>>> subscriptions = new java.util.HashMap<>();
    }

    /**
     * Completion handler for asynchronous callback methods in {@link MediaBrowserService}.
     * <p>
     * Each of the methods that takes one of these to send the result must call
     * {@link #sendResult} to respond to the caller with the given results. If those
     * functions return without calling {@link #sendResult}, they must instead call
     * {@link #detach} before returning, and then may call {@link #sendResult} when
     * they are done. If more than one of those methods is called, an exception will
     * be thrown.
     *
     * @see #onLoadChildren
     * @see #onLoadItem
     */
    public class Result<T> {
        private java.lang.Object mDebug;

        private boolean mDetachCalled;

        private boolean mSendResultCalled;

        private int mFlags;

        Result(java.lang.Object debug) {
            mDebug = debug;
        }

        /**
         * Send the result back to the caller.
         */
        public void sendResult(T result) {
            if (mSendResultCalled) {
                throw new java.lang.IllegalStateException("sendResult() called twice for: " + mDebug);
            }
            mSendResultCalled = true;
            onResultSent(result, mFlags);
        }

        /**
         * Detach this message from the current thread and allow the {@link #sendResult}
         * call to happen later.
         */
        public void detach() {
            if (mDetachCalled) {
                throw new java.lang.IllegalStateException(("detach() called when detach() had already" + " been called for: ") + mDebug);
            }
            if (mSendResultCalled) {
                throw new java.lang.IllegalStateException(("detach() called when sendResult() had already" + " been called for: ") + mDebug);
            }
            mDetachCalled = true;
        }

        boolean isDone() {
            return mDetachCalled || mSendResultCalled;
        }

        void setFlags(@android.service.media.MediaBrowserService.ResultFlags
        int flags) {
            mFlags = flags;
        }

        /**
         * Called when the result is sent, after assertions about not being called twice
         * have happened.
         */
        void onResultSent(T result, @android.service.media.MediaBrowserService.ResultFlags
        int flags) {
        }
    }

    private class ServiceBinder extends android.service.media.IMediaBrowserService.Stub {
        @java.lang.Override
        public void connect(final java.lang.String pkg, final android.os.Bundle rootHints, final android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            final int uid = android.os.Binder.getCallingUid();
            if (!isValidPackage(pkg, uid)) {
                throw new java.lang.IllegalArgumentException((("Package/uid mismatch: uid=" + uid) + " package=") + pkg);
            }
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Clear out the old subscriptions. We are getting new ones.
                    mConnections.remove(b);
                    final android.service.media.MediaBrowserService.ConnectionRecord connection = new android.service.media.MediaBrowserService.ConnectionRecord();
                    connection.pkg = pkg;
                    connection.rootHints = rootHints;
                    connection.callbacks = callbacks;
                    connection.root = android.service.media.MediaBrowserService.this.onGetRoot(pkg, uid, rootHints);
                    // If they didn't return something, don't allow this client.
                    if (connection.root == null) {
                        android.util.Log.i(android.service.media.MediaBrowserService.TAG, (("No root for client " + pkg) + " from service ") + getClass().getName());
                        try {
                            callbacks.onConnectFailed();
                        } catch (android.os.RemoteException ex) {
                            android.util.Log.w(android.service.media.MediaBrowserService.TAG, ("Calling onConnectFailed() failed. Ignoring. " + "pkg=") + pkg);
                        }
                    } else {
                        try {
                            mConnections.put(b, connection);
                            if (mSession != null) {
                                callbacks.onConnect(connection.root.getRootId(), mSession, connection.root.getExtras());
                            }
                        } catch (android.os.RemoteException ex) {
                            android.util.Log.w(android.service.media.MediaBrowserService.TAG, ("Calling onConnect() failed. Dropping client. " + "pkg=") + pkg);
                            mConnections.remove(b);
                        }
                    }
                }
            });
        }

        @java.lang.Override
        public void disconnect(final android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Clear out the old subscriptions. We are getting new ones.
                    final android.service.media.MediaBrowserService.ConnectionRecord old = mConnections.remove(b);
                    if (old != null) {
                        // TODO
                    }
                }
            });
        }

        @java.lang.Override
        public void addSubscriptionDeprecated(java.lang.String id, android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            // do-nothing
        }

        @java.lang.Override
        public void addSubscription(final java.lang.String id, final android.os.IBinder token, final android.os.Bundle options, final android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Get the record for the connection
                    final android.service.media.MediaBrowserService.ConnectionRecord connection = mConnections.get(b);
                    if (connection == null) {
                        android.util.Log.w(android.service.media.MediaBrowserService.TAG, "addSubscription for callback that isn't registered id=" + id);
                        return;
                    }
                    android.service.media.MediaBrowserService.this.addSubscription(id, connection, token, options);
                }
            });
        }

        @java.lang.Override
        public void removeSubscriptionDeprecated(java.lang.String id, android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            // do-nothing
        }

        @java.lang.Override
        public void removeSubscription(final java.lang.String id, final android.os.IBinder token, final android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    android.service.media.MediaBrowserService.ConnectionRecord connection = mConnections.get(b);
                    if (connection == null) {
                        android.util.Log.w(android.service.media.MediaBrowserService.TAG, "removeSubscription for callback that isn't registered id=" + id);
                        return;
                    }
                    if (!android.service.media.MediaBrowserService.this.removeSubscription(id, connection, token)) {
                        android.util.Log.w(android.service.media.MediaBrowserService.TAG, ("removeSubscription called for " + id) + " which is not subscribed");
                    }
                }
            });
        }

        @java.lang.Override
        public void getMediaItem(final java.lang.String mediaId, final android.os.ResultReceiver receiver, final android.service.media.IMediaBrowserServiceCallbacks callbacks) {
            if (android.text.TextUtils.isEmpty(mediaId) || (receiver == null)) {
                return;
            }
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    android.service.media.MediaBrowserService.ConnectionRecord connection = mConnections.get(b);
                    if (connection == null) {
                        android.util.Log.w(android.service.media.MediaBrowserService.TAG, "getMediaItem for callback that isn't registered id=" + mediaId);
                        return;
                    }
                    performLoadItem(mediaId, connection, receiver);
                }
            });
        }
    }

    @java.lang.Override
    public void onCreate() {
        super.onCreate();
        mBinder = new android.service.media.MediaBrowserService.ServiceBinder();
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (android.service.media.MediaBrowserService.SERVICE_INTERFACE.equals(intent.getAction())) {
            return mBinder;
        }
        return null;
    }

    @java.lang.Override
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
    }

    /**
     * Called to get the root information for browsing by a particular client.
     * <p>
     * The implementation should verify that the client package has permission
     * to access browse media information before returning the root id; it
     * should return null if the client is not allowed to access this
     * information.
     * </p>
     *
     * @param clientPackageName
     * 		The package name of the application which is
     * 		requesting access to browse media.
     * @param clientUid
     * 		The uid of the application which is requesting access to
     * 		browse media.
     * @param rootHints
     * 		An optional bundle of service-specific arguments to send
     * 		to the media browse service when connecting and retrieving the
     * 		root id for browsing, or null if none. The contents of this
     * 		bundle may affect the information returned when browsing.
     * @return The {@link BrowserRoot} for accessing this app's content or null.
     * @see BrowserRoot#EXTRA_RECENT
     * @see BrowserRoot#EXTRA_OFFLINE
     * @see BrowserRoot#EXTRA_SUGGESTED
     */
    @android.annotation.Nullable
    public abstract android.service.media.MediaBrowserService.BrowserRoot onGetRoot(@android.annotation.NonNull
    java.lang.String clientPackageName, int clientUid, @android.annotation.Nullable
    android.os.Bundle rootHints);

    /**
     * Called to get information about the children of a media item.
     * <p>
     * Implementations must call {@link Result#sendResult result.sendResult}
     * with the list of children. If loading the children will be an expensive
     * operation that should be performed on another thread,
     * {@link Result#detach result.detach} may be called before returning from
     * this function, and then {@link Result#sendResult result.sendResult}
     * called when the loading is complete.
     * </p><p>
     * In case the media item does not have any children, call {@link Result#sendResult}
     * with an empty list. When the given {@code parentId} is invalid, implementations must
     * call {@link Result#sendResult result.sendResult} with {@code null}, which will invoke
     * {@link MediaBrowser.SubscriptionCallback#onError}.
     * </p>
     *
     * @param parentId
     * 		The id of the parent media item whose children are to be
     * 		queried.
     * @param result
     * 		The Result to send the list of children to.
     */
    public abstract void onLoadChildren(@android.annotation.NonNull
    java.lang.String parentId, @android.annotation.NonNull
    android.service.media.MediaBrowserService.Result<java.util.List<android.media.browse.MediaBrowser.MediaItem>> result);

    /**
     * Called to get information about the children of a media item.
     * <p>
     * Implementations must call {@link Result#sendResult result.sendResult}
     * with the list of children. If loading the children will be an expensive
     * operation that should be performed on another thread,
     * {@link Result#detach result.detach} may be called before returning from
     * this function, and then {@link Result#sendResult result.sendResult}
     * called when the loading is complete.
     * </p><p>
     * In case the media item does not have any children, call {@link Result#sendResult}
     * with an empty list. When the given {@code parentId} is invalid, implementations must
     * call {@link Result#sendResult result.sendResult} with {@code null}, which will invoke
     * {@link MediaBrowser.SubscriptionCallback#onError}.
     * </p>
     *
     * @param parentId
     * 		The id of the parent media item whose children are to be
     * 		queried.
     * @param result
     * 		The Result to send the list of children to.
     * @param options
     * 		A bundle of service-specific arguments sent from the media
     * 		browse. The information returned through the result should be
     * 		affected by the contents of this bundle.
     */
    public void onLoadChildren(@android.annotation.NonNull
    java.lang.String parentId, @android.annotation.NonNull
    android.service.media.MediaBrowserService.Result<java.util.List<android.media.browse.MediaBrowser.MediaItem>> result, @android.annotation.NonNull
    android.os.Bundle options) {
        // To support backward compatibility, when the implementation of MediaBrowserService doesn't
        // override onLoadChildren() with options, onLoadChildren() without options will be used
        // instead, and the options will be applied in the implementation of result.onResultSent().
        result.setFlags(android.service.media.MediaBrowserService.RESULT_FLAG_OPTION_NOT_HANDLED);
        onLoadChildren(parentId, result);
    }

    /**
     * Called to get information about a specific media item.
     * <p>
     * Implementations must call {@link Result#sendResult result.sendResult}. If
     * loading the item will be an expensive operation {@link Result#detach
     * result.detach} may be called before returning from this function, and
     * then {@link Result#sendResult result.sendResult} called when the item has
     * been loaded.
     * </p><p>
     * When the given {@code itemId} is invalid, implementations must call
     * {@link Result#sendResult result.sendResult} with {@code null}, which will
     * invoke {@link MediaBrowser.ItemCallback#onError}.
     * </p><p>
     * The default implementation calls {@link Result#sendResult result.sendResult}
     * with {@code null}.
     * </p>
     *
     * @param itemId
     * 		The id for the specific
     * 		{@link android.media.browse.MediaBrowser.MediaItem}.
     * @param result
     * 		The Result to send the item to.
     */
    public void onLoadItem(java.lang.String itemId, android.service.media.MediaBrowserService.Result<android.media.browse.MediaBrowser.MediaItem> result) {
        result.sendResult(null);
    }

    /**
     * Call to set the media session.
     * <p>
     * This should be called as soon as possible during the service's startup.
     * It may only be called once.
     *
     * @param token
     * 		The token for the service's {@link MediaSession}.
     */
    public void setSessionToken(final android.media.session.MediaSession.Token token) {
        if (token == null) {
            throw new java.lang.IllegalArgumentException("Session token may not be null.");
        }
        if (mSession != null) {
            throw new java.lang.IllegalStateException("The session token has already been set.");
        }
        mSession = token;
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                for (android.os.IBinder key : mConnections.keySet()) {
                    android.service.media.MediaBrowserService.ConnectionRecord connection = mConnections.get(key);
                    try {
                        connection.callbacks.onConnect(connection.root.getRootId(), token, connection.root.getExtras());
                    } catch (android.os.RemoteException e) {
                        android.util.Log.w(android.service.media.MediaBrowserService.TAG, ("Connection for " + connection.pkg) + " is no longer valid.");
                        mConnections.remove(key);
                    }
                }
            }
        });
    }

    /**
     * Gets the session token, or null if it has not yet been created
     * or if it has been destroyed.
     */
    @android.annotation.Nullable
    public android.media.session.MediaSession.Token getSessionToken() {
        return mSession;
    }

    /**
     * Gets the root hints sent from the currently connected {@link MediaBrowser}.
     * The root hints are service-specific arguments included in an optional bundle sent to the
     * media browser service when connecting and retrieving the root id for browsing, or null if
     * none. The contents of this bundle may affect the information returned when browsing.
     *
     * @throws IllegalStateException
     * 		If this method is called outside of {@link #onLoadChildren}
     * 		or {@link #onLoadItem}
     * @see MediaBrowserService.BrowserRoot#EXTRA_RECENT
     * @see MediaBrowserService.BrowserRoot#EXTRA_OFFLINE
     * @see MediaBrowserService.BrowserRoot#EXTRA_SUGGESTED
     */
    public final android.os.Bundle getBrowserRootHints() {
        if (mCurConnection == null) {
            throw new java.lang.IllegalStateException("This should be called inside of onLoadChildren or" + " onLoadItem methods");
        }
        return mCurConnection.rootHints == null ? null : new android.os.Bundle(mCurConnection.rootHints);
    }

    /**
     * Notifies all connected media browsers that the children of
     * the specified parent id have changed in some way.
     * This will cause browsers to fetch subscribed content again.
     *
     * @param parentId
     * 		The id of the parent media item whose
     * 		children changed.
     */
    public void notifyChildrenChanged(@android.annotation.NonNull
    java.lang.String parentId) {
        notifyChildrenChangedInternal(parentId, null);
    }

    /**
     * Notifies all connected media browsers that the children of
     * the specified parent id have changed in some way.
     * This will cause browsers to fetch subscribed content again.
     *
     * @param parentId
     * 		The id of the parent media item whose
     * 		children changed.
     * @param options
     * 		A bundle of service-specific arguments to send
     * 		to the media browse. The contents of this bundle may
     * 		contain the information about the change.
     */
    public void notifyChildrenChanged(@android.annotation.NonNull
    java.lang.String parentId, @android.annotation.NonNull
    android.os.Bundle options) {
        if (options == null) {
            throw new java.lang.IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        }
        notifyChildrenChangedInternal(parentId, options);
    }

    private void notifyChildrenChangedInternal(final java.lang.String parentId, final android.os.Bundle options) {
        if (parentId == null) {
            throw new java.lang.IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                for (android.os.IBinder binder : mConnections.keySet()) {
                    android.service.media.MediaBrowserService.ConnectionRecord connection = mConnections.get(binder);
                    java.util.List<android.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(parentId);
                    if (callbackList != null) {
                        for (android.util.Pair<android.os.IBinder, android.os.Bundle> callback : callbackList) {
                            if (android.media.browse.MediaBrowserUtils.hasDuplicatedItems(options, callback.second)) {
                                performLoadChildren(parentId, connection, callback.second);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Return whether the given package is one of the ones that is owned by the uid.
     */
    private boolean isValidPackage(java.lang.String pkg, int uid) {
        if (pkg == null) {
            return false;
        }
        final android.content.pm.PackageManager pm = getPackageManager();
        final java.lang.String[] packages = pm.getPackagesForUid(uid);
        final int N = packages.length;
        for (int i = 0; i < N; i++) {
            if (packages[i].equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Save the subscription and if it is a new subscription send the results.
     */
    private void addSubscription(java.lang.String id, android.service.media.MediaBrowserService.ConnectionRecord connection, android.os.IBinder token, android.os.Bundle options) {
        // Save the subscription
        java.util.List<android.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(id);
        if (callbackList == null) {
            callbackList = new java.util.ArrayList<>();
        }
        for (android.util.Pair<android.os.IBinder, android.os.Bundle> callback : callbackList) {
            if ((token == callback.first) && android.media.browse.MediaBrowserUtils.areSameOptions(options, callback.second)) {
                return;
            }
        }
        callbackList.add(new android.util.Pair<>(token, options));
        connection.subscriptions.put(id, callbackList);
        // send the results
        performLoadChildren(id, connection, options);
    }

    /**
     * Remove the subscription.
     */
    private boolean removeSubscription(java.lang.String id, android.service.media.MediaBrowserService.ConnectionRecord connection, android.os.IBinder token) {
        if (token == null) {
            return connection.subscriptions.remove(id) != null;
        }
        boolean removed = false;
        java.util.List<android.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(id);
        if (callbackList != null) {
            for (android.util.Pair<android.os.IBinder, android.os.Bundle> callback : callbackList) {
                if (token == callback.first) {
                    removed = true;
                    callbackList.remove(callback);
                }
            }
            if (callbackList.size() == 0) {
                connection.subscriptions.remove(id);
            }
        }
        return removed;
    }

    /**
     * Call onLoadChildren and then send the results back to the connection.
     * <p>
     * Callers must make sure that this connection is still connected.
     */
    private void performLoadChildren(final java.lang.String parentId, final android.service.media.MediaBrowserService.ConnectionRecord connection, final android.os.Bundle options) {
        final android.service.media.MediaBrowserService.Result<java.util.List<android.media.browse.MediaBrowser.MediaItem>> result = new android.service.media.MediaBrowserService.Result<java.util.List<android.media.browse.MediaBrowser.MediaItem>>(parentId) {
            @java.lang.Override
            void onResultSent(java.util.List<android.media.browse.MediaBrowser.MediaItem> list, @android.service.media.MediaBrowserService.ResultFlags
            int flag) {
                if (mConnections.get(connection.callbacks.asBinder()) != connection) {
                    if (android.service.media.MediaBrowserService.DBG) {
                        android.util.Log.d(android.service.media.MediaBrowserService.TAG, ((("Not sending onLoadChildren result for connection that has" + " been disconnected. pkg=") + connection.pkg) + " id=") + parentId);
                    }
                    return;
                }
                java.util.List<android.media.browse.MediaBrowser.MediaItem> filteredList = ((flag & android.service.media.MediaBrowserService.RESULT_FLAG_OPTION_NOT_HANDLED) != 0) ? applyOptions(list, options) : list;
                final android.content.pm.ParceledListSlice<android.media.browse.MediaBrowser.MediaItem> pls = (filteredList == null) ? null : new android.content.pm.ParceledListSlice<>(filteredList);
                try {
                    connection.callbacks.onLoadChildrenWithOptions(parentId, pls, options);
                } catch (android.os.RemoteException ex) {
                    // The other side is in the process of crashing.
                    android.util.Log.w(android.service.media.MediaBrowserService.TAG, (("Calling onLoadChildren() failed for id=" + parentId) + " package=") + connection.pkg);
                }
            }
        };
        mCurConnection = connection;
        if (options == null) {
            onLoadChildren(parentId, result);
        } else {
            onLoadChildren(parentId, result, options);
        }
        mCurConnection = null;
        if (!result.isDone()) {
            throw new java.lang.IllegalStateException(((("onLoadChildren must call detach() or sendResult()" + " before returning for package=") + connection.pkg) + " id=") + parentId);
        }
    }

    private java.util.List<android.media.browse.MediaBrowser.MediaItem> applyOptions(java.util.List<android.media.browse.MediaBrowser.MediaItem> list, final android.os.Bundle options) {
        if (list == null) {
            return null;
        }
        int page = options.getInt(android.media.browse.MediaBrowser.EXTRA_PAGE, -1);
        int pageSize = options.getInt(android.media.browse.MediaBrowser.EXTRA_PAGE_SIZE, -1);
        if ((page == (-1)) && (pageSize == (-1))) {
            return list;
        }
        int fromIndex = pageSize * page;
        int toIndex = fromIndex + pageSize;
        if (((page < 0) || (pageSize < 1)) || (fromIndex >= list.size())) {
            return java.util.Collections.EMPTY_LIST;
        }
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        return list.subList(fromIndex, toIndex);
    }

    private void performLoadItem(java.lang.String itemId, final android.service.media.MediaBrowserService.ConnectionRecord connection, final android.os.ResultReceiver receiver) {
        final android.service.media.MediaBrowserService.Result<android.media.browse.MediaBrowser.MediaItem> result = new android.service.media.MediaBrowserService.Result<android.media.browse.MediaBrowser.MediaItem>(itemId) {
            @java.lang.Override
            void onResultSent(android.media.browse.MediaBrowser.MediaItem item, @android.service.media.MediaBrowserService.ResultFlags
            int flag) {
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putParcelable(android.service.media.MediaBrowserService.KEY_MEDIA_ITEM, item);
                receiver.send(0, bundle);
            }
        };
        mCurConnection = connection;
        onLoadItem(itemId, result);
        mCurConnection = null;
        if (!result.isDone()) {
            throw new java.lang.IllegalStateException(("onLoadItem must call detach() or sendResult()" + " before returning for id=") + itemId);
        }
    }

    /**
     * Contains information that the browser service needs to send to the client
     * when first connected.
     */
    public static final class BrowserRoot {
        /**
         * The lookup key for a boolean that indicates whether the browser service should return a
         * browser root for recently played media items.
         *
         * <p>When creating a media browser for a given media browser service, this key can be
         * supplied as a root hint for retrieving media items that are recently played.
         * If the media browser service can provide such media items, the implementation must return
         * the key in the root hint when {@link #onGetRoot(String, int, Bundle)} is called back.
         *
         * <p>The root hint may contain multiple keys.
         *
         * @see #EXTRA_OFFLINE
         * @see #EXTRA_SUGGESTED
         */
        public static final java.lang.String EXTRA_RECENT = "android.service.media.extra.RECENT";

        /**
         * The lookup key for a boolean that indicates whether the browser service should return a
         * browser root for offline media items.
         *
         * <p>When creating a media browser for a given media browser service, this key can be
         * supplied as a root hint for retrieving media items that are can be played without an
         * internet connection.
         * If the media browser service can provide such media items, the implementation must return
         * the key in the root hint when {@link #onGetRoot(String, int, Bundle)} is called back.
         *
         * <p>The root hint may contain multiple keys.
         *
         * @see #EXTRA_RECENT
         * @see #EXTRA_SUGGESTED
         */
        public static final java.lang.String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";

        /**
         * The lookup key for a boolean that indicates whether the browser service should return a
         * browser root for suggested media items.
         *
         * <p>When creating a media browser for a given media browser service, this key can be
         * supplied as a root hint for retrieving the media items suggested by the media browser
         * service. The list of media items passed in {@link android.media.browse.MediaBrowser.SubscriptionCallback#onChildrenLoaded(String, List)}
         * is considered ordered by relevance, first being the top suggestion.
         * If the media browser service can provide such media items, the implementation must return
         * the key in the root hint when {@link #onGetRoot(String, int, Bundle)} is called back.
         *
         * <p>The root hint may contain multiple keys.
         *
         * @see #EXTRA_RECENT
         * @see #EXTRA_OFFLINE
         */
        public static final java.lang.String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";

        private final java.lang.String mRootId;

        private final android.os.Bundle mExtras;

        /**
         * Constructs a browser root.
         *
         * @param rootId
         * 		The root id for browsing.
         * @param extras
         * 		Any extras about the browser service.
         */
        public BrowserRoot(@android.annotation.NonNull
        java.lang.String rootId, @android.annotation.Nullable
        android.os.Bundle extras) {
            if (rootId == null) {
                throw new java.lang.IllegalArgumentException("The root id in BrowserRoot cannot be null. " + "Use null for BrowserRoot instead.");
            }
            mRootId = rootId;
            mExtras = extras;
        }

        /**
         * Gets the root id for browsing.
         */
        public java.lang.String getRootId() {
            return mRootId;
        }

        /**
         * Gets any extras about the browser service.
         */
        public android.os.Bundle getExtras() {
            return mExtras;
        }
    }
}

