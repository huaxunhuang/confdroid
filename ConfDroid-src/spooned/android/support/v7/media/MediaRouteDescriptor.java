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
 * Describes the properties of a route.
 * <p>
 * Each route is uniquely identified by an opaque id string.  This token
 * may take any form as long as it is unique within the media route provider.
 * </p><p>
 * This object is immutable once created using a {@link Builder} instance.
 * </p>
 */
public final class MediaRouteDescriptor {
    static final java.lang.String KEY_ID = "id";

    static final java.lang.String KEY_GROUP_MEMBER_IDS = "groupMemberIds";

    static final java.lang.String KEY_NAME = "name";

    static final java.lang.String KEY_DESCRIPTION = "status";

    static final java.lang.String KEY_ICON_URI = "iconUri";

    static final java.lang.String KEY_ENABLED = "enabled";

    static final java.lang.String KEY_CONNECTING = "connecting";

    static final java.lang.String KEY_CONNECTION_STATE = "connectionState";

    static final java.lang.String KEY_CONTROL_FILTERS = "controlFilters";

    static final java.lang.String KEY_PLAYBACK_TYPE = "playbackType";

    static final java.lang.String KEY_PLAYBACK_STREAM = "playbackStream";

    static final java.lang.String KEY_DEVICE_TYPE = "deviceType";

    static final java.lang.String KEY_VOLUME = "volume";

    static final java.lang.String KEY_VOLUME_MAX = "volumeMax";

    static final java.lang.String KEY_VOLUME_HANDLING = "volumeHandling";

    static final java.lang.String KEY_PRESENTATION_DISPLAY_ID = "presentationDisplayId";

    static final java.lang.String KEY_EXTRAS = "extras";

    static final java.lang.String KEY_CAN_DISCONNECT = "canDisconnect";

    static final java.lang.String KEY_SETTINGS_INTENT = "settingsIntent";

    static final java.lang.String KEY_MIN_CLIENT_VERSION = "minClientVersion";

    static final java.lang.String KEY_MAX_CLIENT_VERSION = "maxClientVersion";

    final android.os.Bundle mBundle;

    java.util.List<android.content.IntentFilter> mControlFilters;

    MediaRouteDescriptor(android.os.Bundle bundle, java.util.List<android.content.IntentFilter> controlFilters) {
        mBundle = bundle;
        mControlFilters = controlFilters;
    }

    /**
     * Gets the unique id of the route.
     * <p>
     * The route id associated with a route descriptor functions as a stable
     * identifier for the route and must be unique among all routes offered
     * by the provider.
     * </p>
     */
    public java.lang.String getId() {
        return mBundle.getString(android.support.v7.media.MediaRouteDescriptor.KEY_ID);
    }

    /**
     * Gets the group member ids of the route.
     * <p>
     * A route descriptor that has one or more group member route ids
     * represents a route group. A member route may belong to another group.
     * </p>
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public java.util.List<java.lang.String> getGroupMemberIds() {
        return mBundle.getStringArrayList(android.support.v7.media.MediaRouteDescriptor.KEY_GROUP_MEMBER_IDS);
    }

    /**
     * Gets the user-visible name of the route.
     * <p>
     * The route name identifies the destination represented by the route.
     * It may be a user-supplied name, an alias, or device serial number.
     * </p>
     */
    public java.lang.String getName() {
        return mBundle.getString(android.support.v7.media.MediaRouteDescriptor.KEY_NAME);
    }

    /**
     * Gets the user-visible description of the route.
     * <p>
     * The route description describes the kind of destination represented by the route.
     * It may be a user-supplied string, a model number or brand of device.
     * </p>
     */
    public java.lang.String getDescription() {
        return mBundle.getString(android.support.v7.media.MediaRouteDescriptor.KEY_DESCRIPTION);
    }

    /**
     * Gets the URI of the icon representing this route.
     * <p>
     * This icon will be used in picker UIs if available.
     * </p>
     */
    public android.net.Uri getIconUri() {
        java.lang.String iconUri = mBundle.getString(android.support.v7.media.MediaRouteDescriptor.KEY_ICON_URI);
        return iconUri == null ? null : android.net.Uri.parse(iconUri);
    }

    /**
     * Gets whether the route is enabled.
     */
    public boolean isEnabled() {
        return mBundle.getBoolean(android.support.v7.media.MediaRouteDescriptor.KEY_ENABLED, true);
    }

