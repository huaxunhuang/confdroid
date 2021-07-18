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
package android.media;


/**
 * Information available from MediaRouterService about the state perceived by
 * a particular client and the routes that are available to it.
 *
 * Clients must not modify the contents of this object.
 *
 * @unknown 
 */
public final class MediaRouterClientState implements android.os.Parcelable {
    /**
     * A list of all known routes.
     */
    public final java.util.ArrayList<android.media.MediaRouterClientState.RouteInfo> routes;

    /**
     * The id of the current globally selected route, or null if none.
     * Globally selected routes override any other route selections that applications
     * may have made.  Used for remote displays.
     */
    public java.lang.String globallySelectedRouteId;

    public MediaRouterClientState() {
        routes = new java.util.ArrayList<android.media.MediaRouterClientState.RouteInfo>();
    }

    MediaRouterClientState(android.os.Parcel src) {
        routes = src.createTypedArrayList(android.media.MediaRouterClientState.RouteInfo.CREATOR);
        globallySelectedRouteId = src.readString();
    }

    public android.media.MediaRouterClientState.RouteInfo getRoute(java.lang.String id) {
        final int count = routes.size();
        for (int i = 0; i < count; i++) {
            final android.media.MediaRouterClientState.RouteInfo route = routes.get(i);
            if (route.id.equals(id)) {
                return route;
            }
        }
        return null;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeTypedList(routes);
        dest.writeString(globallySelectedRouteId);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("MediaRouterClientState{ globallySelectedRouteId=" + globallySelectedRouteId) + ", routes=") + routes.toString()) + " }";
    }

    public static final android.os.Parcelable.Creator<android.media.MediaRouterClientState> CREATOR = new android.os.Parcelable.Creator<android.media.MediaRouterClientState>() {
        @java.lang.Override
        public android.media.MediaRouterClientState createFromParcel(android.os.Parcel in) {
            return new android.media.MediaRouterClientState(in);
        }

        @java.lang.Override
        public android.media.MediaRouterClientState[] newArray(int size) {
            return new android.media.MediaRouterClientState[size];
        }
    };

    public static final class RouteInfo implements android.os.Parcelable {
        public java.lang.String id;

        public java.lang.String name;

        public java.lang.String description;

        public int supportedTypes;

        public boolean enabled;

        public int statusCode;

        public int playbackType;

        public int playbackStream;

        public int volume;

        public int volumeMax;

        public int volumeHandling;

        public int presentationDisplayId;

        @android.media.MediaRouter.RouteInfo.DeviceType
        public int deviceType;

        public RouteInfo(java.lang.String id) {
            this.id = id;
            enabled = true;
            statusCode = android.media.MediaRouter.RouteInfo.STATUS_NONE;
            playbackType = android.media.MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE;
            playbackStream = -1;
            volumeHandling = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;
            presentationDisplayId = -1;
            deviceType = android.media.MediaRouter.RouteInfo.DEVICE_TYPE_UNKNOWN;
        }

        public RouteInfo(android.media.MediaRouterClientState.RouteInfo other) {
            id = other.id;
            name = other.name;
            description = other.description;
            supportedTypes = other.supportedTypes;
            enabled = other.enabled;
            statusCode = other.statusCode;
            playbackType = other.playbackType;
            playbackStream = other.playbackStream;
            volume = other.volume;
            volumeMax = other.volumeMax;
            volumeHandling = other.volumeHandling;
            presentationDisplayId = other.presentationDisplayId;
            deviceType = other.deviceType;
        }

        RouteInfo(android.os.Parcel in) {
            id = in.readString();
            name = in.readString();
            description = in.readString();
            supportedTypes = in.readInt();
            enabled = in.readInt() != 0;
            statusCode = in.readInt();
            playbackType = in.readInt();
            playbackStream = in.readInt();
            volume = in.readInt();
            volumeMax = in.readInt();
            volumeHandling = in.readInt();
            presentationDisplayId = in.readInt();
            deviceType = in.readInt();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(description);
            dest.writeInt(supportedTypes);
            dest.writeInt(enabled ? 1 : 0);
            dest.writeInt(statusCode);
            dest.writeInt(playbackType);
            dest.writeInt(playbackStream);
            dest.writeInt(volume);
            dest.writeInt(volumeMax);
            dest.writeInt(volumeHandling);
            dest.writeInt(presentationDisplayId);
            dest.writeInt(deviceType);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((((((((((((((((("RouteInfo{ id=" + id) + ", name=") + name) + ", description=") + description) + ", supportedTypes=0x") + java.lang.Integer.toHexString(supportedTypes)) + ", enabled=") + enabled) + ", statusCode=") + statusCode) + ", playbackType=") + playbackType) + ", playbackStream=") + playbackStream) + ", volume=") + volume) + ", volumeMax=") + volumeMax) + ", volumeHandling=") + volumeHandling) + ", presentationDisplayId=") + presentationDisplayId) + ", deviceType=") + deviceType) + " }";
        }

        @java.lang.SuppressWarnings("hiding")
        public static final android.os.Parcelable.Creator<android.media.MediaRouterClientState.RouteInfo> CREATOR = new android.os.Parcelable.Creator<android.media.MediaRouterClientState.RouteInfo>() {
            @java.lang.Override
            public android.media.MediaRouterClientState.RouteInfo createFromParcel(android.os.Parcel in) {
                return new android.media.MediaRouterClientState.RouteInfo(in);
            }

            @java.lang.Override
            public android.media.MediaRouterClientState.RouteInfo[] newArray(int size) {
                return new android.media.MediaRouterClientState.RouteInfo[size];
            }
        };
    }
}

