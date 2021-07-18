/**
 * Copyright 2014, The Android Open Source Project
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
package android.media.session;


/**
 * Convenience class for passing information about the audio configuration of a
 * session. The public implementation is {@link MediaController.PlaybackInfo}.
 *
 * @unknown 
 */
public class ParcelableVolumeInfo implements android.os.Parcelable {
    public int volumeType;

    public android.media.AudioAttributes audioAttrs;

    public int controlType;

    public int maxVolume;

    public int currentVolume;

    public ParcelableVolumeInfo(int volumeType, android.media.AudioAttributes audioAttrs, int controlType, int maxVolume, int currentVolume) {
        this.volumeType = volumeType;
        this.audioAttrs = audioAttrs;
        this.controlType = controlType;
        this.maxVolume = maxVolume;
        this.currentVolume = currentVolume;
    }

    public ParcelableVolumeInfo(android.os.Parcel from) {
        volumeType = from.readInt();
        controlType = from.readInt();
        maxVolume = from.readInt();
        currentVolume = from.readInt();
        audioAttrs = android.media.AudioAttributes.CREATOR.createFromParcel(from);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(volumeType);
        dest.writeInt(controlType);
        dest.writeInt(maxVolume);
        dest.writeInt(currentVolume);
        audioAttrs.writeToParcel(dest, flags);
    }

    public static final android.os.Parcelable.Creator<android.media.session.ParcelableVolumeInfo> CREATOR = new android.os.Parcelable.Creator<android.media.session.ParcelableVolumeInfo>() {
        @java.lang.Override
        public android.media.session.ParcelableVolumeInfo createFromParcel(android.os.Parcel in) {
            return new android.media.session.ParcelableVolumeInfo(in);
        }

        @java.lang.Override
        public android.media.session.ParcelableVolumeInfo[] newArray(int size) {
            return new android.media.session.ParcelableVolumeInfo[size];
        }
    };
}

