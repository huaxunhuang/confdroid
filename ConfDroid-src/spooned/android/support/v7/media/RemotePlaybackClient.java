/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.media;


/**
 * A helper class for playing media on remote routes using the remote playback protocol
 * defined by {@link MediaControlIntent}.
 * <p>
 * The client maintains session state and offers a simplified interface for issuing
 * remote playback media control intents to a single route.
 * </p>
 */
public class RemotePlaybackClient {
    static final java.lang.String TAG = "RemotePlaybackClient";

    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v7.media.RemotePlaybackClient.TAG, android.util.Log.DEBUG);

    private final android.content.Context mContext;

    private final android.support.v7.media.MediaRouter.RouteInfo mRoute;

    private final android.support.v7.media.RemotePlaybackClient.ActionReceiver mActionReceiver;

    private final android.app.PendingIntent mItemStatusPendingIntent;

    private final android.app.PendingIntent mSessionStatusPendingIntent;

    private final android.app.PendingIntent mMessagePendingIntent;

    private boolean mRouteSupportsRemotePlayback;

    private boolean mRouteSupportsQueuing;

    private boolean mRouteSupportsSessionManagement;

    private boolean mRouteSupportsMessaging;

    java.lang.String mSessionId;

    android.support.v7.media.RemotePlaybackClient.StatusCallback mStatusCallback;

    android.support.v7.media.RemotePlaybackClient.OnMessageReceivedListener mOnMessageReceivedListener;

    /**
     * Creates a remote playback client for a route.
     *
     * @param route
     * 		The media route.
     */
    public RemotePlaybackClient(android.content.Context context, android.support.v7.media.MediaRouter.RouteInfo route) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context must not be null");
        }
        if (route == null) {
            throw new java.lang.IllegalArgumentException("route must not be null");
        }
        mContext = context;
        mRoute = route;
        android.content.IntentFilter actionFilter = new android.content.IntentFilter();
        actionFilter.addAction(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_ITEM_STATUS_CHANGED);
        actionFilter.addAction(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_SESSION_STATUS_CHANGED);
        actionFilter.addAction(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_MESSAGE_RECEIVED);
        mActionReceiver = new android.support.v7.media.RemotePlaybackClient.ActionReceiver();
        context.registerReceiver(mActionReceiver, actionFilter);
        android.content.Intent itemStatusIntent = new android.content.Intent(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_ITEM_STATUS_CHANGED);
        itemStatusIntent.setPackage(context.getPackageName());
        mItemStatusPendingIntent = android.app.PendingIntent.getBroadcast(context, 0, itemStatusIntent, 0);
        android.content.Intent sessionStatusIntent = new android.content.Intent(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_SESSION_STATUS_CHANGED);
        sessionStatusIntent.setPackage(context.getPackageName());
        mSessionStatusPendingIntent = android.app.PendingIntent.getBroadcast(context, 0, sessionStatusIntent, 0);
        android.content.Intent messageIntent = new android.content.Intent(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_MESSAGE_RECEIVED);
        messageIntent.setPackage(context.getPackageName());
        mMessagePendingIntent = android.app.PendingIntent.getBroadcast(context, 0, messageIntent, 0);
        detectFeatures();
    }

    /**
     * Releases resources owned by the client.
     */
    public void release() {
        mContext.unregisterReceiver(mActionReceiver);
    }

    /**
     * Returns true if the route supports remote playback.
     * <p>
     * If the route does not support remote playback, then none of the functionality
     * offered by the client will be available.
     * </p><p>
     * This method returns true if the route supports all of the following
     * actions: {@link MediaControlIntent#ACTION_PLAY play},
     * {@link MediaControlIntent#ACTION_SEEK seek},
     * {@link MediaControlIntent#ACTION_GET_STATUS get status},
     * {@link MediaControlIntent#ACTION_PAUSE pause},
     * {@link MediaControlIntent#ACTION_RESUME resume},
     * {@link MediaControlIntent#ACTION_STOP stop}.
     * </p>
     *
     * @return True if remote playback is supported.
     */
    public boolean isRemotePlaybackSupported() {
        return mRouteSupportsRemotePlayback;
    }

    /**
     * Returns true if the route supports queuing features.
     * <p>
     * If the route does not support queuing, then at most one media item can be played
     * at a time and the {@link #enqueue} method will not be available.
     * </p><p>
     * This method returns true if the route supports all of the basic remote playback
     * actions and all of the following actions:
     * {@link MediaControlIntent#ACTION_ENQUEUE enqueue},
     * {@link MediaControlIntent#ACTION_REMOVE remove}.
     * </p>
     *
     * @return True if queuing is supported.  Implies {@link #isRemotePlaybackSupported}
    is also true.
     * @see #isRemotePlaybackSupported
     */
    public boolean isQueuingSupported() {
        return mRouteSupportsQueuing;
    }

    /**
     * Returns true if the route supports session management features.
     * <p>
     * If the route does not support session management, then the session will
     * not be created until the first media item is played.
     * </p><p>
     * This method returns true if the route supports all of the basic remote playback
     * actions and all of the following actions:
     * {@link MediaControlIntent#ACTION_START_SESSION start session},
     * {@link MediaControlIntent#ACTION_GET_SESSION_STATUS get session status},
     * {@link MediaControlIntent#ACTION_END_SESSION end session}.
     * </p>
     *
     * @return True if session management is supported.
    Implies {@link #isRemotePlaybackSupported} is also true.
     * @see #isRemotePlaybackSupported
     */
    public boolean isSessionManagementSupported() {
        return mRouteSupportsSessionManagement;
    }

    /**
     * Returns true if the route supports messages.
     * <p>
     * This method returns true if the route supports all of the basic remote playback
     * actions and all of the following actions:
     * {@link MediaControlIntent#ACTION_START_SESSION start session},
     * {@link MediaControlIntent#ACTION_SEND_MESSAGE send message},
     * {@link MediaControlIntent#ACTION_END_SESSION end session}.
     * </p>
     *
     * @return True if session management is supported.
    Implies {@link #isRemotePlaybackSupported} is also true.
     * @see #isRemotePlaybackSupported
     */
    public boolean isMessagingSupported() {
        return mRouteSupportsMessaging;
    }

    /**
     * Gets the current session id if there is one.
     *
     * @return The current session id, or null if none.
     */
    public java.lang.String getSessionId() {
        return mSessionId;
    }

    /**
     * Sets the current session id.
     * <p>
     * It is usually not necessary to set the session id explicitly since
     * it is created as a side-effect of other requests such as
     * {@link #play}, {@link #enqueue}, and {@link #startSession}.
     * </p>
     *
     * @param sessionId
     * 		The new session id, or null if none.
     */
    public void setSessionId(java.lang.String sessionId) {
        if ((mSessionId != sessionId) && ((mSessionId == null) || (!mSessionId.equals(sessionId)))) {
            if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
                android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, "Session id is now: " + sessionId);
            }
            mSessionId = sessionId;
            if (mStatusCallback != null) {
                mStatusCallback.onSessionChanged(sessionId);
            }
        }
    }

    /**
     * Returns true if the client currently has a session.
     * <p>
     * Equivalent to checking whether {@link #getSessionId} returns a non-null result.
     * </p>
     *
     * @return True if there is a current session.
     */
    public boolean hasSession() {
        return mSessionId != null;
    }

    /**
     * Sets a callback that should receive status updates when the state of
     * media sessions or media items created by this instance of the remote
     * playback client changes.
     * <p>
     * The callback should be set before the session is created or any play
     * commands are issued.
     * </p>
     *
     * @param callback
     * 		The callback to set.  May be null to remove the previous callback.
     */
    public void setStatusCallback(android.support.v7.media.RemotePlaybackClient.StatusCallback callback) {
        mStatusCallback = callback;
    }

    /**
     * Sets a callback that should receive messages when a message is sent from
     * media sessions created by this instance of the remote playback client changes.
     * <p>
     * The callback should be set before the session is created.
     * </p>
     *
     * @param listener
     * 		The callback to set.  May be null to remove the previous callback.
     */
    public void setOnMessageReceivedListener(android.support.v7.media.RemotePlaybackClient.OnMessageReceivedListener listener) {
        mOnMessageReceivedListener = listener;
    }

    /**
     * Sends a request to play a media item.
     * <p>
     * Clears the queue and starts playing the new item immediately.  If the queue
     * was previously paused, then it is resumed as a side-effect of this request.
     * </p><p>
     * The request is issued in the current session.  If no session is available, then
     * one is created implicitly.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_PLAY ACTION_PLAY} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param contentUri
     * 		The content Uri to play.
     * @param mimeType
     * 		The mime type of the content, or null if unknown.
     * @param positionMillis
     * 		The initial content position for the item in milliseconds,
     * 		or <code>0</code> to start at the beginning.
     * @param metadata
     * 		The media item metadata bundle, or null if none.
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_PLAY} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws UnsupportedOperationException
     * 		if the route does not support remote playback.
     * @see MediaControlIntent#ACTION_PLAY
     * @see #isRemotePlaybackSupported
     */
    public void play(android.net.Uri contentUri, java.lang.String mimeType, android.os.Bundle metadata, long positionMillis, android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback) {
        playOrEnqueue(contentUri, mimeType, metadata, positionMillis, extras, callback, android.support.v7.media.MediaControlIntent.ACTION_PLAY);
    }

    /**
     * Sends a request to enqueue a media item.
     * <p>
     * Enqueues a new item to play.  If the queue was previously paused, then will
     * remain paused.
     * </p><p>
     * The request is issued in the current session.  If no session is available, then
     * one is created implicitly.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_ENQUEUE ACTION_ENQUEUE} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param contentUri
     * 		The content Uri to enqueue.
     * @param mimeType
     * 		The mime type of the content, or null if unknown.
     * @param positionMillis
     * 		The initial content position for the item in milliseconds,
     * 		or <code>0</code> to start at the beginning.
     * @param metadata
     * 		The media item metadata bundle, or null if none.
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_ENQUEUE} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws UnsupportedOperationException
     * 		if the route does not support queuing.
     * @see MediaControlIntent#ACTION_ENQUEUE
     * @see #isRemotePlaybackSupported
     * @see #isQueuingSupported
     */
    public void enqueue(android.net.Uri contentUri, java.lang.String mimeType, android.os.Bundle metadata, long positionMillis, android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback) {
        playOrEnqueue(contentUri, mimeType, metadata, positionMillis, extras, callback, android.support.v7.media.MediaControlIntent.ACTION_ENQUEUE);
    }

    private void playOrEnqueue(android.net.Uri contentUri, java.lang.String mimeType, android.os.Bundle metadata, long positionMillis, android.os.Bundle extras, final android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback, java.lang.String action) {
        if (contentUri == null) {
            throw new java.lang.IllegalArgumentException("contentUri must not be null");
        }
        throwIfRemotePlaybackNotSupported();
        if (action.equals(android.support.v7.media.MediaControlIntent.ACTION_ENQUEUE)) {
            throwIfQueuingNotSupported();
        }
        android.content.Intent intent = new android.content.Intent(action);
        intent.setDataAndType(contentUri, mimeType);
        intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_STATUS_UPDATE_RECEIVER, mItemStatusPendingIntent);
        if (metadata != null) {
            intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_METADATA, metadata);
        }
        if (positionMillis != 0) {
            intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_CONTENT_POSITION, positionMillis);
        }
        performItemAction(intent, mSessionId, null, extras, callback);
    }

    /**
     * Sends a request to seek to a new position in a media item.
     * <p>
     * Seeks to a new position.  If the queue was previously paused then it
     * remains paused but the item's new position is still remembered.
     * </p><p>
     * The request is issued in the current session.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_SEEK ACTION_SEEK} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param itemId
     * 		The item id.
     * @param positionMillis
     * 		The new content position for the item in milliseconds,
     * 		or <code>0</code> to start at the beginning.
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_SEEK} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @see MediaControlIntent#ACTION_SEEK
     * @see #isRemotePlaybackSupported
     */
    public void seek(java.lang.String itemId, long positionMillis, android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback) {
        if (itemId == null) {
            throw new java.lang.IllegalArgumentException("itemId must not be null");
        }
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_SEEK);
        intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_CONTENT_POSITION, positionMillis);
        performItemAction(intent, mSessionId, itemId, extras, callback);
    }

    /**
     * Sends a request to get the status of a media item.
     * <p>
     * The request is issued in the current session.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_GET_STATUS ACTION_GET_STATUS} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param itemId
     * 		The item id.
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_GET_STATUS} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @see MediaControlIntent#ACTION_GET_STATUS
     * @see #isRemotePlaybackSupported
     */
    public void getStatus(java.lang.String itemId, android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback) {
        if (itemId == null) {
            throw new java.lang.IllegalArgumentException("itemId must not be null");
        }
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_GET_STATUS);
        performItemAction(intent, mSessionId, itemId, extras, callback);
    }

    /**
     * Sends a request to remove a media item from the queue.
     * <p>
     * The request is issued in the current session.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_REMOVE ACTION_REMOVE} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param itemId
     * 		The item id.
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_REMOVE} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @throws UnsupportedOperationException
     * 		if the route does not support queuing.
     * @see MediaControlIntent#ACTION_REMOVE
     * @see #isRemotePlaybackSupported
     * @see #isQueuingSupported
     */
    public void remove(java.lang.String itemId, android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback) {
        if (itemId == null) {
            throw new java.lang.IllegalArgumentException("itemId must not be null");
        }
        throwIfQueuingNotSupported();
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_REMOVE);
        performItemAction(intent, mSessionId, itemId, extras, callback);
    }

    /**
     * Sends a request to pause media playback.
     * <p>
     * The request is issued in the current session.  If playback is already paused
     * then the request has no effect.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_PAUSE ACTION_PAUSE} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_PAUSE} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @see MediaControlIntent#ACTION_PAUSE
     * @see #isRemotePlaybackSupported
     */
    public void pause(android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_PAUSE);
        performSessionAction(intent, mSessionId, extras, callback);
    }

    /**
     * Sends a request to resume (unpause) media playback.
     * <p>
     * The request is issued in the current session.  If playback is not paused
     * then the request has no effect.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_RESUME ACTION_RESUME} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_RESUME} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @see MediaControlIntent#ACTION_RESUME
     * @see #isRemotePlaybackSupported
     */
    public void resume(android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_RESUME);
        performSessionAction(intent, mSessionId, extras, callback);
    }

    /**
     * Sends a request to stop media playback and clear the media playback queue.
     * <p>
     * The request is issued in the current session.  If the queue is already
     * empty then the request has no effect.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_STOP ACTION_STOP} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_STOP} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @see MediaControlIntent#ACTION_STOP
     * @see #isRemotePlaybackSupported
     */
    public void stop(android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_STOP);
        performSessionAction(intent, mSessionId, extras, callback);
    }

    /**
     * Sends a request to start a new media playback session.
     * <p>
     * The application must wait for the callback to indicate that this request
     * is complete before issuing other requests that affect the session.  If this
     * request is successful then the previous session will be invalidated.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_START_SESSION ACTION_START_SESSION}
     * for more information about the semantics of this request.
     * </p>
     *
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_START_SESSION} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws UnsupportedOperationException
     * 		if the route does not support session management.
     * @see MediaControlIntent#ACTION_START_SESSION
     * @see #isRemotePlaybackSupported
     * @see #isSessionManagementSupported
     */
    public void startSession(android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfSessionManagementNotSupported();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_START_SESSION);
        intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_STATUS_UPDATE_RECEIVER, mSessionStatusPendingIntent);
        if (mRouteSupportsMessaging) {
            intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_MESSAGE_RECEIVER, mMessagePendingIntent);
        }
        performSessionAction(intent, null, extras, callback);
    }

    /**
     * Sends a message.
     * <p>
     * The request is issued in the current session.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_SEND_MESSAGE} for
     * more information about the semantics of this request.
     * </p>
     *
     * @param message
     * 		A bundle message denoting {@link MediaControlIntent#EXTRA_MESSAGE}.
     * @param callback
     * 		A callback to invoke when the request has been processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @throws UnsupportedOperationException
     * 		if the route does not support messages.
     * @see MediaControlIntent#ACTION_SEND_MESSAGE
     * @see #isMessagingSupported
     */
    public void sendMessage(android.os.Bundle message, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfNoCurrentSession();
        throwIfMessageNotSupported();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_SEND_MESSAGE);
        performSessionAction(intent, mSessionId, message, callback);
    }

    /**
     * Sends a request to get the status of the media playback session.
     * <p>
     * The request is issued in the current session.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_GET_SESSION_STATUS
     * ACTION_GET_SESSION_STATUS} for more information about the semantics of this request.
     * </p>
     *
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_GET_SESSION_STATUS} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @throws UnsupportedOperationException
     * 		if the route does not support session management.
     * @see MediaControlIntent#ACTION_GET_SESSION_STATUS
     * @see #isRemotePlaybackSupported
     * @see #isSessionManagementSupported
     */
    public void getSessionStatus(android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfSessionManagementNotSupported();
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_GET_SESSION_STATUS);
        performSessionAction(intent, mSessionId, extras, callback);
    }

    /**
     * Sends a request to end the media playback session.
     * <p>
     * The request is issued in the current session.  If this request is successful,
     * the {@link #getSessionId session id property} will be set to null after
     * the callback is invoked.
     * </p><p>
     * Please refer to {@link MediaControlIntent#ACTION_END_SESSION ACTION_END_SESSION}
     * for more information about the semantics of this request.
     * </p>
     *
     * @param extras
     * 		A bundle of extra arguments to be added to the
     * 		{@link MediaControlIntent#ACTION_END_SESSION} intent, or null if none.
     * @param callback
     * 		A callback to invoke when the request has been
     * 		processed, or null if none.
     * @throws IllegalStateException
     * 		if there is no current session.
     * @throws UnsupportedOperationException
     * 		if the route does not support session management.
     * @see MediaControlIntent#ACTION_END_SESSION
     * @see #isRemotePlaybackSupported
     * @see #isSessionManagementSupported
     */
    public void endSession(android.os.Bundle extras, android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        throwIfSessionManagementNotSupported();
        throwIfNoCurrentSession();
        android.content.Intent intent = new android.content.Intent(android.support.v7.media.MediaControlIntent.ACTION_END_SESSION);
        performSessionAction(intent, mSessionId, extras, callback);
    }

    private void performItemAction(final android.content.Intent intent, final java.lang.String sessionId, final java.lang.String itemId, android.os.Bundle extras, final android.support.v7.media.RemotePlaybackClient.ItemActionCallback callback) {
        intent.addCategory(android.support.v7.media.MediaControlIntent.CATEGORY_REMOTE_PLAYBACK);
        if (sessionId != null) {
            intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_ID, sessionId);
        }
        if (itemId != null) {
            intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_ID, itemId);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        android.support.v7.media.RemotePlaybackClient.logRequest(intent);
        mRoute.sendControlRequest(intent, new android.support.v7.media.MediaRouter.ControlRequestCallback() {
            @java.lang.Override
            public void onResult(android.os.Bundle data) {
                if (data != null) {
                    java.lang.String sessionIdResult = android.support.v7.media.RemotePlaybackClient.inferMissingResult(sessionId, data.getString(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_ID));
                    android.support.v7.media.MediaSessionStatus sessionStatus = android.support.v7.media.MediaSessionStatus.fromBundle(data.getBundle(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_STATUS));
                    java.lang.String itemIdResult = android.support.v7.media.RemotePlaybackClient.inferMissingResult(itemId, data.getString(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_ID));
                    android.support.v7.media.MediaItemStatus itemStatus = android.support.v7.media.MediaItemStatus.fromBundle(data.getBundle(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_STATUS));
                    adoptSession(sessionIdResult);
                    if (((sessionIdResult != null) && (itemIdResult != null)) && (itemStatus != null)) {
                        if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
                            android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, (((((((((("Received result from " + intent.getAction()) + ": data=") + android.support.v7.media.RemotePlaybackClient.bundleToString(data)) + ", sessionId=") + sessionIdResult) + ", sessionStatus=") + sessionStatus) + ", itemId=") + itemIdResult) + ", itemStatus=") + itemStatus);
                        }
                        callback.onResult(data, sessionIdResult, sessionStatus, itemIdResult, itemStatus);
                        return;
                    }
                }
                handleInvalidResult(intent, callback, data);
            }

            @java.lang.Override
            public void onError(java.lang.String error, android.os.Bundle data) {
                handleError(intent, callback, error, data);
            }
        });
    }

    private void performSessionAction(final android.content.Intent intent, final java.lang.String sessionId, android.os.Bundle extras, final android.support.v7.media.RemotePlaybackClient.SessionActionCallback callback) {
        intent.addCategory(android.support.v7.media.MediaControlIntent.CATEGORY_REMOTE_PLAYBACK);
        if (sessionId != null) {
            intent.putExtra(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_ID, sessionId);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        android.support.v7.media.RemotePlaybackClient.logRequest(intent);
        mRoute.sendControlRequest(intent, new android.support.v7.media.MediaRouter.ControlRequestCallback() {
            @java.lang.Override
            public void onResult(android.os.Bundle data) {
                if (data != null) {
                    java.lang.String sessionIdResult = android.support.v7.media.RemotePlaybackClient.inferMissingResult(sessionId, data.getString(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_ID));
                    android.support.v7.media.MediaSessionStatus sessionStatus = android.support.v7.media.MediaSessionStatus.fromBundle(data.getBundle(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_STATUS));
                    adoptSession(sessionIdResult);
                    if (sessionIdResult != null) {
                        if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
                            android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, (((((("Received result from " + intent.getAction()) + ": data=") + android.support.v7.media.RemotePlaybackClient.bundleToString(data)) + ", sessionId=") + sessionIdResult) + ", sessionStatus=") + sessionStatus);
                        }
                        try {
                            callback.onResult(data, sessionIdResult, sessionStatus);
                        } finally {
                            if (intent.getAction().equals(android.support.v7.media.MediaControlIntent.ACTION_END_SESSION) && sessionIdResult.equals(mSessionId)) {
                                setSessionId(null);
                            }
                        }
                        return;
                    }
                }
                handleInvalidResult(intent, callback, data);
            }

            @java.lang.Override
            public void onError(java.lang.String error, android.os.Bundle data) {
                handleError(intent, callback, error, data);
            }
        });
    }

    void adoptSession(java.lang.String sessionId) {
        if (sessionId != null) {
            setSessionId(sessionId);
        }
    }

    void handleInvalidResult(android.content.Intent intent, android.support.v7.media.RemotePlaybackClient.ActionCallback callback, android.os.Bundle data) {
        android.util.Log.w(android.support.v7.media.RemotePlaybackClient.TAG, (("Received invalid result data from " + intent.getAction()) + ": data=") + android.support.v7.media.RemotePlaybackClient.bundleToString(data));
        callback.onError(null, android.support.v7.media.MediaControlIntent.ERROR_UNKNOWN, data);
    }

    void handleError(android.content.Intent intent, android.support.v7.media.RemotePlaybackClient.ActionCallback callback, java.lang.String error, android.os.Bundle data) {
        final int code;
        if (data != null) {
            code = data.getInt(android.support.v7.media.MediaControlIntent.EXTRA_ERROR_CODE, android.support.v7.media.MediaControlIntent.ERROR_UNKNOWN);
        } else {
            code = android.support.v7.media.MediaControlIntent.ERROR_UNKNOWN;
        }
        if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
            android.util.Log.w(android.support.v7.media.RemotePlaybackClient.TAG, (((((("Received error from " + intent.getAction()) + ": error=") + error) + ", code=") + code) + ", data=") + android.support.v7.media.RemotePlaybackClient.bundleToString(data));
        }
        callback.onError(error, code, data);
    }

    private void detectFeatures() {
        mRouteSupportsRemotePlayback = ((((routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_PLAY) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_SEEK)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_GET_STATUS)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_PAUSE)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_RESUME)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_STOP);
        mRouteSupportsQueuing = (mRouteSupportsRemotePlayback && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_ENQUEUE)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_REMOVE);
        mRouteSupportsSessionManagement = ((mRouteSupportsRemotePlayback && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_START_SESSION)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_GET_SESSION_STATUS)) && routeSupportsAction(android.support.v7.media.MediaControlIntent.ACTION_END_SESSION);
        mRouteSupportsMessaging = doesRouteSupportMessaging();
    }

    private boolean routeSupportsAction(java.lang.String action) {
        return mRoute.supportsControlAction(android.support.v7.media.MediaControlIntent.CATEGORY_REMOTE_PLAYBACK, action);
    }

    private boolean doesRouteSupportMessaging() {
        for (android.content.IntentFilter filter : mRoute.getControlFilters()) {
            if (filter.hasAction(android.support.v7.media.MediaControlIntent.ACTION_SEND_MESSAGE)) {
                return true;
            }
        }
        return false;
    }

    private void throwIfRemotePlaybackNotSupported() {
        if (!mRouteSupportsRemotePlayback) {
            throw new java.lang.UnsupportedOperationException("The route does not support remote playback.");
        }
    }

    private void throwIfQueuingNotSupported() {
        if (!mRouteSupportsQueuing) {
            throw new java.lang.UnsupportedOperationException("The route does not support queuing.");
        }
    }

    private void throwIfSessionManagementNotSupported() {
        if (!mRouteSupportsSessionManagement) {
            throw new java.lang.UnsupportedOperationException("The route does not support " + "session management.");
        }
    }

    private void throwIfMessageNotSupported() {
        if (!mRouteSupportsMessaging) {
            throw new java.lang.UnsupportedOperationException("The route does not support message.");
        }
    }

    private void throwIfNoCurrentSession() {
        if (mSessionId == null) {
            throw new java.lang.IllegalStateException("There is no current session.");
        }
    }

    static java.lang.String inferMissingResult(java.lang.String request, java.lang.String result) {
        if (result == null) {
            // Result is missing.
            return request;
        }
        if ((request == null) || request.equals(result)) {
            // Request didn't specify a value or result matches request.
            return result;
        }
        // Result conflicts with request.
        return null;
    }

    private static void logRequest(android.content.Intent intent) {
        if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
            android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, "Sending request: " + intent);
        }
    }

    static java.lang.String bundleToString(android.os.Bundle bundle) {
        if (bundle != null) {
            bundle.size();// force bundle to be unparcelled

            return bundle.toString();
        }
        return "null";
    }

    private final class ActionReceiver extends android.content.BroadcastReceiver {
        public static final java.lang.String ACTION_ITEM_STATUS_CHANGED = "android.support.v7.media.actions.ACTION_ITEM_STATUS_CHANGED";

        public static final java.lang.String ACTION_SESSION_STATUS_CHANGED = "android.support.v7.media.actions.ACTION_SESSION_STATUS_CHANGED";

        public static final java.lang.String ACTION_MESSAGE_RECEIVED = "android.support.v7.media.actions.ACTION_MESSAGE_RECEIVED";

        ActionReceiver() {
        }

        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String sessionId = intent.getStringExtra(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_ID);
            if ((sessionId == null) || (!sessionId.equals(mSessionId))) {
                android.util.Log.w(android.support.v7.media.RemotePlaybackClient.TAG, ("Discarding spurious status callback " + "with missing or invalid session id: sessionId=") + sessionId);
                return;
            }
            android.support.v7.media.MediaSessionStatus sessionStatus = android.support.v7.media.MediaSessionStatus.fromBundle(intent.getBundleExtra(android.support.v7.media.MediaControlIntent.EXTRA_SESSION_STATUS));
            java.lang.String action = intent.getAction();
            if (action.equals(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_ITEM_STATUS_CHANGED)) {
                java.lang.String itemId = intent.getStringExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_ID);
                if (itemId == null) {
                    android.util.Log.w(android.support.v7.media.RemotePlaybackClient.TAG, "Discarding spurious status callback with missing item id.");
                    return;
                }
                android.support.v7.media.MediaItemStatus itemStatus = android.support.v7.media.MediaItemStatus.fromBundle(intent.getBundleExtra(android.support.v7.media.MediaControlIntent.EXTRA_ITEM_STATUS));
                if (itemStatus == null) {
                    android.util.Log.w(android.support.v7.media.RemotePlaybackClient.TAG, "Discarding spurious status callback with missing item status.");
                    return;
                }
                if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
                    android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, (((((("Received item status callback: sessionId=" + sessionId) + ", sessionStatus=") + sessionStatus) + ", itemId=") + itemId) + ", itemStatus=") + itemStatus);
                }
                if (mStatusCallback != null) {
                    mStatusCallback.onItemStatusChanged(intent.getExtras(), sessionId, sessionStatus, itemId, itemStatus);
                }
            } else
                if (action.equals(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_SESSION_STATUS_CHANGED)) {
                    if (sessionStatus == null) {
                        android.util.Log.w(android.support.v7.media.RemotePlaybackClient.TAG, "Discarding spurious media status callback with " + "missing session status.");
                        return;
                    }
                    if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
                        android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, (("Received session status callback: sessionId=" + sessionId) + ", sessionStatus=") + sessionStatus);
                    }
                    if (mStatusCallback != null) {
                        mStatusCallback.onSessionStatusChanged(intent.getExtras(), sessionId, sessionStatus);
                    }
                } else
                    if (action.equals(android.support.v7.media.RemotePlaybackClient.ActionReceiver.ACTION_MESSAGE_RECEIVED)) {
                        if (android.support.v7.media.RemotePlaybackClient.DEBUG) {
                            android.util.Log.d(android.support.v7.media.RemotePlaybackClient.TAG, "Received message callback: sessionId=" + sessionId);
                        }
                        if (mOnMessageReceivedListener != null) {
                            mOnMessageReceivedListener.onMessageReceived(sessionId, intent.getBundleExtra(android.support.v7.media.MediaControlIntent.EXTRA_MESSAGE));
                        }
                    }


        }
    }

    /**
     * A callback that will receive media status updates.
     */
    public static abstract class StatusCallback {
        /**
         * Called when the status of a media item changes.
         *
         * @param data
         * 		The result data bundle.
         * @param sessionId
         * 		The session id.
         * @param sessionStatus
         * 		The session status, or null if unknown.
         * @param itemId
         * 		The item id.
         * @param itemStatus
         * 		The item status.
         */
        public void onItemStatusChanged(android.os.Bundle data, java.lang.String sessionId, android.support.v7.media.MediaSessionStatus sessionStatus, java.lang.String itemId, android.support.v7.media.MediaItemStatus itemStatus) {
        }

        /**
         * Called when the status of a media session changes.
         *
         * @param data
         * 		The result data bundle.
         * @param sessionId
         * 		The session id.
         * @param sessionStatus
         * 		The session status, or null if unknown.
         */
        public void onSessionStatusChanged(android.os.Bundle data, java.lang.String sessionId, android.support.v7.media.MediaSessionStatus sessionStatus) {
        }

        /**
         * Called when the session of the remote playback client changes.
         *
         * @param sessionId
         * 		The new session id.
         */
        public void onSessionChanged(java.lang.String sessionId) {
        }
    }

    /**
     * Base callback type for remote playback requests.
     */
    public static abstract class ActionCallback {
        /**
         * Called when a media control request fails.
         *
         * @param error
         * 		A localized error message which may be shown to the user, or null
         * 		if the cause of the error is unclear.
         * @param code
         * 		The error code, or {@link MediaControlIntent#ERROR_UNKNOWN} if unknown.
         * @param data
         * 		The error data bundle, or null if none.
         */
        public void onError(java.lang.String error, int code, android.os.Bundle data) {
        }
    }

    /**
     * Callback for remote playback requests that operate on items.
     */
    public static abstract class ItemActionCallback extends android.support.v7.media.RemotePlaybackClient.ActionCallback {
        /**
         * Called when the request succeeds.
         *
         * @param data
         * 		The result data bundle.
         * @param sessionId
         * 		The session id.
         * @param sessionStatus
         * 		The session status, or null if unknown.
         * @param itemId
         * 		The item id.
         * @param itemStatus
         * 		The item status.
         */
        public void onResult(android.os.Bundle data, java.lang.String sessionId, android.support.v7.media.MediaSessionStatus sessionStatus, java.lang.String itemId, android.support.v7.media.MediaItemStatus itemStatus) {
        }
    }

    /**
     * Callback for remote playback requests that operate on sessions.
     */
    public static abstract class SessionActionCallback extends android.support.v7.media.RemotePlaybackClient.ActionCallback {
        /**
         * Called when the request succeeds.
         *
         * @param data
         * 		The result data bundle.
         * @param sessionId
         * 		The session id.
         * @param sessionStatus
         * 		The session status, or null if unknown.
         */
        public void onResult(android.os.Bundle data, java.lang.String sessionId, android.support.v7.media.MediaSessionStatus sessionStatus) {
        }
    }

    /**
     * A callback that will receive messages from media sessions.
     */
    public interface OnMessageReceivedListener {
        /**
         * Called when a message received.
         *
         * @param sessionId
         * 		The session id.
         * @param message
         * 		A bundle message denoting {@link MediaControlIntent#EXTRA_MESSAGE}.
         */
        void onMessageReceived(java.lang.String sessionId, android.os.Bundle message);
    }
}

