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
package android.hardware.camera2.params;


/**
 * Immutable class to store a time duration for any given format/size combination.
 *
 * @see CameraCharacteristics#SCALER_AVAILABLE_STREAM_CONFIGURATIONS
 * @see CameraCharacteristics#SCALER_AVAILABLE_MIN_FRAME_DURATIONS
 * @see CameraCharacteristics#SCALER_AVAILABLE_STALL_DURATIONS
 * @unknown 
 */
public final class StreamConfigurationDuration {
    /**
     * Create a new {@link StreamConfigurationDuration}.
     *
     * @param format
     * 		image format
     * @param width
     * 		image width, in pixels (positive)
     * @param height
     * 		image height, in pixels (positive)
     * @param durationNs
     * 		duration in nanoseconds (non-negative)
     * @throws IllegalArgumentException
     * 		if width/height were not positive, or durationNs was negative
     * 		or if the format was not user-defined in ImageFormat/PixelFormat
     * 		(IMPL_DEFINED is OK)
     * @unknown 
     */
    public StreamConfigurationDuration(final int format, final int width, final int height, final long durationNs) {
        mFormat = android.hardware.camera2.params.StreamConfigurationMap.checkArgumentFormatInternal(format);
        mWidth = checkArgumentPositive(width, "width must be positive");
        mHeight = checkArgumentPositive(height, "height must be positive");
        mDurationNs = checkArgumentNonnegative(durationNs, "durationNs must be non-negative");
    }

    /**
     * Get the internal image {@code format} in this stream configuration duration
     *
     * @return an integer format
     * @see ImageFormat
     * @see PixelFormat
     */
    public final int getFormat() {
        return mFormat;
    }

    /**
     * Return the width of the stream configuration duration.
     *
     * @return width > 0
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Return the height of the stream configuration duration
     *
     * @return height > 0
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Convenience method to return the size of this stream configuration duration.
     *
     * @return a Size with positive width and height
     */
    public android.util.Size getSize() {
        return new android.util.Size(mWidth, mHeight);
    }

    /**
     * Get the time duration (in nanoseconds).
     *
     * @return long >= 0
     */
    public long getDuration() {
        return mDurationNs;
    }

    /**
     * Check if this {@link StreamConfigurationDuration} is equal to another
     * {@link StreamConfigurationDuration}.
     *
     * <p>Two vectors are only equal if and only if each of the respective elements is equal.</p>
     *
     * @return {@code true} if the objects were equal, {@code false} otherwise
     */
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof android.hardware.camera2.params.StreamConfigurationDuration) {
            final android.hardware.camera2.params.StreamConfigurationDuration other = ((android.hardware.camera2.params.StreamConfigurationDuration) (obj));
            return (((mFormat == other.mFormat) && (mWidth == other.mWidth)) && (mHeight == other.mHeight)) && (mDurationNs == other.mDurationNs);
        }
        return false;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int hashCode() {
        return android.hardware.camera2.utils.HashCodeHelpers.hashCode(mFormat, mWidth, mHeight, ((int) (mDurationNs)), ((int) (mDurationNs >>> java.lang.Integer.SIZE)));
    }

    private final int mFormat;

    private final int mWidth;

    private final int mHeight;

    private final long mDurationNs;
}