    /**
     * Gets whether the route is connecting.
     *
     * @deprecated Use {@link #getConnectionState} instead
     */
    @java.lang.Deprecated
    public boolean isConnecting() {
        return mBundle.getBoolean(android.support.v7.media.MediaRouteDescriptor.KEY_CONNECTING, false);
    }

    /**
     * Gets the connection state of the route.
     *
     * @return The connection state of this route:
    {@link MediaRouter.RouteInfo#CONNECTION_STATE_DISCONNECTED},
    {@link MediaRouter.RouteInfo#CONNECTION_STATE_CONNECTING}, or
    {@link MediaRouter.RouteInfo#CONNECTION_STATE_CONNECTED}.
     */
    public int getConnectionState() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_CONNECTION_STATE, android.support.v7.media.MediaRouter.RouteInfo.CONNECTION_STATE_DISCONNECTED);
    }

    /**
     * Gets whether the route can be disconnected without stopping playback.
     * <p>
     * The route can normally be disconnected without stopping playback when
     * the destination device on the route is connected to two or more source
     * devices. The route provider should update the route immediately when the
     * number of connected devices changes.
     * </p><p>
     * To specify that the route should disconnect without stopping use
     * {@link MediaRouter#unselect(int)} with
     * {@link MediaRouter#UNSELECT_REASON_DISCONNECTED}.
     * </p>
     */
    public boolean canDisconnectAndKeepPlaying() {
        return mBundle.getBoolean(android.support.v7.media.MediaRouteDescriptor.KEY_CAN_DISCONNECT, false);
    }

    /**
     * Gets an {@link IntentSender} for starting a settings activity for this
     * route. The activity may have specific route settings or general settings
     * for the connected device or route provider.
     *
     * @return An {@link IntentSender} to start a settings activity.
     */
    public android.content.IntentSender getSettingsActivity() {
        return mBundle.getParcelable(android.support.v7.media.MediaRouteDescriptor.KEY_SETTINGS_INTENT);
    }

    /**
     * Gets the route's {@link MediaControlIntent media control intent} filters.
     */
    public java.util.List<android.content.IntentFilter> getControlFilters() {
        ensureControlFilters();
        return mControlFilters;
    }

    void ensureControlFilters() {
        if (mControlFilters == null) {
            mControlFilters = mBundle.<android.content.IntentFilter>getParcelableArrayList(android.support.v7.media.MediaRouteDescriptor.KEY_CONTROL_FILTERS);
            if (mControlFilters == null) {
                mControlFilters = java.util.Collections.<android.content.IntentFilter>emptyList();
            }
        }
    }

    /**
     * Gets the type of playback associated with this route.
     *
     * @return The type of playback associated with this route:
    {@link MediaRouter.RouteInfo#PLAYBACK_TYPE_LOCAL} or
    {@link MediaRouter.RouteInfo#PLAYBACK_TYPE_REMOTE}.
     */
    public int getPlaybackType() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_PLAYBACK_TYPE, android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE);
    }

    /**
     * Gets the route's playback stream.
     */
    public int getPlaybackStream() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_PLAYBACK_STREAM, -1);
    }

    /**
     * Gets the type of the receiver device associated with this route.
     *
     * @return The type of the receiver device associated with this route:
    {@link MediaRouter.RouteInfo#DEVICE_TYPE_TV} or
    {@link MediaRouter.RouteInfo#DEVICE_TYPE_SPEAKER}.
     */
    public int getDeviceType() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_DEVICE_TYPE);
    }

    /**
     * Gets the route's current volume, or 0 if unknown.
     */
    public int getVolume() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_VOLUME);
    }

    /**
     * Gets the route's maximum volume, or 0 if unknown.
     */
    public int getVolumeMax() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_VOLUME_MAX);
    }

    /**
     * Gets information about how volume is handled on the route.
     *
     * @return How volume is handled on the route:
    {@link MediaRouter.RouteInfo#PLAYBACK_VOLUME_FIXED} or
    {@link MediaRouter.RouteInfo#PLAYBACK_VOLUME_VARIABLE}.
     */
    public int getVolumeHandling() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_VOLUME_HANDLING, android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED);
    }

    /**
     * Gets the route's presentation display id, or -1 if none.
     */
    public int getPresentationDisplayId() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_PRESENTATION_DISPLAY_ID, android.support.v7.media.MediaRouter.RouteInfo.PRESENTATION_DISPLAY_ID_NONE);
    }

    /**
     * Gets a bundle of extras for this route descriptor.
     * The extras will be ignored by the media router but they may be used
     * by applications.
     */
    public android.os.Bundle getExtras() {
        return mBundle.getBundle(android.support.v7.media.MediaRouteDescriptor.KEY_EXTRAS);
    }

    /**
     * Gets the minimum client version required for this route.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public int getMinClientVersion() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_MIN_CLIENT_VERSION, android.support.v7.media.MediaRouteProviderProtocol.CLIENT_VERSION_START);
    }

    /**
     * Gets the maximum client version required for this route.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public int getMaxClientVersion() {
        return mBundle.getInt(android.support.v7.media.MediaRouteDescriptor.KEY_MAX_CLIENT_VERSION, java.lang.Integer.MAX_VALUE);
    }

    /**
     * Returns true if the route descriptor has all of the required fields.
     */
    public boolean isValid() {
        ensureControlFilters();
        if ((android.text.TextUtils.isEmpty(getId()) || android.text.TextUtils.isEmpty(getName())) || mControlFilters.contains(null)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        result.append("MediaRouteDescriptor{ ");
        result.append("id=").append(getId());
        result.append(", groupMemberIds=").append(getGroupMemberIds());
        result.append(", name=").append(getName());
        result.append(", description=").append(getDescription());
        result.append(", iconUri=").append(getIconUri());
        result.append(", isEnabled=").append(isEnabled());
        result.append(", isConnecting=").append(isConnecting());
        result.append(", connectionState=").append(getConnectionState());
        result.append(", controlFilters=").append(java.util.Arrays.toString(getControlFilters().toArray()));
        result.append(", playbackType=").append(getPlaybackType());
        result.append(", playbackStream=").append(getPlaybackStream());
        result.append(", deviceType=").append(getDeviceType());
        result.append(", volume=").append(getVolume());
        result.append(", volumeMax=").append(getVolumeMax());
        result.append(", volumeHandling=").append(getVolumeHandling());
        result.append(", presentationDisplayId=").append(getPresentationDisplayId());
        result.append(", extras=").append(getExtras());
        result.append(", isValid=").append(isValid());
        result.append(", minClientVersion=").append(getMinClientVersion());
        result.append(", maxClientVersion=").append(getMaxClientVersion());
        result.append(" }");
        return result.toString();
    }

    /**
     * Converts this object to a bundle for serialization.
     *
     * @return The contents of the object represented as a bundle.
     */
    public android.os.Bundle asBundle() {
        return mBundle;
    }

    /**
     * Creates an instance from a bundle.
     *
     * @param bundle
     * 		The bundle, or null if none.
     * @return The new instance, or null if the bundle was null.
     */
    public static android.support.v7.media.MediaRouteDescriptor fromBundle(android.os.Bundle bundle) {
        return bundle != null ? new android.support.v7.media.MediaRouteDescriptor(bundle, null) : null;
    }

    /**
     * Builder for {@link MediaRouteDescriptor media route descriptors}.
     */
    public static final class Builder {
        private final android.os.Bundle mBundle;

        private java.util.ArrayList<java.lang.String> mGroupMemberIds;

        private java.util.ArrayList<android.content.IntentFilter> mControlFilters;

        /**
         * Creates a media route descriptor builder.
         *
         * @param id
         * 		The unique id of the route.
         * @param name
         * 		The user-visible name of the route.
         */
        public Builder(java.lang.String id, java.lang.String name) {
            mBundle = new android.os.Bundle();
            setId(id);
            setName(name);
        }

        /**
         * Creates a media route descriptor builder whose initial contents are
         * copied from an existing descriptor.
         */
        public Builder(android.support.v7.media.MediaRouteDescriptor descriptor) {
            if (descriptor == null) {
                throw new java.lang.IllegalArgumentException("descriptor must not be null");
            }
            mBundle = new android.os.Bundle(descriptor.mBundle);
            descriptor.ensureControlFilters();
            if (!descriptor.mControlFilters.isEmpty()) {
                mControlFilters = new java.util.ArrayList<android.content.IntentFilter>(descriptor.mControlFilters);
            }
        }

        /**
         * Sets the unique id of the route.
         * <p>
         * The route id associated with a route descriptor functions as a stable
         * identifier for the route and must be unique among all routes offered
         * by the provider.
         * </p>
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setId(java.lang.String id) {
            mBundle.putString(android.support.v7.media.MediaRouteDescriptor.KEY_ID, id);
            return this;
        }

        /**
         * Adds a group member id of the route.
         * <p>
         * A route descriptor that has one or more group member route ids
         * represents a route group. A member route may belong to another group.
         * </p>
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v7.media.MediaRouteDescriptor.Builder addGroupMemberId(java.lang.String groupMemberId) {
            if (android.text.TextUtils.isEmpty(groupMemberId)) {
                throw new java.lang.IllegalArgumentException("groupMemberId must not be empty");
            }
            if (mGroupMemberIds == null) {
                mGroupMemberIds = new java.util.ArrayList<>();
            }
            if (!mGroupMemberIds.contains(groupMemberId)) {
                mGroupMemberIds.add(groupMemberId);
            }
            return this;
        }

        /**
         * Adds a list of group member ids of the route.
         * <p>
         * A route descriptor that has one or more group member route ids
         * represents a route group. A member route may belong to another group.
         * </p>
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v7.media.MediaRouteDescriptor.Builder addGroupMemberIds(java.util.Collection<java.lang.String> groupMemberIds) {
            if (groupMemberIds == null) {
                throw new java.lang.IllegalArgumentException("groupMemberIds must not be null");
            }
            if (!groupMemberIds.isEmpty()) {
                for (java.lang.String groupMemberId : groupMemberIds) {
                    addGroupMemberId(groupMemberId);
                }
            }
            return this;
        }

        /**
         * Sets the user-visible name of the route.
         * <p>
         * The route name identifies the destination represented by the route.
         * It may be a user-supplied name, an alias, or device serial number.
         * </p>
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setName(java.lang.String name) {
            mBundle.putString(android.support.v7.media.MediaRouteDescriptor.KEY_NAME, name);
            return this;
        }

        /**
         * Sets the user-visible description of the route.
         * <p>
         * The route description describes the kind of destination represented by the route.
         * It may be a user-supplied string, a model number or brand of device.
         * </p>
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setDescription(java.lang.String description) {
            mBundle.putString(android.support.v7.media.MediaRouteDescriptor.KEY_DESCRIPTION, description);
            return this;
        }

        /**
         * Sets the URI of the icon representing this route.
         * <p>
         * This icon will be used in picker UIs if available.
         * </p><p>
         * The URI must be one of the following formats:
         * <ul>
         * <li>content ({@link android.content.ContentResolver#SCHEME_CONTENT})</li>
         * <li>android.resource ({@link android.content.ContentResolver#SCHEME_ANDROID_RESOURCE})
         * </li>
         * <li>file ({@link android.content.ContentResolver#SCHEME_FILE})</li>
         * </ul>
         * </p>
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setIconUri(android.net.Uri iconUri) {
            if (iconUri == null) {
                throw new java.lang.IllegalArgumentException("iconUri must not be null");
            }
            mBundle.putString(android.support.v7.media.MediaRouteDescriptor.KEY_ICON_URI, iconUri.toString());
            return this;
        }

        /**
         * Sets whether the route is enabled.
         * <p>
         * Disabled routes represent routes that a route provider knows about, such as paired
         * Wifi Display receivers, but that are not currently available for use.
         * </p>
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setEnabled(boolean enabled) {
            mBundle.putBoolean(android.support.v7.media.MediaRouteDescriptor.KEY_ENABLED, enabled);
            return this;
        }

        /**
         * Sets whether the route is in the process of connecting and is not yet
         * ready for use.
         *
         * @deprecated Use {@link #setConnectionState} instead.
         */
        @java.lang.Deprecated
        public android.support.v7.media.MediaRouteDescriptor.Builder setConnecting(boolean connecting) {
            mBundle.putBoolean(android.support.v7.media.MediaRouteDescriptor.KEY_CONNECTING, connecting);
            return this;
        }

        /**
         * Sets the route's connection state.
         *
         * @param connectionState
         * 		The connection state of the route:
         * 		{@link MediaRouter.RouteInfo#CONNECTION_STATE_DISCONNECTED},
         * 		{@link MediaRouter.RouteInfo#CONNECTION_STATE_CONNECTING}, or
         * 		{@link MediaRouter.RouteInfo#CONNECTION_STATE_CONNECTED}.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setConnectionState(int connectionState) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_CONNECTION_STATE, connectionState);
            return this;
        }

        /**
         * Sets whether the route can be disconnected without stopping playback.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setCanDisconnect(boolean canDisconnect) {
            mBundle.putBoolean(android.support.v7.media.MediaRouteDescriptor.KEY_CAN_DISCONNECT, canDisconnect);
            return this;
        }

        /**
         * Sets an intent sender for launching the settings activity for this
         * route.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setSettingsActivity(android.content.IntentSender is) {
            mBundle.putParcelable(android.support.v7.media.MediaRouteDescriptor.KEY_SETTINGS_INTENT, is);
            return this;
        }

        /**
         * Adds a {@link MediaControlIntent media control intent} filter for the route.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder addControlFilter(android.content.IntentFilter filter) {
            if (filter == null) {
                throw new java.lang.IllegalArgumentException("filter must not be null");
            }
            if (mControlFilters == null) {
                mControlFilters = new java.util.ArrayList<android.content.IntentFilter>();
            }
            if (!mControlFilters.contains(filter)) {
                mControlFilters.add(filter);
            }
            return this;
        }

        /**
         * Adds a list of {@link MediaControlIntent media control intent} filters for the route.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder addControlFilters(java.util.Collection<android.content.IntentFilter> filters) {
            if (filters == null) {
                throw new java.lang.IllegalArgumentException("filters must not be null");
            }
            if (!filters.isEmpty()) {
                for (android.content.IntentFilter filter : filters) {
                    addControlFilter(filter);
                }
            }
            return this;
        }

        /**
         * Sets the route's playback type.
         *
         * @param playbackType
         * 		The playback type of the route:
         * 		{@link MediaRouter.RouteInfo#PLAYBACK_TYPE_LOCAL} or
         * 		{@link MediaRouter.RouteInfo#PLAYBACK_TYPE_REMOTE}.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setPlaybackType(int playbackType) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_PLAYBACK_TYPE, playbackType);
            return this;
        }

        /**
         * Sets the route's playback stream.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setPlaybackStream(int playbackStream) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_PLAYBACK_STREAM, playbackStream);
            return this;
        }

        /**
         * Sets the route's receiver device type.
         *
         * @param deviceType
         * 		The receive device type of the route:
         * 		{@link MediaRouter.RouteInfo#DEVICE_TYPE_TV} or
         * 		{@link MediaRouter.RouteInfo#DEVICE_TYPE_SPEAKER}.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setDeviceType(int deviceType) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_DEVICE_TYPE, deviceType);
            return this;
        }

        /**
         * Sets the route's current volume, or 0 if unknown.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setVolume(int volume) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_VOLUME, volume);
            return this;
        }

        /**
         * Sets the route's maximum volume, or 0 if unknown.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setVolumeMax(int volumeMax) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_VOLUME_MAX, volumeMax);
            return this;
        }

        /**
         * Sets the route's volume handling.
         *
         * @param volumeHandling
         * 		how volume is handled on the route:
         * 		{@link MediaRouter.RouteInfo#PLAYBACK_VOLUME_FIXED} or
         * 		{@link MediaRouter.RouteInfo#PLAYBACK_VOLUME_VARIABLE}.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setVolumeHandling(int volumeHandling) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_VOLUME_HANDLING, volumeHandling);
            return this;
        }

        /**
         * Sets the route's presentation display id, or -1 if none.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setPresentationDisplayId(int presentationDisplayId) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_PRESENTATION_DISPLAY_ID, presentationDisplayId);
            return this;
        }

        /**
         * Sets a bundle of extras for this route descriptor.
         * The extras will be ignored by the media router but they may be used
         * by applications.
         */
        public android.support.v7.media.MediaRouteDescriptor.Builder setExtras(android.os.Bundle extras) {
            mBundle.putBundle(android.support.v7.media.MediaRouteDescriptor.KEY_EXTRAS, extras);
            return this;
        }

        /**
         * Sets the route's minimum client version.
         * A router whose version is lower than this will not be able to connect to this route.
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v7.media.MediaRouteDescriptor.Builder setMinClientVersion(int minVersion) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_MIN_CLIENT_VERSION, minVersion);
            return this;
        }

        /**
         * Sets the route's maximum client version.
         * A router whose version is higher than this will not be able to connect to this route.
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v7.media.MediaRouteDescriptor.Builder setMaxClientVersion(int maxVersion) {
            mBundle.putInt(android.support.v7.media.MediaRouteDescriptor.KEY_MAX_CLIENT_VERSION, maxVersion);
            return this;
        }

        /**
         * Builds the {@link MediaRouteDescriptor media route descriptor}.
         */
        public android.support.v7.media.MediaRouteDescriptor build() {
            if (mControlFilters != null) {
                mBundle.putParcelableArrayList(android.support.v7.media.MediaRouteDescriptor.KEY_CONTROL_FILTERS, mControlFilters);
            }
            if (mGroupMemberIds != null) {
                mBundle.putStringArrayList(android.support.v7.media.MediaRouteDescriptor.KEY_GROUP_MEMBER_IDS, mGroupMemberIds);
            }
            return new android.support.v7.media.MediaRouteDescriptor(mBundle, mControlFilters);
        }
    }
}

