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
package android.support.v4.media;


/**
 * Base class for media browse services.
 * <p>
 * Media browse services enable applications to browse media content provided by an application
 * and ask the application to start playing it. They may also be used to control content that
 * is already playing by way of a {@link MediaSessionCompat}.
 * </p>
 *
 * To extend this class, you must declare the service in your manifest file with
 * an intent filter with the {@link #SERVICE_INTERFACE} action.
 *
 * For example:
 * </p><pre>
 * &lt;service android:name=".MyMediaBrowserServiceCompat"
 *          android:label="&#64;string/service_name" >
 *     &lt;intent-filter>
 *         &lt;action android:name="android.media.browse.MediaBrowserService" />
 *     &lt;/intent-filter>
 * &lt;/service>
 * </pre>
 */
public abstract class MediaBrowserServiceCompat extends android.app.Service {
    static final java.lang.String TAG = "MBServiceCompat";

    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v4.media.MediaBrowserServiceCompat.TAG, android.util.Log.DEBUG);

    private android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl mImpl;

    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    public static final java.lang.String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";

    /**
     * A key for passing the MediaItem to the ResultReceiver in getItem.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static final java.lang.String KEY_MEDIA_ITEM = "media_item";

    static final int RESULT_FLAG_OPTION_NOT_HANDLED = 0x1;

    static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 0x2;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.support.annotation.IntDef(flag = true, value = { android.support.v4.media.MediaBrowserServiceCompat.RESULT_FLAG_OPTION_NOT_HANDLED, android.support.v4.media.MediaBrowserServiceCompat.RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED })
    private @interface ResultFlags {}

    final android.support.v4.util.ArrayMap<android.os.IBinder, android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord> mConnections = new android.support.v4.util.ArrayMap<>();

    android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord mCurConnection;

    final android.support.v4.media.MediaBrowserServiceCompat.ServiceHandler mHandler = new android.support.v4.media.MediaBrowserServiceCompat.ServiceHandler();

    android.support.v4.media.session.MediaSessionCompat.Token mSession;

    interface MediaBrowserServiceImpl {
        void onCreate();

        android.os.IBinder onBind(android.content.Intent intent);

        void setSessionToken(android.support.v4.media.session.MediaSessionCompat.Token token);

        void notifyChildrenChanged(final java.lang.String parentId, final android.os.Bundle options);

        android.os.Bundle getBrowserRootHints();
    }

    class MediaBrowserServiceImplBase implements android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl {
        private android.os.Messenger mMessenger;

        @java.lang.Override
        public void onCreate() {
            mMessenger = new android.os.Messenger(mHandler);
        }

        @java.lang.Override
        public android.os.IBinder onBind(android.content.Intent intent) {
            if (android.support.v4.media.MediaBrowserServiceCompat.SERVICE_INTERFACE.equals(intent.getAction())) {
                return mMessenger.getBinder();
            }
            return null;
        }

        @java.lang.Override
        public void setSessionToken(final android.support.v4.media.session.MediaSessionCompat.Token token) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    java.util.Iterator<android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord> iter = mConnections.values().iterator();
                    while (iter.hasNext()) {
                        android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = iter.next();
                        try {
                            connection.callbacks.onConnect(connection.root.getRootId(), token, connection.root.getExtras());
                        } catch (android.os.RemoteException e) {
                            android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, ("Connection for " + connection.pkg) + " is no longer valid.");
                            iter.remove();
                        }
                    } 
                }
            });
        }

        @java.lang.Override
        public void notifyChildrenChanged(@android.support.annotation.NonNull
        final java.lang.String parentId, final android.os.Bundle options) {
            mHandler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    for (android.os.IBinder binder : mConnections.keySet()) {
                        android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = mConnections.get(binder);
                        java.util.List<android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(parentId);
                        if (callbackList != null) {
                            for (android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle> callback : callbackList) {
                                if (android.support.v4.media.MediaBrowserCompatUtils.hasDuplicatedItems(options, callback.second)) {
                                    performLoadChildren(parentId, connection, callback.second);
                                }
                            }
                        }
                    }
                }
            });
        }

        @java.lang.Override
        public android.os.Bundle getBrowserRootHints() {
            if (mCurConnection == null) {
                throw new java.lang.IllegalStateException("This should be called inside of onLoadChildren or" + " onLoadItem methods");
            }
            return mCurConnection.rootHints == null ? null : new android.os.Bundle(mCurConnection.rootHints);
        }
    }

    class MediaBrowserServiceImplApi21 implements android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImpl , android.support.v4.media.MediaBrowserServiceCompatApi21.ServiceCompatProxy {
        java.lang.Object mServiceObj;

        android.os.Messenger mMessenger;

        @java.lang.Override
        public void onCreate() {
            mServiceObj = android.support.v4.media.MediaBrowserServiceCompatApi21.createService(android.support.v4.media.MediaBrowserServiceCompat.this, this);
            android.support.v4.media.MediaBrowserServiceCompatApi21.onCreate(mServiceObj);
        }

        @java.lang.Override
        public android.os.IBinder onBind(android.content.Intent intent) {
            return android.support.v4.media.MediaBrowserServiceCompatApi21.onBind(mServiceObj, intent);
        }

        @java.lang.Override
        public void setSessionToken(android.support.v4.media.session.MediaSessionCompat.Token token) {
            android.support.v4.media.MediaBrowserServiceCompatApi21.setSessionToken(mServiceObj, token.getToken());
        }

        @java.lang.Override
        public void notifyChildrenChanged(final java.lang.String parentId, final android.os.Bundle options) {
            if (mMessenger == null) {
                android.support.v4.media.MediaBrowserServiceCompatApi21.notifyChildrenChanged(mServiceObj, parentId);
            } else {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        for (android.os.IBinder binder : mConnections.keySet()) {
                            android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = mConnections.get(binder);
                            java.util.List<android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(parentId);
                            if (callbackList != null) {
                                for (android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle> callback : callbackList) {
                                    if (android.support.v4.media.MediaBrowserCompatUtils.hasDuplicatedItems(options, callback.second)) {
                                        performLoadChildren(parentId, connection, callback.second);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        @java.lang.Override
        public android.os.Bundle getBrowserRootHints() {
            if (mMessenger == null) {
                // TODO: Handle getBrowserRootHints when connected with framework MediaBrowser.
                return null;
            }
            if (mCurConnection == null) {
                throw new java.lang.IllegalStateException("This should be called inside of onLoadChildren or" + " onLoadItem methods");
            }
            return mCurConnection.rootHints == null ? null : new android.os.Bundle(mCurConnection.rootHints);
        }

        @java.lang.Override
        public android.support.v4.media.MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(java.lang.String clientPackageName, int clientUid, android.os.Bundle rootHints) {
            android.os.Bundle rootExtras = null;
            if ((rootHints != null) && (rootHints.getInt(android.support.v4.media.MediaBrowserProtocol.EXTRA_CLIENT_VERSION, 0) != 0)) {
                rootHints.remove(android.support.v4.media.MediaBrowserProtocol.EXTRA_CLIENT_VERSION);
                mMessenger = new android.os.Messenger(mHandler);
                rootExtras = new android.os.Bundle();
                rootExtras.putInt(android.support.v4.media.MediaBrowserProtocol.EXTRA_SERVICE_VERSION, android.support.v4.media.MediaBrowserProtocol.SERVICE_VERSION_CURRENT);
                android.support.v4.app.BundleCompat.putBinder(rootExtras, android.support.v4.media.MediaBrowserProtocol.EXTRA_MESSENGER_BINDER, mMessenger.getBinder());
            }
            android.support.v4.media.MediaBrowserServiceCompat.BrowserRoot root = android.support.v4.media.MediaBrowserServiceCompat.this.onGetRoot(clientPackageName, clientUid, rootHints);
            if (root == null) {
                return null;
            }
            if (rootExtras == null) {
                rootExtras = root.getExtras();
            } else
                if (root.getExtras() != null) {
                    rootExtras.putAll(root.getExtras());
                }

            return new android.support.v4.media.MediaBrowserServiceCompatApi21.BrowserRoot(root.getRootId(), rootExtras);
        }

        @java.lang.Override
        public void onLoadChildren(java.lang.String parentId, final android.support.v4.media.MediaBrowserServiceCompatApi21.ResultWrapper<java.util.List<android.os.Parcel>> resultWrapper) {
            final android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result = new android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>>(parentId) {
                @java.lang.Override
                void onResultSent(java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> list, @android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
                int flags) {
                    java.util.List<android.os.Parcel> parcelList = null;
                    if (list != null) {
                        parcelList = new java.util.ArrayList<>();
                        for (android.support.v4.media.MediaBrowserCompat.MediaItem item : list) {
                            android.os.Parcel parcel = android.os.Parcel.obtain();
                            item.writeToParcel(parcel, 0);
                            parcelList.add(parcel);
                        }
                    }
                    resultWrapper.sendResult(parcelList);
                }

                @java.lang.Override
                public void detach() {
                    resultWrapper.detach();
                }
            };
            android.support.v4.media.MediaBrowserServiceCompat.this.onLoadChildren(parentId, result);
        }
    }

    class MediaBrowserServiceImplApi23 extends android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21 implements android.support.v4.media.MediaBrowserServiceCompatApi23.ServiceCompatProxy {
        @java.lang.Override
        public void onCreate() {
            mServiceObj = android.support.v4.media.MediaBrowserServiceCompatApi23.createService(android.support.v4.media.MediaBrowserServiceCompat.this, this);
            android.support.v4.media.MediaBrowserServiceCompatApi21.onCreate(mServiceObj);
        }

        @java.lang.Override
        public void onLoadItem(java.lang.String itemId, final android.support.v4.media.MediaBrowserServiceCompatApi21.ResultWrapper<android.os.Parcel> resultWrapper) {
            final android.support.v4.media.MediaBrowserServiceCompat.Result<android.support.v4.media.MediaBrowserCompat.MediaItem> result = new android.support.v4.media.MediaBrowserServiceCompat.Result<android.support.v4.media.MediaBrowserCompat.MediaItem>(itemId) {
                @java.lang.Override
                void onResultSent(android.support.v4.media.MediaBrowserCompat.MediaItem item, @android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
                int flags) {
                    if (item == null) {
                        resultWrapper.sendResult(null);
                    } else {
                        android.os.Parcel parcelItem = android.os.Parcel.obtain();
                        item.writeToParcel(parcelItem, 0);
                        resultWrapper.sendResult(parcelItem);
                    }
                }

                @java.lang.Override
                public void detach() {
                    resultWrapper.detach();
                }
            };
            android.support.v4.media.MediaBrowserServiceCompat.this.onLoadItem(itemId, result);
        }
    }

    class MediaBrowserServiceImplApi24 extends android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi23 implements android.support.v4.media.MediaBrowserServiceCompatApi24.ServiceCompatProxy {
        @java.lang.Override
        public void onCreate() {
            mServiceObj = android.support.v4.media.MediaBrowserServiceCompatApi24.createService(android.support.v4.media.MediaBrowserServiceCompat.this, this);
            android.support.v4.media.MediaBrowserServiceCompatApi21.onCreate(mServiceObj);
        }

        @java.lang.Override
        public void notifyChildrenChanged(final java.lang.String parentId, final android.os.Bundle options) {
            if (options == null) {
                android.support.v4.media.MediaBrowserServiceCompatApi21.notifyChildrenChanged(mServiceObj, parentId);
            } else {
                android.support.v4.media.MediaBrowserServiceCompatApi24.notifyChildrenChanged(mServiceObj, parentId, options);
            }
        }

        @java.lang.Override
        public void onLoadChildren(java.lang.String parentId, final android.support.v4.media.MediaBrowserServiceCompatApi24.ResultWrapper resultWrapper, android.os.Bundle options) {
            final android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result = new android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>>(parentId) {
                @java.lang.Override
                void onResultSent(java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> list, @android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
                int flags) {
                    java.util.List<android.os.Parcel> parcelList = null;
                    if (list != null) {
                        parcelList = new java.util.ArrayList<>();
                        for (android.support.v4.media.MediaBrowserCompat.MediaItem item : list) {
                            android.os.Parcel parcel = android.os.Parcel.obtain();
                            item.writeToParcel(parcel, 0);
                            parcelList.add(parcel);
                        }
                    }
                    resultWrapper.sendResult(parcelList, flags);
                }

                @java.lang.Override
                public void detach() {
                    resultWrapper.detach();
                }
            };
            android.support.v4.media.MediaBrowserServiceCompat.this.onLoadChildren(parentId, result, options);
        }

        @java.lang.Override
        public android.os.Bundle getBrowserRootHints() {
            return android.support.v4.media.MediaBrowserServiceCompatApi24.getBrowserRootHints(mServiceObj);
        }
    }

    private final class ServiceHandler extends android.os.Handler {
        private final android.support.v4.media.MediaBrowserServiceCompat.ServiceBinderImpl mServiceBinderImpl = new android.support.v4.media.MediaBrowserServiceCompat.ServiceBinderImpl();

        ServiceHandler() {
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.os.Bundle data = msg.getData();
            switch (msg.what) {
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_CONNECT :
                    mServiceBinderImpl.connect(data.getString(android.support.v4.media.MediaBrowserProtocol.DATA_PACKAGE_NAME), data.getInt(android.support.v4.media.MediaBrowserProtocol.DATA_CALLING_UID), data.getBundle(android.support.v4.media.MediaBrowserProtocol.DATA_ROOT_HINTS), new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_DISCONNECT :
                    mServiceBinderImpl.disconnect(new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_ADD_SUBSCRIPTION :
                    mServiceBinderImpl.addSubscription(data.getString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), android.support.v4.app.BundleCompat.getBinder(data, android.support.v4.media.MediaBrowserProtocol.DATA_CALLBACK_TOKEN), data.getBundle(android.support.v4.media.MediaBrowserProtocol.DATA_OPTIONS), new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_REMOVE_SUBSCRIPTION :
                    mServiceBinderImpl.removeSubscription(data.getString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), android.support.v4.app.BundleCompat.getBinder(data, android.support.v4.media.MediaBrowserProtocol.DATA_CALLBACK_TOKEN), new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_GET_MEDIA_ITEM :
                    mServiceBinderImpl.getMediaItem(data.getString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), ((android.support.v4.os.ResultReceiver) (data.getParcelable(android.support.v4.media.MediaBrowserProtocol.DATA_RESULT_RECEIVER))), new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_REGISTER_CALLBACK_MESSENGER :
                    mServiceBinderImpl.registerCallbacks(new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo), data.getBundle(android.support.v4.media.MediaBrowserProtocol.DATA_ROOT_HINTS));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_UNREGISTER_CALLBACK_MESSENGER :
                    mServiceBinderImpl.unregisterCallbacks(new android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacksCompat(msg.replyTo));
                    break;
                default :
                    android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, (((("Unhandled message: " + msg) + "\n  Service version: ") + android.support.v4.media.MediaBrowserProtocol.SERVICE_VERSION_CURRENT) + "\n  Client version: ") + msg.arg1);
            }
        }

        @java.lang.Override
        public boolean sendMessageAtTime(android.os.Message msg, long uptimeMillis) {
            // Binder.getCallingUid() in handleMessage will return the uid of this process.
            // In order to get the right calling uid, Binder.getCallingUid() should be called here.
            android.os.Bundle data = msg.getData();
            data.setClassLoader(android.support.v4.media.MediaBrowserCompat.class.getClassLoader());
            data.putInt(android.support.v4.media.MediaBrowserProtocol.DATA_CALLING_UID, android.os.Binder.getCallingUid());
            return super.sendMessageAtTime(msg, uptimeMillis);
        }

        public void postOrRun(java.lang.Runnable r) {
            if (java.lang.Thread.currentThread() == getLooper().getThread()) {
                r.run();
            } else {
                post(r);
            }
        }
    }

    /**
     * All the info about a connection.
     */
    private class ConnectionRecord {
        java.lang.String pkg;

        android.os.Bundle rootHints;

        android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks;

        android.support.v4.media.MediaBrowserServiceCompat.BrowserRoot root;

        java.util.HashMap<java.lang.String, java.util.List<android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle>>> subscriptions = new java.util.HashMap();

        ConnectionRecord() {
        }
    }

    /**
     * Completion handler for asynchronous callback methods in {@link MediaBrowserServiceCompat}.
     * <p>
     * Each of the methods that takes one of these to send the result must call
     * {@link #sendResult} to respond to the caller with the given results. If those
     * functions return without calling {@link #sendResult}, they must instead call
     * {@link #detach} before returning, and then may call {@link #sendResult} when
     * they are done. If more than one of those methods is called, an exception will
     * be thrown.
     *
     * @see MediaBrowserServiceCompat#onLoadChildren
     * @see MediaBrowserServiceCompat#onLoadItem
     */
    public static class Result<T> {
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

        void setFlags(@android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
        int flags) {
            mFlags = flags;
        }

        /**
         * Called when the result is sent, after assertions about not being called twice
         * have happened.
         */
        void onResultSent(T result, @android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
        int flags) {
        }
    }

    private class ServiceBinderImpl {
        ServiceBinderImpl() {
        }

        public void connect(final java.lang.String pkg, final int uid, final android.os.Bundle rootHints, final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
            if (!isValidPackage(pkg, uid)) {
                throw new java.lang.IllegalArgumentException((("Package/uid mismatch: uid=" + uid) + " package=") + pkg);
            }
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Clear out the old subscriptions. We are getting new ones.
                    mConnections.remove(b);
                    final android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = new android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord();
                    connection.pkg = pkg;
                    connection.rootHints = rootHints;
                    connection.callbacks = callbacks;
                    connection.root = android.support.v4.media.MediaBrowserServiceCompat.this.onGetRoot(pkg, uid, rootHints);
                    // If they didn't return something, don't allow this client.
                    if (connection.root == null) {
                        android.util.Log.i(android.support.v4.media.MediaBrowserServiceCompat.TAG, (("No root for client " + pkg) + " from service ") + getClass().getName());
                        try {
                            callbacks.onConnectFailed();
                        } catch (android.os.RemoteException ex) {
                            android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, ("Calling onConnectFailed() failed. Ignoring. " + "pkg=") + pkg);
                        }
                    } else {
                        try {
                            mConnections.put(b, connection);
                            if (mSession != null) {
                                callbacks.onConnect(connection.root.getRootId(), mSession, connection.root.getExtras());
                            }
                        } catch (android.os.RemoteException ex) {
                            android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, ("Calling onConnect() failed. Dropping client. " + "pkg=") + pkg);
                            mConnections.remove(b);
                        }
                    }
                }
            });
        }

        public void disconnect(final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Clear out the old subscriptions. We are getting new ones.
                    final android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord old = mConnections.remove(b);
                    if (old != null) {
                        // TODO
                    }
                }
            });
        }

        public void addSubscription(final java.lang.String id, final android.os.IBinder token, final android.os.Bundle options, final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Get the record for the connection
                    final android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = mConnections.get(b);
                    if (connection == null) {
                        android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, "addSubscription for callback that isn't registered id=" + id);
                        return;
                    }
                    android.support.v4.media.MediaBrowserServiceCompat.this.addSubscription(id, connection, token, options);
                }
            });
        }

        public void removeSubscription(final java.lang.String id, final android.os.IBinder token, final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = mConnections.get(b);
                    if (connection == null) {
                        android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, "removeSubscription for callback that isn't registered id=" + id);
                        return;
                    }
                    if (!android.support.v4.media.MediaBrowserServiceCompat.this.removeSubscription(id, connection, token)) {
                        android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, ("removeSubscription called for " + id) + " which is not subscribed");
                    }
                }
            });
        }

        public void getMediaItem(final java.lang.String mediaId, final android.support.v4.os.ResultReceiver receiver, final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
            if (android.text.TextUtils.isEmpty(mediaId) || (receiver == null)) {
                return;
            }
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = mConnections.get(b);
                    if (connection == null) {
                        android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, "getMediaItem for callback that isn't registered id=" + mediaId);
                        return;
                    }
                    performLoadItem(mediaId, connection, receiver);
                }
            });
        }

        // Used when {@link MediaBrowserProtocol#EXTRA_MESSENGER_BINDER} is used.
        public void registerCallbacks(final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks, final android.os.Bundle rootHints) {
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    // Clear out the old subscriptions. We are getting new ones.
                    mConnections.remove(b);
                    final android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection = new android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord();
                    connection.callbacks = callbacks;
                    connection.rootHints = rootHints;
                    mConnections.put(b, connection);
                }
            });
        }

        // Used when {@link MediaBrowserProtocol#EXTRA_MESSENGER_BINDER} is used.
        public void unregisterCallbacks(final android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks callbacks) {
            mHandler.postOrRun(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    final android.os.IBinder b = callbacks.asBinder();
                    mConnections.remove(b);
                }
            });
        }
    }

    private interface ServiceCallbacks {
        android.os.IBinder asBinder();

        void onConnect(java.lang.String root, android.support.v4.media.session.MediaSessionCompat.Token session, android.os.Bundle extras) throws android.os.RemoteException;

        void onConnectFailed() throws android.os.RemoteException;

        void onLoadChildren(java.lang.String mediaId, java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> list, android.os.Bundle options) throws android.os.RemoteException;
    }

    private class ServiceCallbacksCompat implements android.support.v4.media.MediaBrowserServiceCompat.ServiceCallbacks {
        final android.os.Messenger mCallbacks;

        ServiceCallbacksCompat(android.os.Messenger callbacks) {
            mCallbacks = callbacks;
        }

        @java.lang.Override
        public android.os.IBinder asBinder() {
            return mCallbacks.getBinder();
        }

        @java.lang.Override
        public void onConnect(java.lang.String root, android.support.v4.media.session.MediaSessionCompat.Token session, android.os.Bundle extras) throws android.os.RemoteException {
            if (extras == null) {
                extras = new android.os.Bundle();
            }
            extras.putInt(android.support.v4.media.MediaBrowserProtocol.EXTRA_SERVICE_VERSION, android.support.v4.media.MediaBrowserProtocol.SERVICE_VERSION_CURRENT);
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, root);
            data.putParcelable(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_SESSION_TOKEN, session);
            data.putBundle(android.support.v4.media.MediaBrowserProtocol.DATA_ROOT_HINTS, extras);
            sendRequest(android.support.v4.media.MediaBrowserProtocol.SERVICE_MSG_ON_CONNECT, data);
        }

        @java.lang.Override
        public void onConnectFailed() throws android.os.RemoteException {
            sendRequest(android.support.v4.media.MediaBrowserProtocol.SERVICE_MSG_ON_CONNECT_FAILED, null);
        }

        @java.lang.Override
        public void onLoadChildren(java.lang.String mediaId, java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> list, android.os.Bundle options) throws android.os.RemoteException {
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, mediaId);
            data.putBundle(android.support.v4.media.MediaBrowserProtocol.DATA_OPTIONS, options);
            if (list != null) {
                data.putParcelableArrayList(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_LIST, list instanceof java.util.ArrayList ? ((java.util.ArrayList) (list)) : new java.util.ArrayList<>(list));
            }
            sendRequest(android.support.v4.media.MediaBrowserProtocol.SERVICE_MSG_ON_LOAD_CHILDREN, data);
        }

        private void sendRequest(int what, android.os.Bundle data) throws android.os.RemoteException {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = what;
            msg.arg1 = android.support.v4.media.MediaBrowserProtocol.SERVICE_VERSION_CURRENT;
            msg.setData(data);
            mCallbacks.send(msg);
        }
    }

    @java.lang.Override
    public void onCreate() {
        super.onCreate();
        if ((android.os.Build.VERSION.SDK_INT >= 24) || android.support.v4.os.BuildCompat.isAtLeastN()) {
            mImpl = new android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi24();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                mImpl = new android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi23();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    mImpl = new android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImplApi21();
                } else {
                    mImpl = new android.support.v4.media.MediaBrowserServiceCompat.MediaBrowserServiceImplBase();
                }


        mImpl.onCreate();
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return mImpl.onBind(intent);
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
     * @see BrowserRoot#EXTRA_SUGGESTION_KEYWORDS
     */
    @android.support.annotation.Nullable
    public abstract android.support.v4.media.MediaBrowserServiceCompat.BrowserRoot onGetRoot(@android.support.annotation.NonNull
    java.lang.String clientPackageName, int clientUid, @android.support.annotation.Nullable
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
     *
     * @param parentId
     * 		The id of the parent media item whose children are to be
     * 		queried.
     * @param result
     * 		The Result to send the list of children to, or null if the
     * 		id is invalid.
     */
    public abstract void onLoadChildren(@android.support.annotation.NonNull
    java.lang.String parentId, @android.support.annotation.NonNull
    android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result);

    /**
     * Called to get information about the children of a media item.
     * <p>
     * Implementations must call {@link Result#sendResult result.sendResult}
     * with the list of children. If loading the children will be an expensive
     * operation that should be performed on another thread,
     * {@link Result#detach result.detach} may be called before returning from
     * this function, and then {@link Result#sendResult result.sendResult}
     * called when the loading is complete.
     *
     * @param parentId
     * 		The id of the parent media item whose children are to be
     * 		queried.
     * @param result
     * 		The Result to send the list of children to, or null if the
     * 		id is invalid.
     * @param options
     * 		A bundle of service-specific arguments sent from the media
     * 		browse. The information returned through the result should be
     * 		affected by the contents of this bundle.
     */
    public void onLoadChildren(@android.support.annotation.NonNull
    java.lang.String parentId, @android.support.annotation.NonNull
    android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result, @android.support.annotation.NonNull
    android.os.Bundle options) {
        // To support backward compatibility, when the implementation of MediaBrowserService doesn't
        // override onLoadChildren() with options, onLoadChildren() without options will be used
        // instead, and the options will be applied in the implementation of result.onResultSent().
        result.setFlags(android.support.v4.media.MediaBrowserServiceCompat.RESULT_FLAG_OPTION_NOT_HANDLED);
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
     * {@link Result#sendResult result.sendResult} with {@code null}.
     * </p><p>
     * The default implementation will invoke {@link MediaBrowserCompat.ItemCallback#onError}.
     *
     * @param itemId
     * 		The id for the specific {@link MediaBrowserCompat.MediaItem}.
     * @param result
     * 		The Result to send the item to, or null if the id is
     * 		invalid.
     */
    public void onLoadItem(java.lang.String itemId, android.support.v4.media.MediaBrowserServiceCompat.Result<android.support.v4.media.MediaBrowserCompat.MediaItem> result) {
        result.setFlags(android.support.v4.media.MediaBrowserServiceCompat.RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED);
        result.sendResult(null);
    }

    /**
     * Call to set the media session.
     * <p>
     * This should be called as soon as possible during the service's startup.
     * It may only be called once.
     *
     * @param token
     * 		The token for the service's {@link MediaSessionCompat}.
     */
    public void setSessionToken(android.support.v4.media.session.MediaSessionCompat.Token token) {
        if (token == null) {
            throw new java.lang.IllegalArgumentException("Session token may not be null.");
        }
        if (mSession != null) {
            throw new java.lang.IllegalStateException("The session token has already been set.");
        }
        mSession = token;
        mImpl.setSessionToken(token);
    }

    /**
     * Gets the session token, or null if it has not yet been created
     * or if it has been destroyed.
     */
    @android.support.annotation.Nullable
    public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
        return mSession;
    }

    /**
     * Gets the root hints sent from the currently connected {@link MediaBrowserCompat}.
     * The root hints are service-specific arguments included in an optional bundle sent to the
     * media browser service when connecting and retrieving the root id for browsing, or null if
     * none. The contents of this bundle may affect the information returned when browsing.
     * <p>
     * Note that this will return null when connected to {@link android.media.browse.MediaBrowser}
     * and running on API 23 or lower.
     *
     * @throws IllegalStateException
     * 		If this method is called outside of {@link #onLoadChildren}
     * 		or {@link #onLoadItem}
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_RECENT
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_OFFLINE
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_SUGGESTED
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_SUGGESTION_KEYWORDS
     */
    public final android.os.Bundle getBrowserRootHints() {
        return mImpl.getBrowserRootHints();
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
    public void notifyChildrenChanged(@android.support.annotation.NonNull
    java.lang.String parentId) {
        if (parentId == null) {
            throw new java.lang.IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        mImpl.notifyChildrenChanged(parentId, null);
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
    public void notifyChildrenChanged(@android.support.annotation.NonNull
    java.lang.String parentId, @android.support.annotation.NonNull
    android.os.Bundle options) {
        if (parentId == null) {
            throw new java.lang.IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        if (options == null) {
            throw new java.lang.IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        }
        mImpl.notifyChildrenChanged(parentId, options);
    }

    /**
     * Return whether the given package is one of the ones that is owned by the uid.
     */
    boolean isValidPackage(java.lang.String pkg, int uid) {
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
    void addSubscription(java.lang.String id, android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection, android.os.IBinder token, android.os.Bundle options) {
        // Save the subscription
        java.util.List<android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(id);
        if (callbackList == null) {
            callbackList = new java.util.ArrayList<>();
        }
        for (android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle> callback : callbackList) {
            if ((token == callback.first) && android.support.v4.media.MediaBrowserCompatUtils.areSameOptions(options, callback.second)) {
                return;
            }
        }
        callbackList.add(new android.support.v4.util.Pair<>(token, options));
        connection.subscriptions.put(id, callbackList);
        // send the results
        performLoadChildren(id, connection, options);
    }

    /**
     * Remove the subscription.
     */
    boolean removeSubscription(java.lang.String id, android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection, android.os.IBinder token) {
        if (token == null) {
            return connection.subscriptions.remove(id) != null;
        }
        boolean removed = false;
        java.util.List<android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle>> callbackList = connection.subscriptions.get(id);
        if (callbackList != null) {
            java.util.Iterator<android.support.v4.util.Pair<android.os.IBinder, android.os.Bundle>> iter = callbackList.iterator();
            while (iter.hasNext()) {
                if (token == iter.next().first) {
                    removed = true;
                    iter.remove();
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
    void performLoadChildren(final java.lang.String parentId, final android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection, final android.os.Bundle options) {
        final android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result = new android.support.v4.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>>(parentId) {
            @java.lang.Override
            void onResultSent(java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> list, @android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
            int flags) {
                if (mConnections.get(connection.callbacks.asBinder()) != connection) {
                    if (android.support.v4.media.MediaBrowserServiceCompat.DEBUG) {
                        android.util.Log.d(android.support.v4.media.MediaBrowserServiceCompat.TAG, ((("Not sending onLoadChildren result for connection that has" + " been disconnected. pkg=") + connection.pkg) + " id=") + parentId);
                    }
                    return;
                }
                java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> filteredList = ((flags & android.support.v4.media.MediaBrowserServiceCompat.RESULT_FLAG_OPTION_NOT_HANDLED) != 0) ? applyOptions(list, options) : list;
                try {
                    connection.callbacks.onLoadChildren(parentId, filteredList, options);
                } catch (android.os.RemoteException ex) {
                    // The other side is in the process of crashing.
                    android.util.Log.w(android.support.v4.media.MediaBrowserServiceCompat.TAG, (("Calling onLoadChildren() failed for id=" + parentId) + " package=") + connection.pkg);
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

    java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> applyOptions(java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> list, final android.os.Bundle options) {
        if (list == null) {
            return null;
        }
        int page = options.getInt(android.support.v4.media.MediaBrowserCompat.EXTRA_PAGE, -1);
        int pageSize = options.getInt(android.support.v4.media.MediaBrowserCompat.EXTRA_PAGE_SIZE, -1);
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

    void performLoadItem(java.lang.String itemId, android.support.v4.media.MediaBrowserServiceCompat.ConnectionRecord connection, final android.support.v4.os.ResultReceiver receiver) {
        final android.support.v4.media.MediaBrowserServiceCompat.Result<android.support.v4.media.MediaBrowserCompat.MediaItem> result = new android.support.v4.media.MediaBrowserServiceCompat.Result<android.support.v4.media.MediaBrowserCompat.MediaItem>(itemId) {
            @java.lang.Override
            void onResultSent(android.support.v4.media.MediaBrowserCompat.MediaItem item, @android.support.v4.media.MediaBrowserServiceCompat.ResultFlags
            int flags) {
                if ((flags & android.support.v4.media.MediaBrowserServiceCompat.RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED) != 0) {
                    receiver.send(-1, null);
                    return;
                }
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putParcelable(android.support.v4.media.MediaBrowserServiceCompat.KEY_MEDIA_ITEM, item);
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
         * @see #EXTRA_SUGGESTION_KEYWORDS
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
         * @see #EXTRA_SUGGESTION_KEYWORDS
         */
        public static final java.lang.String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";

        /**
         * The lookup key for a boolean that indicates whether the browser service should return a
         * browser root for suggested media items.
         *
         * <p>When creating a media browser for a given media browser service, this key can be
         * supplied as a root hint for retrieving the media items suggested by the media browser
         * service. The list of media items passed in {@link android.support.v4.media.MediaBrowserCompat.SubscriptionCallback#onChildrenLoaded(String, List)}
         * is considered ordered by relevance, first being the top suggestion.
         * If the media browser service can provide such media items, the implementation must return
         * the key in the root hint when {@link #onGetRoot(String, int, Bundle)} is called back.
         *
         * <p>The root hint may contain multiple keys.
         *
         * @see #EXTRA_RECENT
         * @see #EXTRA_OFFLINE
         * @see #EXTRA_SUGGESTION_KEYWORDS
         */
        public static final java.lang.String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";

        /**
         * The lookup key for a string that indicates specific keywords which will be considered
         * when the browser service suggests media items.
         *
         * <p>When creating a media browser for a given media browser service, this key can be
         * supplied as a root hint together with {@link #EXTRA_SUGGESTED} for retrieving suggested
         * media items related with the keywords. The list of media items passed in
         * {@link android.media.browse.MediaBrowser.SubscriptionCallback#onChildrenLoaded(String, List)}
         * is considered ordered by relevance, first being the top suggestion.
         * If the media browser service can provide such media items, the implementation must return
         * the key in the root hint when {@link #onGetRoot(String, int, Bundle)} is called back.
         *
         * <p>The root hint may contain multiple keys.
         *
         * @see #EXTRA_RECENT
         * @see #EXTRA_OFFLINE
         * @see #EXTRA_SUGGESTED
         */
        public static final java.lang.String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";

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
        public BrowserRoot(@android.support.annotation.NonNull
        java.lang.String rootId, @android.support.annotation.Nullable
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

