/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.bluetooth;


/**
 * Represents the audio configuration for a Bluetooth A2DP source device.
 *
 * {@see BluetoothA2dpSink}
 *
 * {@hide }
 */
public final class BluetoothAudioConfig implements android.os.Parcelable {
    private final int mSampleRate;

    private final int mChannelConfig;

    private final int mAudioFormat;

    public BluetoothAudioConfig(int sampleRate, int channelConfig, int audioFormat) {
        mSampleRate = sampleRate;
        mChannelConfig = channelConfig;
        mAudioFormat = audioFormat;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.bluetooth.BluetoothAudioConfig) {
            android.bluetooth.BluetoothAudioConfig bac = ((android.bluetooth.BluetoothAudioConfig) (o));
            return ((bac.mSampleRate == mSampleRate) && (bac.mChannelConfig == mChannelConfig)) && (bac.mAudioFormat == mAudioFormat);
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return (mSampleRate | (mChannelConfig << 24)) | (mAudioFormat << 28);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("{mSampleRate:" + mSampleRate) + ",mChannelConfig:") + mChannelConfig) + ",mAudioFormat:") + mAudioFormat) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothAudioConfig> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothAudioConfig>() {
        public android.bluetooth.BluetoothAudioConfig createFromParcel(android.os.Parcel in) {
            int sampleRate = in.readInt();
            int channelConfig = in.readInt();
            int audioFormat = in.readInt();
            return new android.bluetooth.BluetoothAudioConfig(sampleRate, channelConfig, audioFormat);
        }

        public android.bluetooth.BluetoothAudioConfig[] newArray(int size) {
            return new android.bluetooth.BluetoothAudioConfig[size];
        }
    };

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mSampleRate);
        out.writeInt(mChannelConfig);
        out.writeInt(mAudioFormat);
    }

    /**
     * Returns the sample rate in samples per second
     *
     * @return sample rate
     */
    public int getSampleRate() {
        return mSampleRate;
    }

    /**
     * Returns the channel configuration (either {@link android.media.AudioFormat#CHANNEL_IN_MONO}
     * or {@link android.media.AudioFormat#CHANNEL_IN_STEREO})
     *
     * @return channel configuration
     */
    public int getChannelConfig() {
        return mChannelConfig;
    }

    /**
     * Returns the channel audio format (either {@link android.media.AudioFormat#ENCODING_PCM_16BIT}
     * or {@link android.media.AudioFormat#ENCODING_PCM_8BIT}
     *
     * @return audio format
     */
    public int getAudioFormat() {
        return mAudioFormat;
    }
}

