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
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class TvStreamConfig implements android.os.Parcelable {
    static final java.lang.String TAG = android.media.tv.TvStreamConfig.class.getSimpleName();

    public static final int STREAM_TYPE_INDEPENDENT_VIDEO_SOURCE = 1;

    public static final int STREAM_TYPE_BUFFER_PRODUCER = 2;

    private int mStreamId;

    private int mType;

    private int mMaxWidth;

    private int mMaxHeight;

    /**
     * Generations are incremented once framework receives STREAM_CONFIGURATION_CHANGED event from
     * HAL module. Framework should throw away outdated configurations and get new configurations
     * via tv_input_device::get_stream_configurations().
     */
    private int mGeneration;

    public static final android.os.Parcelable.Creator<android.media.tv.TvStreamConfig> CREATOR = new android.os.Parcelable.Creator<android.media.tv.TvStreamConfig>() {
        @java.lang.Override
        public android.media.tv.TvStreamConfig createFromParcel(android.os.Parcel source) {
            try {
                return new android.media.tv.TvStreamConfig.Builder().streamId(source.readInt()).type(source.readInt()).maxWidth(source.readInt()).maxHeight(source.readInt()).generation(source.readInt()).build();
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.media.tv.TvStreamConfig.TAG, "Exception creating TvStreamConfig from parcel", e);
                return null;
            }
        }

        @java.lang.Override
        public android.media.tv.TvStreamConfig[] newArray(int size) {
            return new android.media.tv.TvStreamConfig[size];
        }
    };

    private TvStreamConfig() {
    }

    public int getStreamId() {
        return mStreamId;
    }

    public int getType() {
        return mType;
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    public int getGeneration() {
        return mGeneration;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("TvStreamConfig {mStreamId=" + mStreamId) + ";") + "mType=") + mType) + ";mGeneration=") + mGeneration) + "}";
    }

    // Parcelable
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mStreamId);
        dest.writeInt(mType);
        dest.writeInt(mMaxWidth);
        dest.writeInt(mMaxHeight);
        dest.writeInt(mGeneration);
    }

    /**
     * A helper class for creating a TvStreamConfig object.
     */
    public static final class Builder {
        private java.lang.Integer mStreamId;

        private java.lang.Integer mType;

        private java.lang.Integer mMaxWidth;

        private java.lang.Integer mMaxHeight;

        private java.lang.Integer mGeneration;

        public Builder() {
        }

        public android.media.tv.TvStreamConfig.Builder streamId(int streamId) {
            mStreamId = streamId;
            return this;
        }

        public android.media.tv.TvStreamConfig.Builder type(int type) {
            mType = type;
            return this;
        }

        public android.media.tv.TvStreamConfig.Builder maxWidth(int maxWidth) {
            mMaxWidth = maxWidth;
            return this;
        }

        public android.media.tv.TvStreamConfig.Builder maxHeight(int maxHeight) {
            mMaxHeight = maxHeight;
            return this;
        }

        public android.media.tv.TvStreamConfig.Builder generation(int generation) {
            mGeneration = generation;
            return this;
        }

        public android.media.tv.TvStreamConfig build() {
            if (((((mStreamId == null) || (mType == null)) || (mMaxWidth == null)) || (mMaxHeight == null)) || (mGeneration == null)) {
                throw new java.lang.UnsupportedOperationException();
            }
            android.media.tv.TvStreamConfig config = new android.media.tv.TvStreamConfig();
            config.mStreamId = mStreamId;
            config.mType = mType;
            config.mMaxWidth = mMaxWidth;
            config.mMaxHeight = mMaxHeight;
            config.mGeneration = mGeneration;
            return config;
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof android.media.tv.TvStreamConfig))
            return false;

        android.media.tv.TvStreamConfig config = ((android.media.tv.TvStreamConfig) (obj));
        return ((((config.mGeneration == mGeneration) && (config.mStreamId == mStreamId)) && (config.mType == mType)) && (config.mMaxWidth == mMaxWidth)) && (config.mMaxHeight == mMaxHeight);
    }
}

