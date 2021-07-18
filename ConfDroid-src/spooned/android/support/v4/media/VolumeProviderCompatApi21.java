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
package android.support.v4.media;


class VolumeProviderCompatApi21 {
    public static java.lang.Object createVolumeProvider(int volumeControl, int maxVolume, int currentVolume, final android.support.v4.media.VolumeProviderCompatApi21.Delegate delegate) {
        return new android.media.VolumeProvider(volumeControl, maxVolume, currentVolume) {
            @java.lang.Override
            public void onSetVolumeTo(int volume) {
                delegate.onSetVolumeTo(volume);
            }

            @java.lang.Override
            public void onAdjustVolume(int direction) {
                delegate.onAdjustVolume(direction);
            }
        };
    }

    public static void setCurrentVolume(java.lang.Object volumeProviderObj, int currentVolume) {
        ((android.media.VolumeProvider) (volumeProviderObj)).setCurrentVolume(currentVolume);
    }

    public interface Delegate {
        void onSetVolumeTo(int volume);

        void onAdjustVolume(int delta);
    }
}

