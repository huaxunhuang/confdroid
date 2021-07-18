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
 * Browses media content offered by a {@link MediaBrowserServiceCompat}.
 * <p>
 * This object is not thread-safe. All calls should happen on the thread on which the browser
 * was constructed.
 * </p>
 */
public final class MediaBrowserCompat {
    static final java.lang.String TAG = "MediaBrowserCompat";

    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v4.media.MediaBrowserCompat.TAG, android.util.Log.DEBUG);

    /**
     * Used as an int extra field to denote the page number to subscribe.
     * The value of {@code EXTRA_PAGE} should be greater than or equal to 1.
     *
     * @see android.service.media.MediaBrowserService.BrowserRoot
     * @see #EXTRA_PAGE_SIZE
     */
    public static final java.lang.String EXTRA_PAGE = "android.media.browse.extra.PAGE";

    /**
     * Used as an int extra field to denote the number of media items in a page.
     * The value of {@code EXTRA_PAGE_SIZE} should be greater than or equal to 1.
     *
     * @see android.service.media.MediaBrowserService.BrowserRoot
     * @see #EXTRA_PAGE
     */
    public static final java.lang.String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";

    private final android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl mImpl;

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
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_RECENT
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_OFFLINE
     * @see MediaBrowserServiceCompat.BrowserRoot#EXTRA_SUGGESTED
     */
    public MediaBrowserCompat(android.content.Context context, android.content.ComponentName serviceComponent, android.support.v4.media.MediaBrowserCompat.ConnectionCallback callback, android.os.Bundle rootHints) {
        if ((android.os.Build.VERSION.SDK_INT >= 24) || android.support.v4.os.BuildCompat.isAtLeastN()) {
            mImpl = new android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi24(context, serviceComponent, callback, rootHints);
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                mImpl = new android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi23(context, serviceComponent, callback, rootHints);
            } else
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    mImpl = new android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi21(context, serviceComponent, callback, rootHints);
                } else {
                    mImpl = new android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase(context, serviceComponent, callback, rootHints);
                }


    }

    /**
     * Connects to the media browse service.
     * <p>
     * The connection callback specified in the constructor will be invoked
     * when the connection completes or fails.
     * </p>
     */
    public void connect() {
        mImpl.connect();
    }

    /**
     * Disconnects from the media browse service.
     * After this, no more callbacks will be received.
     */
    public void disconnect() {
        mImpl.disconnect();
    }

    /**
     * Returns whether the browser is connected to the service.
     */
    public boolean isConnected() {
        return mImpl.isConnected();
    }

    /**
     * Gets the service component that the media browser is connected to.
     */
    @android.support.annotation.NonNull
    public android.content.ComponentName getServiceComponent() {
        return mImpl.getServiceComponent();
    }

    /**
     * Gets the root id.
     * <p>
     * Note that the root id may become invalid or change when when the
     * browser is disconnected.
     * </p>
     *
     * @throws IllegalStateException
     * 		if not connected.
     */
    @android.support.annotation.NonNull
    public java.lang.String getRoot() {
        return mImpl.getRoot();
    }

    /**
     * Gets any extras for the media service.
     *
     * @throws IllegalStateException
     * 		if not connected.
     */
    @android.support.annotation.Nullable
    public android.os.Bundle getExtras() {
        return mImpl.getExtras();
    }

    /**
     * Gets the media session token associated with the media browser.
     * <p>
     * Note that the session token may become invalid or change when when the
     * browser is disconnected.
     * </p>
     *
     * @return The session token for the browser, never null.
     * @throws IllegalStateException
     * 		if not connected.
     */
    @android.support.annotation.NonNull
    public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
        return mImpl.getSessionToken();
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
    public void subscribe(@android.support.annotation.NonNull
    java.lang.String parentId, @android.support.annotation.NonNull
    android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
        // Check arguments.
        if (android.text.TextUtils.isEmpty(parentId)) {
            throw new java.lang.IllegalArgumentException("parentId is empty");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback is null");
        }
        mImpl.subscribe(parentId, null, callback);
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
    public void subscribe(@android.support.annotation.NonNull
    java.lang.String parentId, @android.support.annotation.NonNull
    android.os.Bundle options, @android.support.annotation.NonNull
    android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
        // Check arguments.
        if (android.text.TextUtils.isEmpty(parentId)) {
            throw new java.lang.IllegalArgumentException("parentId is empty");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback is null");
        }
        if (options == null) {
            throw new java.lang.IllegalArgumentException("options are null");
        }
        mImpl.subscribe(parentId, options, callback);
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
    public void unsubscribe(@android.support.annotation.NonNull
    java.lang.String parentId) {
        // Check arguments.
        if (android.text.TextUtils.isEmpty(parentId)) {
            throw new java.lang.IllegalArgumentException("parentId is empty");
        }
        mImpl.unsubscribe(parentId, null);
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
     * @param callback
     * 		A callback sent to the media browse service to subscribe.
     */
    public void unsubscribe(@android.support.annotation.NonNull
    java.lang.String parentId, @android.support.annotation.NonNull
    android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
        // Check arguments.
        if (android.text.TextUtils.isEmpty(parentId)) {
            throw new java.lang.IllegalArgumentException("parentId is empty");
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback is null");
        }
        mImpl.unsubscribe(parentId, callback);
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
    public void getItem(@android.support.annotation.NonNull
    final java.lang.String mediaId, @android.support.annotation.NonNull
    final android.support.v4.media.MediaBrowserCompat.ItemCallback cb) {
        mImpl.getItem(mediaId, cb);
    }

    /**
     * A class with information on a single media item for use in browsing media.
     */
    public static class MediaItem implements android.os.Parcelable {
        private final int mFlags;

        private final android.support.v4.media.MediaDescriptionCompat mDescription;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.support.annotation.IntDef(flag = true, value = { android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE, android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE })
        public @interface Flags {}

        /**
         * Flag: Indicates that the item has children of its own.
         */
        public static final int FLAG_BROWSABLE = 1 << 0;

        /**
         * Flag: Indicates that the item is playable.
         * <p>
         * The id of this item may be passed to
         * {@link MediaControllerCompat.TransportControls#playFromMediaId(String, Bundle)}
         * to start playing it.
         * </p>
         */
        public static final int FLAG_PLAYABLE = 1 << 1;

        /**
         * Creates an instance from a framework {@link android.media.browse.MediaBrowser.MediaItem}
         * object.
         * <p>
         * This method is only supported on API 21+. On API 20 and below, it returns null.
         * </p>
         *
         * @param itemObj
         * 		A {@link android.media.browse.MediaBrowser.MediaItem} object.
         * @return An equivalent {@link MediaItem} object, or null if none.
         */
        public static android.support.v4.media.MediaBrowserCompat.MediaItem fromMediaItem(java.lang.Object itemObj) {
            if ((itemObj == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return null;
            }
            int flags = android.support.v4.media.MediaBrowserCompatApi21.MediaItem.getFlags(itemObj);
            android.support.v4.media.MediaDescriptionCompat description = android.support.v4.media.MediaDescriptionCompat.fromMediaDescription(android.support.v4.media.MediaBrowserCompatApi21.MediaItem.getDescription(itemObj));
            return new android.support.v4.media.MediaBrowserCompat.MediaItem(description, flags);
        }

        /**
         * Creates a list of {@link MediaItem} objects from a framework
         * {@link android.media.browse.MediaBrowser.MediaItem} object list.
         * <p>
         * This method is only supported on API 21+. On API 20 and below, it returns null.
         * </p>
         *
         * @param itemList
         * 		A list of {@link android.media.browse.MediaBrowser.MediaItem} objects.
         * @return An equivalent list of {@link MediaItem} objects, or null if none.
         */
        public static java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> fromMediaItemList(java.util.List<?> itemList) {
            if ((itemList == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
                return null;
            }
            java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> items = new java.util.ArrayList<>(itemList.size());
            for (java.lang.Object itemObj : itemList) {
                items.add(android.support.v4.media.MediaBrowserCompat.MediaItem.fromMediaItem(itemObj));
            }
            return items;
        }

        /**
         * Create a new MediaItem for use in browsing media.
         *
         * @param description
         * 		The description of the media, which must include a
         * 		media id.
         * @param flags
         * 		The flags for this item.
         */
        public MediaItem(@android.support.annotation.NonNull
        android.support.v4.media.MediaDescriptionCompat description, @android.support.v4.media.MediaBrowserCompat.MediaItem.Flags
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
        MediaItem(android.os.Parcel in) {
            mFlags = in.readInt();
            mDescription = android.support.v4.media.MediaDescriptionCompat.CREATOR.createFromParcel(in);
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

        public static final android.os.Parcelable.Creator<android.support.v4.media.MediaBrowserCompat.MediaItem> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.MediaBrowserCompat.MediaItem>() {
            @java.lang.Override
            public android.support.v4.media.MediaBrowserCompat.MediaItem createFromParcel(android.os.Parcel in) {
                return new android.support.v4.media.MediaBrowserCompat.MediaItem(in);
            }

            @java.lang.Override
            public android.support.v4.media.MediaBrowserCompat.MediaItem[] newArray(int size) {
                return new android.support.v4.media.MediaBrowserCompat.MediaItem[size];
            }
        };

        /**
         * Gets the flags of the item.
         */
        @android.support.v4.media.MediaBrowserCompat.MediaItem.Flags
        public int getFlags() {
            return mFlags;
        }

        /**
         * Returns whether this item is browsable.
         *
         * @see #FLAG_BROWSABLE
         */
        public boolean isBrowsable() {
            return (mFlags & android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE) != 0;
        }

        /**
         * Returns whether this item is playable.
         *
         * @see #FLAG_PLAYABLE
         */
        public boolean isPlayable() {
            return (mFlags & android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE) != 0;
        }

        /**
         * Returns the description of the media.
         */
        @android.support.annotation.NonNull
        public android.support.v4.media.MediaDescriptionCompat getDescription() {
            return mDescription;
        }

        /**
         * Returns the media id in the {@link MediaDescriptionCompat} for this item.
         *
         * @see MediaMetadataCompat#METADATA_KEY_MEDIA_ID
         */
        @android.support.annotation.NonNull
        public java.lang.String getMediaId() {
            return mDescription.getMediaId();
        }
    }

    /**
     * Callbacks for connection related events.
     */
    public static class ConnectionCallback {
        final java.lang.Object mConnectionCallbackObj;

        android.support.v4.media.MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal mConnectionCallbackInternal;

        public ConnectionCallback() {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                mConnectionCallbackObj = android.support.v4.media.MediaBrowserCompatApi21.createConnectionCallback(new android.support.v4.media.MediaBrowserCompat.ConnectionCallback.StubApi21());
            } else {
                mConnectionCallbackObj = null;
            }
        }

        /**
         * Invoked after {@link MediaBrowserCompat#connect()} when the request has successfully
         * completed.
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

        void setInternalConnectionCallback(android.support.v4.media.MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal connectionCallbackInternal) {
            mConnectionCallbackInternal = connectionCallbackInternal;
        }

        interface ConnectionCallbackInternal {
            void onConnected();

            void onConnectionSuspended();

            void onConnectionFailed();
        }

        private class StubApi21 implements android.support.v4.media.MediaBrowserCompatApi21.ConnectionCallback {
            StubApi21() {
            }

            @java.lang.Override
            public void onConnected() {
                if (mConnectionCallbackInternal != null) {
                    mConnectionCallbackInternal.onConnected();
                }
                android.support.v4.media.MediaBrowserCompat.ConnectionCallback.this.onConnected();
            }

            @java.lang.Override
            public void onConnectionSuspended() {
                if (mConnectionCallbackInternal != null) {
                    mConnectionCallbackInternal.onConnectionSuspended();
                }
                android.support.v4.media.MediaBrowserCompat.ConnectionCallback.this.onConnectionSuspended();
            }

            @java.lang.Override
            public void onConnectionFailed() {
                if (mConnectionCallbackInternal != null) {
                    mConnectionCallbackInternal.onConnectionFailed();
                }
                android.support.v4.media.MediaBrowserCompat.ConnectionCallback.this.onConnectionFailed();
            }
        }
    }

    /**
     * Callbacks for subscription related events.
     */
    public static abstract class SubscriptionCallback {
        private final java.lang.Object mSubscriptionCallbackObj;

        private final android.os.IBinder mToken;

        java.lang.ref.WeakReference<android.support.v4.media.MediaBrowserCompat.Subscription> mSubscriptionRef;

        public SubscriptionCallback() {
            if ((android.os.Build.VERSION.SDK_INT >= 24) || android.support.v4.os.BuildCompat.isAtLeastN()) {
                mSubscriptionCallbackObj = android.support.v4.media.MediaBrowserCompatApi24.createSubscriptionCallback(new android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.StubApi24());
                mToken = null;
            } else
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    mSubscriptionCallbackObj = android.support.v4.media.MediaBrowserCompatApi21.createSubscriptionCallback(new android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.StubApi21());
                    mToken = new android.os.Binder();
                } else {
                    mSubscriptionCallbackObj = null;
                    mToken = new android.os.Binder();
                }

        }

        /**
         * Called when the list of children is loaded or updated.
         *
         * @param parentId
         * 		The media id of the parent media item.
         * @param children
         * 		The children which were loaded, or null if the id is invalid.
         */
        public void onChildrenLoaded(@android.support.annotation.NonNull
        java.lang.String parentId, java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> children) {
        }

        /**
         * Called when the list of children is loaded or updated.
         *
         * @param parentId
         * 		The media id of the parent media item.
         * @param children
         * 		The children which were loaded, or null if the id is invalid.
         * @param options
         * 		A bundle of service-specific arguments to send to the media
         * 		browse service. The contents of this bundle may affect the
         * 		information returned when browsing.
         */
        public void onChildrenLoaded(@android.support.annotation.NonNull
        java.lang.String parentId, java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> children, @android.support.annotation.NonNull
        android.os.Bundle options) {
        }

        /**
         * Called when the id doesn't exist or other errors in subscribing.
         * <p>
         * If this is called, the subscription remains until {@link MediaBrowserCompat#unsubscribe}
         * called, because some errors may heal themselves.
         * </p>
         *
         * @param parentId
         * 		The media id of the parent media item whose children could not be loaded.
         */
        public void onError(@android.support.annotation.NonNull
        java.lang.String parentId) {
        }

        /**
         * Called when the id doesn't exist or other errors in subscribing.
         * <p>
         * If this is called, the subscription remains until {@link MediaBrowserCompat#unsubscribe}
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
        public void onError(@android.support.annotation.NonNull
        java.lang.String parentId, @android.support.annotation.NonNull
        android.os.Bundle options) {
        }

        private void setSubscription(android.support.v4.media.MediaBrowserCompat.Subscription subscription) {
            mSubscriptionRef = new java.lang.ref.WeakReference(subscription);
        }

        private class StubApi21 implements android.support.v4.media.MediaBrowserCompatApi21.SubscriptionCallback {
            StubApi21() {
            }

            @java.lang.Override
            public void onChildrenLoaded(@android.support.annotation.NonNull
            java.lang.String parentId, java.util.List<?> children) {
                android.support.v4.media.MediaBrowserCompat.Subscription sub = (mSubscriptionRef == null) ? null : mSubscriptionRef.get();
                if (sub == null) {
                    android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(parentId, android.support.v4.media.MediaBrowserCompat.MediaItem.fromMediaItemList(children));
                } else {
                    java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> itemList = android.support.v4.media.MediaBrowserCompat.MediaItem.fromMediaItemList(children);
                    final java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> callbacks = sub.getCallbacks();
                    final java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                    for (int i = 0; i < callbacks.size(); ++i) {
                        android.os.Bundle options = optionsList.get(i);
                        if (options == null) {
                            android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(parentId, itemList);
                        } else {
                            android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(parentId, applyOptions(itemList, options), options);
                        }
                    }
                }
            }

            @java.lang.Override
            public void onError(@android.support.annotation.NonNull
            java.lang.String parentId) {
                android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.this.onError(parentId);
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
        }

        private class StubApi24 extends android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.StubApi21 implements android.support.v4.media.MediaBrowserCompatApi24.SubscriptionCallback {
            StubApi24() {
            }

            @java.lang.Override
            public void onChildrenLoaded(@android.support.annotation.NonNull
            java.lang.String parentId, java.util.List<?> children, @android.support.annotation.NonNull
            android.os.Bundle options) {
                android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(parentId, android.support.v4.media.MediaBrowserCompat.MediaItem.fromMediaItemList(children), options);
            }

            @java.lang.Override
            public void onError(@android.support.annotation.NonNull
            java.lang.String parentId, @android.support.annotation.NonNull
            android.os.Bundle options) {
                android.support.v4.media.MediaBrowserCompat.SubscriptionCallback.this.onError(parentId, options);
            }
        }
    }

    /**
     * Callback for receiving the result of {@link #getItem}.
     */
    public static abstract class ItemCallback {
        final java.lang.Object mItemCallbackObj;

        public ItemCallback() {
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                mItemCallbackObj = android.support.v4.media.MediaBrowserCompatApi23.createItemCallback(new android.support.v4.media.MediaBrowserCompat.ItemCallback.StubApi23());
            } else {
                mItemCallbackObj = null;
            }
        }

        /**
         * Called when the item has been returned by the browser service.
         *
         * @param item
         * 		The item that was returned or null if it doesn't exist.
         */
        public void onItemLoaded(android.support.v4.media.MediaBrowserCompat.MediaItem item) {
        }

        /**
         * Called when the item doesn't exist or there was an error retrieving it.
         *
         * @param itemId
         * 		The media id of the media item which could not be loaded.
         */
        public void onError(@android.support.annotation.NonNull
        java.lang.String itemId) {
        }

        private class StubApi23 implements android.support.v4.media.MediaBrowserCompatApi23.ItemCallback {
            StubApi23() {
            }

            @java.lang.Override
            public void onItemLoaded(android.os.Parcel itemParcel) {
                itemParcel.setDataPosition(0);
                android.support.v4.media.MediaBrowserCompat.MediaItem item = android.support.v4.media.MediaBrowserCompat.MediaItem.CREATOR.createFromParcel(itemParcel);
                itemParcel.recycle();
                android.support.v4.media.MediaBrowserCompat.ItemCallback.this.onItemLoaded(item);
            }

            @java.lang.Override
            public void onError(@android.support.annotation.NonNull
            java.lang.String itemId) {
                android.support.v4.media.MediaBrowserCompat.ItemCallback.this.onError(itemId);
            }
        }
    }

    interface MediaBrowserImpl {
        void connect();

        void disconnect();

        boolean isConnected();

        android.content.ComponentName getServiceComponent();

        @android.support.annotation.NonNull
        java.lang.String getRoot();

        @android.support.annotation.Nullable
        android.os.Bundle getExtras();

        @android.support.annotation.NonNull
        android.support.v4.media.session.MediaSessionCompat.Token getSessionToken();

        void subscribe(@android.support.annotation.NonNull
        java.lang.String parentId, android.os.Bundle options, @android.support.annotation.NonNull
        android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback);

        void unsubscribe(@android.support.annotation.NonNull
        java.lang.String parentId, android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback);

        void getItem(@android.support.annotation.NonNull
        final java.lang.String mediaId, @android.support.annotation.NonNull
        final android.support.v4.media.MediaBrowserCompat.ItemCallback cb);
    }

    interface MediaBrowserServiceCallbackImpl {
        void onServiceConnected(android.os.Messenger callback, java.lang.String root, android.support.v4.media.session.MediaSessionCompat.Token session, android.os.Bundle extra);

        void onConnectionFailed(android.os.Messenger callback);

        void onLoadChildren(android.os.Messenger callback, java.lang.String parentId, java.util.List list, android.os.Bundle options);
    }

    static class MediaBrowserImplBase implements android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl , android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl {
        static final int CONNECT_STATE_DISCONNECTED = 0;

        static final int CONNECT_STATE_CONNECTING = 1;

        private static final int CONNECT_STATE_CONNECTED = 2;

        static final int CONNECT_STATE_SUSPENDED = 3;

        final android.content.Context mContext;

        final android.content.ComponentName mServiceComponent;

        final android.support.v4.media.MediaBrowserCompat.ConnectionCallback mCallback;

        final android.os.Bundle mRootHints;

        final android.support.v4.media.MediaBrowserCompat.CallbackHandler mHandler = new android.support.v4.media.MediaBrowserCompat.CallbackHandler(this);

        private final android.support.v4.util.ArrayMap<java.lang.String, android.support.v4.media.MediaBrowserCompat.Subscription> mSubscriptions = new android.support.v4.util.ArrayMap<>();

        int mState = android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_DISCONNECTED;

        android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection mServiceConnection;

        android.support.v4.media.MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;

        android.os.Messenger mCallbacksMessenger;

        private java.lang.String mRootId;

        private android.support.v4.media.session.MediaSessionCompat.Token mMediaSessionToken;

        private android.os.Bundle mExtras;

        public MediaBrowserImplBase(android.content.Context context, android.content.ComponentName serviceComponent, android.support.v4.media.MediaBrowserCompat.ConnectionCallback callback, android.os.Bundle rootHints) {
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

        @java.lang.Override
        public void connect() {
            if (mState != android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_DISCONNECTED) {
                throw new java.lang.IllegalStateException(("connect() called while not disconnected (state=" + android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(mState)) + ")");
            }
            // TODO: remove this extra check.
            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                if (mServiceConnection != null) {
                    throw new java.lang.RuntimeException("mServiceConnection should be null. Instead it is " + mServiceConnection);
                }
            }
            if (mServiceBinderWrapper != null) {
                throw new java.lang.RuntimeException("mServiceBinderWrapper should be null. Instead it is " + mServiceBinderWrapper);
            }
            if (mCallbacksMessenger != null) {
                throw new java.lang.RuntimeException("mCallbacksMessenger should be null. Instead it is " + mCallbacksMessenger);
            }
            mState = android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTING;
            final android.content.Intent intent = new android.content.Intent(android.support.v4.media.MediaBrowserServiceCompat.SERVICE_INTERFACE);
            intent.setComponent(mServiceComponent);
            final android.content.ServiceConnection thisConnection = mServiceConnection = new android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection();
            boolean bound = false;
            try {
                bound = mContext.bindService(intent, mServiceConnection, android.content.Context.BIND_AUTO_CREATE);
            } catch (java.lang.Exception ex) {
                android.util.Log.e(android.support.v4.media.MediaBrowserCompat.TAG, "Failed binding to service " + mServiceComponent);
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
            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "connect...");
                dump();
            }
        }

        @java.lang.Override
        public void disconnect() {
            // It's ok to call this any state, because allowing this lets apps not have
            // to check isConnected() unnecessarily. They won't appreciate the extra
            // assertions for this. We do everything we can here to go back to a sane state.
            if (mCallbacksMessenger != null) {
                try {
                    mServiceBinderWrapper.disconnect(mCallbacksMessenger);
                } catch (android.os.RemoteException ex) {
                    // We are disconnecting anyway. Log, just for posterity but it's not
                    // a big problem.
                    android.util.Log.w(android.support.v4.media.MediaBrowserCompat.TAG, "RemoteException during connect for " + mServiceComponent);
                }
            }
            forceCloseConnection();
            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "disconnect...");
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
        void forceCloseConnection() {
            if (mServiceConnection != null) {
                mContext.unbindService(mServiceConnection);
            }
            mState = android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_DISCONNECTED;
            mServiceConnection = null;
            mServiceBinderWrapper = null;
            mCallbacksMessenger = null;
            mHandler.setCallbacksMessenger(null);
            mRootId = null;
            mMediaSessionToken = null;
        }

        @java.lang.Override
        public boolean isConnected() {
            return mState == android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED;
        }

        @java.lang.Override
        @android.support.annotation.NonNull
        public android.content.ComponentName getServiceComponent() {
            if (!isConnected()) {
                throw new java.lang.IllegalStateException((("getServiceComponent() called while not connected" + " (state=") + mState) + ")");
            }
            return mServiceComponent;
        }

        @java.lang.Override
        @android.support.annotation.NonNull
        public java.lang.String getRoot() {
            if (!isConnected()) {
                throw new java.lang.IllegalStateException((("getRoot() called while not connected" + "(state=") + android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(mState)) + ")");
            }
            return mRootId;
        }

        @java.lang.Override
        @android.support.annotation.Nullable
        public android.os.Bundle getExtras() {
            if (!isConnected()) {
                throw new java.lang.IllegalStateException(("getExtras() called while not connected (state=" + android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(mState)) + ")");
            }
            return mExtras;
        }

        @java.lang.Override
        @android.support.annotation.NonNull
        public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
            if (!isConnected()) {
                throw new java.lang.IllegalStateException((("getSessionToken() called while not connected" + "(state=") + mState) + ")");
            }
            return mMediaSessionToken;
        }

        @java.lang.Override
        public void subscribe(@android.support.annotation.NonNull
        java.lang.String parentId, android.os.Bundle options, @android.support.annotation.NonNull
        android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            // Update or create the subscription.
            android.support.v4.media.MediaBrowserCompat.Subscription sub = mSubscriptions.get(parentId);
            if (sub == null) {
                sub = new android.support.v4.media.MediaBrowserCompat.Subscription();
                mSubscriptions.put(parentId, sub);
            }
            sub.putCallback(options, callback);
            // If we are connected, tell the service that we are watching. If we aren't
            // connected, the service will be told when we connect.
            if (mState == android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED) {
                try {
                    mServiceBinderWrapper.addSubscription(parentId, callback.mToken, options, mCallbacksMessenger);
                } catch (android.os.RemoteException e) {
                    // Process is crashing. We will disconnect, and upon reconnect we will
                    // automatically reregister. So nothing to do here.
                    android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "addSubscription failed with RemoteException parentId=" + parentId);
                }
            }
        }

        @java.lang.Override
        public void unsubscribe(@android.support.annotation.NonNull
        java.lang.String parentId, android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            android.support.v4.media.MediaBrowserCompat.Subscription sub = mSubscriptions.get(parentId);
            if (sub == null) {
                return;
            }
            // Tell the service if necessary.
            try {
                if (callback == null) {
                    if (mState == android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED) {
                        mServiceBinderWrapper.removeSubscription(parentId, null, mCallbacksMessenger);
                    }
                } else {
                    final java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> callbacks = sub.getCallbacks();
                    final java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                    for (int i = callbacks.size() - 1; i >= 0; --i) {
                        if (callbacks.get(i) == callback) {
                            if (mState == android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED) {
                                mServiceBinderWrapper.removeSubscription(parentId, callback.mToken, mCallbacksMessenger);
                            }
                            callbacks.remove(i);
                            optionsList.remove(i);
                        }
                    }
                }
            } catch (android.os.RemoteException ex) {
                // Process is crashing. We will disconnect, and upon reconnect we will
                // automatically reregister. So nothing to do here.
                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "removeSubscription failed with RemoteException parentId=" + parentId);
            }
            if (sub.isEmpty() || (callback == null)) {
                mSubscriptions.remove(parentId);
            }
        }

        @java.lang.Override
        public void getItem(@android.support.annotation.NonNull
        final java.lang.String mediaId, @android.support.annotation.NonNull
        final android.support.v4.media.MediaBrowserCompat.ItemCallback cb) {
            if (android.text.TextUtils.isEmpty(mediaId)) {
                throw new java.lang.IllegalArgumentException("mediaId is empty");
            }
            if (cb == null) {
                throw new java.lang.IllegalArgumentException("cb is null");
            }
            if (mState != android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED) {
                android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Not connected, unable to retrieve the MediaItem.");
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
                return;
            }
            android.support.v4.os.ResultReceiver receiver = new android.support.v4.media.MediaBrowserCompat.ItemReceiver(mediaId, cb, mHandler);
            try {
                mServiceBinderWrapper.getMediaItem(mediaId, receiver, mCallbacksMessenger);
            } catch (android.os.RemoteException e) {
                android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Remote error getting media item.");
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
            }
        }

        @java.lang.Override
        public void onServiceConnected(final android.os.Messenger callback, final java.lang.String root, final android.support.v4.media.session.MediaSessionCompat.Token session, final android.os.Bundle extra) {
            // Check to make sure there hasn't been a disconnect or a different ServiceConnection.
            if (!isCurrent(callback, "onConnect")) {
                return;
            }
            // Don't allow them to call us twice.
            if (mState != android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTING) {
                android.util.Log.w(android.support.v4.media.MediaBrowserCompat.TAG, ("onConnect from service while mState=" + android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(mState)) + "... ignoring");
                return;
            }
            mRootId = root;
            mMediaSessionToken = session;
            mExtras = extra;
            mState = android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED;
            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
                dump();
            }
            mCallback.onConnected();
            // we may receive some subscriptions before we are connected, so re-subscribe
            // everything now
            try {
                for (java.util.Map.Entry<java.lang.String, android.support.v4.media.MediaBrowserCompat.Subscription> subscriptionEntry : mSubscriptions.entrySet()) {
                    java.lang.String id = subscriptionEntry.getKey();
                    android.support.v4.media.MediaBrowserCompat.Subscription sub = subscriptionEntry.getValue();
                    java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> callbackList = sub.getCallbacks();
                    java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                    for (int i = 0; i < callbackList.size(); ++i) {
                        mServiceBinderWrapper.addSubscription(id, callbackList.get(i).mToken, optionsList.get(i), mCallbacksMessenger);
                    }
                }
            } catch (android.os.RemoteException ex) {
                // Process is crashing. We will disconnect, and upon reconnect we will
                // automatically reregister. So nothing to do here.
                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "addSubscription failed with RemoteException.");
            }
        }

        @java.lang.Override
        public void onConnectionFailed(final android.os.Messenger callback) {
            android.util.Log.e(android.support.v4.media.MediaBrowserCompat.TAG, "onConnectFailed for " + mServiceComponent);
            // Check to make sure there hasn't been a disconnect or a different ServiceConnection.
            if (!isCurrent(callback, "onConnectFailed")) {
                return;
            }
            // Don't allow them to call us twice.
            if (mState != android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTING) {
                android.util.Log.w(android.support.v4.media.MediaBrowserCompat.TAG, ("onConnect from service while mState=" + android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(mState)) + "... ignoring");
                return;
            }
            // Clean up
            forceCloseConnection();
            // Tell the app.
            mCallback.onConnectionFailed();
        }

        @java.lang.Override
        public void onLoadChildren(final android.os.Messenger callback, final java.lang.String parentId, final java.util.List list, final android.os.Bundle options) {
            // Check that there hasn't been a disconnect or a different ServiceConnection.
            if (!isCurrent(callback, "onLoadChildren")) {
                return;
            }
            java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem> data = list;
            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, (("onLoadChildren for " + mServiceComponent) + " id=") + parentId);
            }
            // Check that the subscription is still subscribed.
            final android.support.v4.media.MediaBrowserCompat.Subscription subscription = mSubscriptions.get(parentId);
            if (subscription == null) {
                if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                    android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "onLoadChildren for id that isn't subscribed id=" + parentId);
                }
                return;
            }
            // Tell the app.
            android.support.v4.media.MediaBrowserCompat.SubscriptionCallback subscriptionCallback = subscription.getCallback(options);
            if (subscriptionCallback != null) {
                if (options == null) {
                    subscriptionCallback.onChildrenLoaded(parentId, data);
                } else {
                    subscriptionCallback.onChildrenLoaded(parentId, data, options);
                }
            }
        }

        /**
         * For debugging.
         */
        private static java.lang.String getStateLabel(int state) {
            switch (state) {
                case android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_DISCONNECTED :
                    return "CONNECT_STATE_DISCONNECTED";
                case android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTING :
                    return "CONNECT_STATE_CONNECTING";
                case android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTED :
                    return "CONNECT_STATE_CONNECTED";
                case android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_SUSPENDED :
                    return "CONNECT_STATE_SUSPENDED";
                default :
                    return "UNKNOWN/" + state;
            }
        }

        /**
         * Return true if {@code callback} is the current ServiceCallbacks. Also logs if it's not.
         */
        private boolean isCurrent(android.os.Messenger callback, java.lang.String funcName) {
            if (mCallbacksMessenger != callback) {
                if (mState != android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_DISCONNECTED) {
                    android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, (((((funcName + " for ") + mServiceComponent) + " with mCallbacksMessenger=") + mCallbacksMessenger) + " this=") + this);
                }
                return false;
            }
            return true;
        }

        /**
         * Log internal state.
         */
        void dump() {
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "MediaBrowserCompat...");
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mServiceComponent=" + mServiceComponent);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mCallback=" + mCallback);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mRootHints=" + mRootHints);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mState=" + android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(mState));
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mServiceConnection=" + mServiceConnection);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mServiceBinderWrapper=" + mServiceBinderWrapper);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mCallbacksMessenger=" + mCallbacksMessenger);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mRootId=" + mRootId);
            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "  mMediaSessionToken=" + mMediaSessionToken);
        }

        /**
         * ServiceConnection to the other app.
         */
        private class MediaServiceConnection implements android.content.ServiceConnection {
            MediaServiceConnection() {
            }

            @java.lang.Override
            public void onServiceConnected(final android.content.ComponentName name, final android.os.IBinder binder) {
                postOrRun(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, (("MediaServiceConnection.onServiceConnected name=" + name) + " binder=") + binder);
                            dump();
                        }
                        // Make sure we are still the current connection, and that they haven't
                        // called disconnect().
                        if (!isCurrent("onServiceConnected")) {
                            return;
                        }
                        // Save their binder
                        mServiceBinderWrapper = new android.support.v4.media.MediaBrowserCompat.ServiceBinderWrapper(binder, mRootHints);
                        // We make a new mServiceCallbacks each time we connect so that we can drop
                        // responses from previous connections.
                        mCallbacksMessenger = new android.os.Messenger(mHandler);
                        mHandler.setCallbacksMessenger(mCallbacksMessenger);
                        mState = android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_CONNECTING;
                        // Call connect, which is async. When we get a response from that we will
                        // say that we're connected.
                        try {
                            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
                                dump();
                            }
                            mServiceBinderWrapper.connect(mContext, mCallbacksMessenger);
                        } catch (android.os.RemoteException ex) {
                            // Connect failed, which isn't good. But the auto-reconnect on the
                            // service will take over and we will come back. We will also get the
                            // onServiceDisconnected, which has all the cleanup code. So let that
                            // do it.
                            android.util.Log.w(android.support.v4.media.MediaBrowserCompat.TAG, "RemoteException during connect for " + mServiceComponent);
                            if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                                android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "ServiceCallbacks.onConnect...");
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
                        if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                            android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, (((("MediaServiceConnection.onServiceDisconnected name=" + name) + " this=") + this) + " mServiceConnection=") + mServiceConnection);
                            dump();
                        }
                        // Make sure we are still the current connection, and that they haven't
                        // called disconnect().
                        if (!isCurrent("onServiceDisconnected")) {
                            return;
                        }
                        // Clear out what we set in onServiceConnected
                        mServiceBinderWrapper = null;
                        mCallbacksMessenger = null;
                        mHandler.setCallbacksMessenger(null);
                        // And tell the app that it's suspended.
                        mState = android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_SUSPENDED;
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
            boolean isCurrent(java.lang.String funcName) {
                if (mServiceConnection != this) {
                    if (mState != android.support.v4.media.MediaBrowserCompat.MediaBrowserImplBase.CONNECT_STATE_DISCONNECTED) {
                        // Check mState, because otherwise this log is noisy.
                        android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, (((((funcName + " for ") + mServiceComponent) + " with mServiceConnection=") + mServiceConnection) + " this=") + this);
                    }
                    return false;
                }
                return true;
            }
        }
    }

    static class MediaBrowserImplApi21 implements android.support.v4.media.MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal , android.support.v4.media.MediaBrowserCompat.MediaBrowserImpl , android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl {
        protected final java.lang.Object mBrowserObj;

        protected final android.os.Bundle mRootHints;

        protected final android.support.v4.media.MediaBrowserCompat.CallbackHandler mHandler = new android.support.v4.media.MediaBrowserCompat.CallbackHandler(this);

        private final android.support.v4.util.ArrayMap<java.lang.String, android.support.v4.media.MediaBrowserCompat.Subscription> mSubscriptions = new android.support.v4.util.ArrayMap<>();

        protected android.support.v4.media.MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;

        protected android.os.Messenger mCallbacksMessenger;

        public MediaBrowserImplApi21(android.content.Context context, android.content.ComponentName serviceComponent, android.support.v4.media.MediaBrowserCompat.ConnectionCallback callback, android.os.Bundle rootHints) {
            // Do not send the client version for API 25 and higher, since we don't need to use
            // EXTRA_MESSENGER_BINDER for API 24 and higher.
            if (android.os.Build.VERSION.SDK_INT < 25) {
                if (rootHints == null) {
                    rootHints = new android.os.Bundle();
                }
                rootHints.putInt(android.support.v4.media.MediaBrowserProtocol.EXTRA_CLIENT_VERSION, android.support.v4.media.MediaBrowserProtocol.CLIENT_VERSION_CURRENT);
                mRootHints = new android.os.Bundle(rootHints);
            } else {
                mRootHints = (rootHints == null) ? null : new android.os.Bundle(rootHints);
            }
            callback.setInternalConnectionCallback(this);
            mBrowserObj = android.support.v4.media.MediaBrowserCompatApi21.createBrowser(context, serviceComponent, callback.mConnectionCallbackObj, mRootHints);
        }

        @java.lang.Override
        public void connect() {
            android.support.v4.media.MediaBrowserCompatApi21.connect(mBrowserObj);
        }

        @java.lang.Override
        public void disconnect() {
            if ((mServiceBinderWrapper != null) && (mCallbacksMessenger != null)) {
                try {
                    mServiceBinderWrapper.unregisterCallbackMessenger(mCallbacksMessenger);
                } catch (android.os.RemoteException e) {
                    android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Remote error unregistering client messenger.");
                }
            }
            android.support.v4.media.MediaBrowserCompatApi21.disconnect(mBrowserObj);
        }

        @java.lang.Override
        public boolean isConnected() {
            return android.support.v4.media.MediaBrowserCompatApi21.isConnected(mBrowserObj);
        }

        @java.lang.Override
        public android.content.ComponentName getServiceComponent() {
            return android.support.v4.media.MediaBrowserCompatApi21.getServiceComponent(mBrowserObj);
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public java.lang.String getRoot() {
            return android.support.v4.media.MediaBrowserCompatApi21.getRoot(mBrowserObj);
        }

        @android.support.annotation.Nullable
        @java.lang.Override
        public android.os.Bundle getExtras() {
            return android.support.v4.media.MediaBrowserCompatApi21.getExtras(mBrowserObj);
        }

        @android.support.annotation.NonNull
        @java.lang.Override
        public android.support.v4.media.session.MediaSessionCompat.Token getSessionToken() {
            return android.support.v4.media.session.MediaSessionCompat.Token.fromToken(android.support.v4.media.MediaBrowserCompatApi21.getSessionToken(mBrowserObj));
        }

        @java.lang.Override
        public void subscribe(@android.support.annotation.NonNull
        final java.lang.String parentId, final android.os.Bundle options, @android.support.annotation.NonNull
        final android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            // Update or create the subscription.
            android.support.v4.media.MediaBrowserCompat.Subscription sub = mSubscriptions.get(parentId);
            if (sub == null) {
                sub = new android.support.v4.media.MediaBrowserCompat.Subscription();
                mSubscriptions.put(parentId, sub);
            }
            callback.setSubscription(sub);
            sub.putCallback(options, callback);
            if (mServiceBinderWrapper == null) {
                android.support.v4.media.MediaBrowserCompatApi21.subscribe(mBrowserObj, parentId, callback.mSubscriptionCallbackObj);
            } else {
                try {
                    mServiceBinderWrapper.addSubscription(parentId, callback.mToken, options, mCallbacksMessenger);
                } catch (android.os.RemoteException e) {
                    // Process is crashing. We will disconnect, and upon reconnect we will
                    // automatically reregister. So nothing to do here.
                    android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Remote error subscribing media item: " + parentId);
                }
            }
        }

        @java.lang.Override
        public void unsubscribe(@android.support.annotation.NonNull
        java.lang.String parentId, android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            android.support.v4.media.MediaBrowserCompat.Subscription sub = mSubscriptions.get(parentId);
            if (sub == null) {
                return;
            }
            if (mServiceBinderWrapper == null) {
                if (callback == null) {
                    android.support.v4.media.MediaBrowserCompatApi21.unsubscribe(mBrowserObj, parentId);
                } else {
                    final java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> callbacks = sub.getCallbacks();
                    final java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                    for (int i = callbacks.size() - 1; i >= 0; --i) {
                        if (callbacks.get(i) == callback) {
                            callbacks.remove(i);
                            optionsList.remove(i);
                        }
                    }
                    if (callbacks.size() == 0) {
                        android.support.v4.media.MediaBrowserCompatApi21.unsubscribe(mBrowserObj, parentId);
                    }
                }
            } else {
                // Tell the service if necessary.
                try {
                    if (callback == null) {
                        mServiceBinderWrapper.removeSubscription(parentId, null, mCallbacksMessenger);
                    } else {
                        final java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> callbacks = sub.getCallbacks();
                        final java.util.List<android.os.Bundle> optionsList = sub.getOptionsList();
                        for (int i = callbacks.size() - 1; i >= 0; --i) {
                            if (callbacks.get(i) == callback) {
                                mServiceBinderWrapper.removeSubscription(parentId, callback.mToken, mCallbacksMessenger);
                                callbacks.remove(i);
                                optionsList.remove(i);
                            }
                        }
                    }
                } catch (android.os.RemoteException ex) {
                    // Process is crashing. We will disconnect, and upon reconnect we will
                    // automatically reregister. So nothing to do here.
                    android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "removeSubscription failed with RemoteException parentId=" + parentId);
                }
            }
            if (sub.isEmpty() || (callback == null)) {
                mSubscriptions.remove(parentId);
            }
        }

        @java.lang.Override
        public void getItem(@android.support.annotation.NonNull
        final java.lang.String mediaId, @android.support.annotation.NonNull
        final android.support.v4.media.MediaBrowserCompat.ItemCallback cb) {
            if (android.text.TextUtils.isEmpty(mediaId)) {
                throw new java.lang.IllegalArgumentException("mediaId is empty");
            }
            if (cb == null) {
                throw new java.lang.IllegalArgumentException("cb is null");
            }
            if (!android.support.v4.media.MediaBrowserCompatApi21.isConnected(mBrowserObj)) {
                android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Not connected, unable to retrieve the MediaItem.");
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
                return;
            }
            if (mServiceBinderWrapper == null) {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        // Default framework implementation.
                        cb.onError(mediaId);
                    }
                });
                return;
            }
            android.support.v4.os.ResultReceiver receiver = new android.support.v4.media.MediaBrowserCompat.ItemReceiver(mediaId, cb, mHandler);
            try {
                mServiceBinderWrapper.getMediaItem(mediaId, receiver, mCallbacksMessenger);
            } catch (android.os.RemoteException e) {
                android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Remote error getting media item: " + mediaId);
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        cb.onError(mediaId);
                    }
                });
            }
        }

        @java.lang.Override
        public void onConnected() {
            android.os.Bundle extras = android.support.v4.media.MediaBrowserCompatApi21.getExtras(mBrowserObj);
            if (extras == null) {
                return;
            }
            android.os.IBinder serviceBinder = android.support.v4.app.BundleCompat.getBinder(extras, android.support.v4.media.MediaBrowserProtocol.EXTRA_MESSENGER_BINDER);
            if (serviceBinder != null) {
                mServiceBinderWrapper = new android.support.v4.media.MediaBrowserCompat.ServiceBinderWrapper(serviceBinder, mRootHints);
                mCallbacksMessenger = new android.os.Messenger(mHandler);
                mHandler.setCallbacksMessenger(mCallbacksMessenger);
                try {
                    mServiceBinderWrapper.registerCallbackMessenger(mCallbacksMessenger);
                } catch (android.os.RemoteException e) {
                    android.util.Log.i(android.support.v4.media.MediaBrowserCompat.TAG, "Remote error registering client messenger.");
                }
            }
        }

        @java.lang.Override
        public void onConnectionSuspended() {
            mServiceBinderWrapper = null;
            mCallbacksMessenger = null;
            mHandler.setCallbacksMessenger(null);
        }

        @java.lang.Override
        public void onConnectionFailed() {
            // Do noting
        }

        @java.lang.Override
        public void onServiceConnected(final android.os.Messenger callback, final java.lang.String root, final android.support.v4.media.session.MediaSessionCompat.Token session, final android.os.Bundle extra) {
            // This method will not be called.
        }

        @java.lang.Override
        public void onConnectionFailed(android.os.Messenger callback) {
            // This method will not be called.
        }

        @java.lang.Override
        public void onLoadChildren(android.os.Messenger callback, java.lang.String parentId, java.util.List list, android.os.Bundle options) {
            if (mCallbacksMessenger != callback) {
                return;
            }
            // Check that the subscription is still subscribed.
            android.support.v4.media.MediaBrowserCompat.Subscription subscription = mSubscriptions.get(parentId);
            if (subscription == null) {
                if (android.support.v4.media.MediaBrowserCompat.DEBUG) {
                    android.util.Log.d(android.support.v4.media.MediaBrowserCompat.TAG, "onLoadChildren for id that isn't subscribed id=" + parentId);
                }
                return;
            }
            // Tell the app.
            android.support.v4.media.MediaBrowserCompat.SubscriptionCallback subscriptionCallback = subscription.getCallback(options);
            if (subscriptionCallback != null) {
                if (options == null) {
                    subscriptionCallback.onChildrenLoaded(parentId, list);
                } else {
                    subscriptionCallback.onChildrenLoaded(parentId, list, options);
                }
            }
        }
    }

    static class MediaBrowserImplApi23 extends android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi21 {
        public MediaBrowserImplApi23(android.content.Context context, android.content.ComponentName serviceComponent, android.support.v4.media.MediaBrowserCompat.ConnectionCallback callback, android.os.Bundle rootHints) {
            super(context, serviceComponent, callback, rootHints);
        }

        @java.lang.Override
        public void getItem(@android.support.annotation.NonNull
        final java.lang.String mediaId, @android.support.annotation.NonNull
        final android.support.v4.media.MediaBrowserCompat.ItemCallback cb) {
            if (mServiceBinderWrapper == null) {
                android.support.v4.media.MediaBrowserCompatApi23.getItem(mBrowserObj, mediaId, cb.mItemCallbackObj);
            } else {
                super.getItem(mediaId, cb);
            }
        }
    }

    static class MediaBrowserImplApi24 extends android.support.v4.media.MediaBrowserCompat.MediaBrowserImplApi23 {
        public MediaBrowserImplApi24(android.content.Context context, android.content.ComponentName serviceComponent, android.support.v4.media.MediaBrowserCompat.ConnectionCallback callback, android.os.Bundle rootHints) {
            super(context, serviceComponent, callback, rootHints);
        }

        @java.lang.Override
        public void subscribe(@android.support.annotation.NonNull
        java.lang.String parentId, @android.support.annotation.NonNull
        android.os.Bundle options, @android.support.annotation.NonNull
        android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            if (options == null) {
                android.support.v4.media.MediaBrowserCompatApi21.subscribe(mBrowserObj, parentId, callback.mSubscriptionCallbackObj);
            } else {
                android.support.v4.media.MediaBrowserCompatApi24.subscribe(mBrowserObj, parentId, options, callback.mSubscriptionCallbackObj);
            }
        }

        @java.lang.Override
        public void unsubscribe(@android.support.annotation.NonNull
        java.lang.String parentId, android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            if (callback == null) {
                android.support.v4.media.MediaBrowserCompatApi21.unsubscribe(mBrowserObj, parentId);
            } else {
                android.support.v4.media.MediaBrowserCompatApi24.unsubscribe(mBrowserObj, parentId, callback.mSubscriptionCallbackObj);
            }
        }
    }

    private static class Subscription {
        private final java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> mCallbacks;

        private final java.util.List<android.os.Bundle> mOptionsList;

        public Subscription() {
            mCallbacks = new java.util.ArrayList();
            mOptionsList = new java.util.ArrayList();
        }

        public boolean isEmpty() {
            return mCallbacks.isEmpty();
        }

        public java.util.List<android.os.Bundle> getOptionsList() {
            return mOptionsList;
        }

        public java.util.List<android.support.v4.media.MediaBrowserCompat.SubscriptionCallback> getCallbacks() {
            return mCallbacks;
        }

        public android.support.v4.media.MediaBrowserCompat.SubscriptionCallback getCallback(android.os.Bundle options) {
            for (int i = 0; i < mOptionsList.size(); ++i) {
                if (android.support.v4.media.MediaBrowserCompatUtils.areSameOptions(mOptionsList.get(i), options)) {
                    return mCallbacks.get(i);
                }
            }
            return null;
        }

        public void putCallback(android.os.Bundle options, android.support.v4.media.MediaBrowserCompat.SubscriptionCallback callback) {
            for (int i = 0; i < mOptionsList.size(); ++i) {
                if (android.support.v4.media.MediaBrowserCompatUtils.areSameOptions(mOptionsList.get(i), options)) {
                    mCallbacks.set(i, callback);
                    return;
                }
            }
            mCallbacks.add(callback);
            mOptionsList.add(options);
        }
    }

    private static class CallbackHandler extends android.os.Handler {
        private final java.lang.ref.WeakReference<android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl> mCallbackImplRef;

        private java.lang.ref.WeakReference<android.os.Messenger> mCallbacksMessengerRef;

        CallbackHandler(android.support.v4.media.MediaBrowserCompat.MediaBrowserServiceCallbackImpl callbackImpl) {
            super();
            mCallbackImplRef = new java.lang.ref.WeakReference<>(callbackImpl);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (((mCallbacksMessengerRef == null) || (mCallbacksMessengerRef.get() == null)) || (mCallbackImplRef.get() == null)) {
                return;
            }
            android.os.Bundle data = msg.getData();
            data.setClassLoader(android.support.v4.media.session.MediaSessionCompat.class.getClassLoader());
            switch (msg.what) {
                case android.support.v4.media.MediaBrowserProtocol.SERVICE_MSG_ON_CONNECT :
                    mCallbackImplRef.get().onServiceConnected(mCallbacksMessengerRef.get(), data.getString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), ((android.support.v4.media.session.MediaSessionCompat.Token) (data.getParcelable(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_SESSION_TOKEN))), data.getBundle(android.support.v4.media.MediaBrowserProtocol.DATA_ROOT_HINTS));
                    break;
                case android.support.v4.media.MediaBrowserProtocol.SERVICE_MSG_ON_CONNECT_FAILED :
                    mCallbackImplRef.get().onConnectionFailed(mCallbacksMessengerRef.get());
                    break;
                case android.support.v4.media.MediaBrowserProtocol.SERVICE_MSG_ON_LOAD_CHILDREN :
                    mCallbackImplRef.get().onLoadChildren(mCallbacksMessengerRef.get(), data.getString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID), data.getParcelableArrayList(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_LIST), data.getBundle(android.support.v4.media.MediaBrowserProtocol.DATA_OPTIONS));
                    break;
                default :
                    android.util.Log.w(android.support.v4.media.MediaBrowserCompat.TAG, (((("Unhandled message: " + msg) + "\n  Client version: ") + android.support.v4.media.MediaBrowserProtocol.CLIENT_VERSION_CURRENT) + "\n  Service version: ") + msg.arg1);
            }
        }

        void setCallbacksMessenger(android.os.Messenger callbacksMessenger) {
            mCallbacksMessengerRef = new java.lang.ref.WeakReference<>(callbacksMessenger);
        }
    }

    private static class ServiceBinderWrapper {
        private android.os.Messenger mMessenger;

        private android.os.Bundle mRootHints;

        public ServiceBinderWrapper(android.os.IBinder target, android.os.Bundle rootHints) {
            mMessenger = new android.os.Messenger(target);
            mRootHints = rootHints;
        }

        void connect(android.content.Context context, android.os.Messenger callbacksMessenger) throws android.os.RemoteException {
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v4.media.MediaBrowserProtocol.DATA_PACKAGE_NAME, context.getPackageName());
            data.putBundle(android.support.v4.media.MediaBrowserProtocol.DATA_ROOT_HINTS, mRootHints);
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_CONNECT, data, callbacksMessenger);
        }

        void disconnect(android.os.Messenger callbacksMessenger) throws android.os.RemoteException {
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_DISCONNECT, null, callbacksMessenger);
        }

        void addSubscription(java.lang.String parentId, android.os.IBinder callbackToken, android.os.Bundle options, android.os.Messenger callbacksMessenger) throws android.os.RemoteException {
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, parentId);
            android.support.v4.app.BundleCompat.putBinder(data, android.support.v4.media.MediaBrowserProtocol.DATA_CALLBACK_TOKEN, callbackToken);
            data.putBundle(android.support.v4.media.MediaBrowserProtocol.DATA_OPTIONS, options);
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_ADD_SUBSCRIPTION, data, callbacksMessenger);
        }

        void removeSubscription(java.lang.String parentId, android.os.IBinder callbackToken, android.os.Messenger callbacksMessenger) throws android.os.RemoteException {
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, parentId);
            android.support.v4.app.BundleCompat.putBinder(data, android.support.v4.media.MediaBrowserProtocol.DATA_CALLBACK_TOKEN, callbackToken);
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_REMOVE_SUBSCRIPTION, data, callbacksMessenger);
        }

        void getMediaItem(java.lang.String mediaId, android.support.v4.os.ResultReceiver receiver, android.os.Messenger callbacksMessenger) throws android.os.RemoteException {
            android.os.Bundle data = new android.os.Bundle();
            data.putString(android.support.v4.media.MediaBrowserProtocol.DATA_MEDIA_ITEM_ID, mediaId);
            data.putParcelable(android.support.v4.media.MediaBrowserProtocol.DATA_RESULT_RECEIVER, receiver);
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_GET_MEDIA_ITEM, data, callbacksMessenger);
        }

        void registerCallbackMessenger(android.os.Messenger callbackMessenger) throws android.os.RemoteException {
            android.os.Bundle data = new android.os.Bundle();
            data.putBundle(android.support.v4.media.MediaBrowserProtocol.DATA_ROOT_HINTS, mRootHints);
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_REGISTER_CALLBACK_MESSENGER, data, callbackMessenger);
        }

        void unregisterCallbackMessenger(android.os.Messenger callbackMessenger) throws android.os.RemoteException {
            sendRequest(android.support.v4.media.MediaBrowserProtocol.CLIENT_MSG_UNREGISTER_CALLBACK_MESSENGER, null, callbackMessenger);
        }

        private void sendRequest(int what, android.os.Bundle data, android.os.Messenger cbMessenger) throws android.os.RemoteException {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = what;
            msg.arg1 = android.support.v4.media.MediaBrowserProtocol.CLIENT_VERSION_CURRENT;
            msg.setData(data);
            msg.replyTo = cbMessenger;
            mMessenger.send(msg);
        }
    }

    private static class ItemReceiver extends android.support.v4.os.ResultReceiver {
        private final java.lang.String mMediaId;

        private final android.support.v4.media.MediaBrowserCompat.ItemCallback mCallback;

        ItemReceiver(java.lang.String mediaId, android.support.v4.media.MediaBrowserCompat.ItemCallback callback, android.os.Handler handler) {
            super(handler);
            mMediaId = mediaId;
            mCallback = callback;
        }

        @java.lang.Override
        protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
            if (resultData != null) {
                resultData.setClassLoader(android.support.v4.media.MediaBrowserCompat.class.getClassLoader());
            }
            if (((resultCode != 0) || (resultData == null)) || (!resultData.containsKey(android.support.v4.media.MediaBrowserServiceCompat.KEY_MEDIA_ITEM))) {
                mCallback.onError(mMediaId);
                return;
            }
            android.os.Parcelable item = resultData.getParcelable(android.support.v4.media.MediaBrowserServiceCompat.KEY_MEDIA_ITEM);
            if ((item == null) || (item instanceof android.support.v4.media.MediaBrowserCompat.MediaItem)) {
                mCallback.onItemLoaded(((android.support.v4.media.MediaBrowserCompat.MediaItem) (item)));
            } else {
                mCallback.onError(mMediaId);
            }
        }
    }
}

