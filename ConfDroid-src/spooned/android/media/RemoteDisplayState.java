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
 * Information available from IRemoteDisplayProvider about available remote displays.
 *
 * Clients must not modify the contents of this object.
 *
 * @unknown 
 */
public final class RemoteDisplayState implements android.os.Parcelable {
    // Note: These constants are used by the remote display provider API.
    // Do not change them!
    public static final java.lang.String SERVICE_INTERFACE = "com.android.media.remotedisplay.RemoteDisplayProvider";

    public static final int DISCOVERY_MODE_NONE = 0;

    public static final int DISCOVERY_MODE_PASSIVE = 1;

    public static final int DISCOVERY_MODE_ACTIVE = 2;

    /**
     * A list of all remote displays.
     */
    public final java.util.ArrayList<android.media.RemoteDisplayState.RemoteDisplayInfo> displays;

    public RemoteDisplayState() {
        displays = new java.util.ArrayList<android.media.RemoteDisplayState.RemoteDisplayInfo>();
    }

    RemoteDisplayState(android.os.Parcel src) {
        displays = src.createTypedArrayList(android.media.RemoteDisplayState.RemoteDisplayInfo.CREATOR);
    }

    public boolean isValid() {
        if (displays == null) {
            return false;
        }
        final int count = displays.size();
        for (int i = 0; i < count; i++) {
            if (!displays.get(i).isValid()) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeTypedList(displays);
    }

    public static final android.os.Parcelable.Creator<android.media.RemoteDisplayState> CREATOR = new android.os.Parcelable.Creator<android.media.RemoteDisplayState>() {
        @java.lang.Override
        public android.media.RemoteDisplayState createFromParcel(android.os.Parcel in) {
            return new android.media.RemoteDisplayState(in);
        }

        @java.lang.Override
        public android.media.RemoteDisplayState[] newArray(int size) {
            return new android.media.RemoteDisplayState[size];
        }
    };

    public static final class RemoteDisplayInfo implements android.os.Parcelable {
        // Note: These constants are used by the remote display provider API.
        // Do not change them!
        public static final int STATUS_NOT_AVAILABLE = 0;

        public static final int STATUS_IN_USE = 1;

        public static final int STATUS_AVAILABLE = 2;

        public static final int STATUS_CONNECTING = 3;

        public static final int STATUS_CONNECTED = 4;

        public static final int PLAYBACK_VOLUME_VARIABLE = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE;

        public static final int PLAYBACK_VOLUME_FIXED = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;

        public java.lang.String id;

        public java.lang.String name;

        public java.lang.String description;

        public int status;

        public int volume;

        public int volumeMax;

        public int volumeHandling;

        public int presentationDisplayId;

        public RemoteDisplayInfo(java.lang.String id) {
            this.id = id;
            status = android.media.RemoteDisplayState.RemoteDisplayInfo.STATUS_NOT_AVAILABLE;
            volumeHandling = android.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED;
            presentationDisplayId = -1;
        }

        public RemoteDisplayInfo(android.media.RemoteDisplayState.RemoteDisplayInfo other) {
            id = other.id;
            name = other.name;
            description = other.description;
            status = other.status;
            volume = other.volume;
            volumeMax = other.volumeMax;
            volumeHandling = other.volumeHandling;
            presentationDisplayId = other.presentationDisplayId;
        }

        RemoteDisplayInfo(android.os.Parcel in) {
            id = in.readString();
            name = in.readString();
            description = in.readString();
            status = in.readInt();
            volume = in.readInt();
            volumeMax = in.readInt();
            volumeHandling = in.readInt();
            presentationDisplayId = in.readInt();
        }

        public boolean isValid() {
            return (!android.text.TextUtils.isEmpty(id)) && (!android.text.TextUtils.isEmpty(name));
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
            dest.writeInt(status);
            dest.writeInt(volume);
            dest.writeInt(volumeMax);
            dest.writeInt(volumeHandling);
            dest.writeInt(presentationDisplayId);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((((((("RemoteDisplayInfo{ id=" + id) + ", name=") + name) + ", description=") + description) + ", status=") + status) + ", volume=") + volume) + ", volumeMax=") + volumeMax) + ", volumeHandling=") + volumeHandling) + ", presentationDisplayId=") + presentationDisplayId) + " }";
        }

        @java.lang.SuppressWarnings("hiding")
        public static final android.os.Parcelable.Creator<android.media.RemoteDisplayState.RemoteDisplayInfo> CREATOR = new android.os.Parcelable.Creator<android.media.RemoteDisplayState.RemoteDisplayInfo>() {
            @java.lang.Override
            public android.media.RemoteDisplayState.RemoteDisplayInfo createFromParcel(android.os.Parcel in) {
                return new android.media.RemoteDisplayState.RemoteDisplayInfo(in);
            }

            @java.lang.Override
            public android.media.RemoteDisplayState.RemoteDisplayInfo[] newArray(int size) {
                return new android.media.RemoteDisplayState.RemoteDisplayInfo[size];
            }
        };
    }
}

