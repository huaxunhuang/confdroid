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
package android.media;


/**
 * The AudioMixPort is a specialized type of AudioPort
 * describing an audio mix or stream at an input or output stream of the audio
 * framework.
 * In addition to base audio port attributes, the mix descriptor contains:
 * - the unique audio I/O handle assigned by AudioFlinger to this mix.
 *
 * @see AudioPort
 * @unknown 
 */
public class AudioMixPort extends android.media.AudioPort {
    private final int mIoHandle;

    AudioMixPort(android.media.AudioHandle handle, int ioHandle, int role, java.lang.String deviceName, int[] samplingRates, int[] channelMasks, int[] channelIndexMasks, int[] formats, android.media.AudioGain[] gains) {
        super(handle, role, deviceName, samplingRates, channelMasks, channelIndexMasks, formats, gains);
        mIoHandle = ioHandle;
    }

    /**
     * Build a specific configuration of this audio mix port for use by methods
     * like AudioManager.connectAudioPatch().
     */
    public android.media.AudioMixPortConfig buildConfig(int samplingRate, int channelMask, int format, android.media.AudioGainConfig gain) {
        return new android.media.AudioMixPortConfig(this, samplingRate, channelMask, format, gain);
    }

    /**
     * Get the device type (e.g AudioManager.DEVICE_OUT_SPEAKER)
     */
    public int ioHandle() {
        return mIoHandle;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if ((o == null) || (!(o instanceof android.media.AudioMixPort))) {
            return false;
        }
        android.media.AudioMixPort other = ((android.media.AudioMixPort) (o));
        if (mIoHandle != other.ioHandle()) {
            return false;
        }
        return super.equals(o);
    }
}

