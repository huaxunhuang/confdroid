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
package android.media.tv;


/**
 * Simple container for information about TV input hardware.
 * Not for third-party developers.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class TvInputHardwareInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "TvInputHardwareInfo";

    // Match hardware/libhardware/include/hardware/tv_input.h
    public static final int TV_INPUT_TYPE_OTHER_HARDWARE = 1;

    public static final int TV_INPUT_TYPE_TUNER = 2;

    public static final int TV_INPUT_TYPE_COMPOSITE = 3;

    public static final int TV_INPUT_TYPE_SVIDEO = 4;

    public static final int TV_INPUT_TYPE_SCART = 5;

    public static final int TV_INPUT_TYPE_COMPONENT = 6;

    public static final int TV_INPUT_TYPE_VGA = 7;

    public static final int TV_INPUT_TYPE_DVI = 8;

    public static final int TV_INPUT_TYPE_HDMI = 9;

    public static final int TV_INPUT_TYPE_DISPLAY_PORT = 10;

    public static final android.os.Parcelable.Creator<android.media.tv.TvInputHardwareInfo> CREATOR = new android.os.Parcelable.Creator<android.media.tv.TvInputHardwareInfo>() {
        @java.lang.Override
        public android.media.tv.TvInputHardwareInfo createFromParcel(android.os.Parcel source) {
            try {
                android.media.tv.TvInputHardwareInfo info = new android.media.tv.TvInputHardwareInfo();
                info.readFromParcel(source);
                return info;
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.media.tv.TvInputHardwareInfo.TAG, "Exception creating TvInputHardwareInfo from parcel", e);
                return null;
            }
        }

        @java.lang.Override
        public android.media.tv.TvInputHardwareInfo[] newArray(int size) {
            return new android.media.tv.TvInputHardwareInfo[size];
        }
    };

    private int mDeviceId;

    private int mType;

    private int mAudioType;

    private java.lang.String mAudioAddress;

    private int mHdmiPortId;

    private TvInputHardwareInfo() {
    }

    public int getDeviceId() {
        return mDeviceId;
    }

    public int getType() {
        return mType;
    }

    public int getAudioType() {
        return mAudioType;
    }

    public java.lang.String getAudioAddress() {
        return mAudioAddress;
    }

    public int getHdmiPortId() {
        if (mType != android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI) {
            throw new java.lang.IllegalStateException();
        }
        return mHdmiPortId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder b = new java.lang.StringBuilder(128);
        b.append("TvInputHardwareInfo {id=").append(mDeviceId);
        b.append(", type=").append(mType);
        b.append(", audio_type=").append(mAudioType);
        b.append(", audio_addr=").append(mAudioAddress);
        if (mType == android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI) {
            b.append(", hdmi_port=").append(mHdmiPortId);
        }
        b.append("}");
        return b.toString();
    }

    // Parcelable
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mDeviceId);
        dest.writeInt(mType);
        dest.writeInt(mAudioType);
        dest.writeString(mAudioAddress);
        if (mType == android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI) {
            dest.writeInt(mHdmiPortId);
        }
    }

    public void readFromParcel(android.os.Parcel source) {
        mDeviceId = source.readInt();
        mType = source.readInt();
        mAudioType = source.readInt();
        mAudioAddress = source.readString();
        if (mType == android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI) {
            mHdmiPortId = source.readInt();
        }
    }

    public static final class Builder {
        private java.lang.Integer mDeviceId = null;

        private java.lang.Integer mType = null;

        private int mAudioType = android.media.AudioManager.DEVICE_NONE;

        private java.lang.String mAudioAddress = "";

        private java.lang.Integer mHdmiPortId = null;

        public Builder() {
        }

        public android.media.tv.TvInputHardwareInfo.Builder deviceId(int deviceId) {
            mDeviceId = deviceId;
            return this;
        }

        public android.media.tv.TvInputHardwareInfo.Builder type(int type) {
            mType = type;
            return this;
        }

        public android.media.tv.TvInputHardwareInfo.Builder audioType(int audioType) {
            mAudioType = audioType;
            return this;
        }

        public android.media.tv.TvInputHardwareInfo.Builder audioAddress(java.lang.String audioAddress) {
            mAudioAddress = audioAddress;
            return this;
        }

        public android.media.tv.TvInputHardwareInfo.Builder hdmiPortId(int hdmiPortId) {
            mHdmiPortId = hdmiPortId;
            return this;
        }

        public android.media.tv.TvInputHardwareInfo build() {
            if ((mDeviceId == null) || (mType == null)) {
                throw new java.lang.UnsupportedOperationException();
            }
            if (((mType == android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI) && (mHdmiPortId == null)) || ((mType != android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI) && (mHdmiPortId != null))) {
                throw new java.lang.UnsupportedOperationException();
            }
            android.media.tv.TvInputHardwareInfo info = new android.media.tv.TvInputHardwareInfo();
            info.mDeviceId = mDeviceId;
            info.mType = mType;
            info.mAudioType = mAudioType;
            if (info.mAudioType != android.media.AudioManager.DEVICE_NONE) {
                info.mAudioAddress = mAudioAddress;
            }
            if (mHdmiPortId != null) {
                info.mHdmiPortId = mHdmiPortId;
            }
            return info;
        }
    }
}

