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
package android.hardware.input;


/**
 * Encapsulates calibration data for input devices.
 *
 * @unknown 
 */
public class TouchCalibration implements android.os.Parcelable {
    public static final android.hardware.input.TouchCalibration IDENTITY = new android.hardware.input.TouchCalibration();

    public static final android.os.Parcelable.Creator<android.hardware.input.TouchCalibration> CREATOR = new android.os.Parcelable.Creator<android.hardware.input.TouchCalibration>() {
        public android.hardware.input.TouchCalibration createFromParcel(android.os.Parcel in) {
            return new android.hardware.input.TouchCalibration(in);
        }

        public android.hardware.input.TouchCalibration[] newArray(int size) {
            return new android.hardware.input.TouchCalibration[size];
        }
    };

    private final float mXScale;

    private final float mXYMix;

    private final float mXOffset;

    private final float mYXMix;

    private final float mYScale;

    private final float mYOffset;

    /**
     * Create a new TouchCalibration initialized to the identity transformation.
     */
    public TouchCalibration() {
        this(1, 0, 0, 0, 1, 0);
    }

    /**
     * Create a new TouchCalibration from affine transformation paramters.
     *
     * @param xScale
     * 		Influence of input x-axis value on output x-axis value.
     * @param xyMix
     * 		Influence of input y-axis value on output x-axis value.
     * @param xOffset
     * 		Constant offset to be applied to output x-axis value.
     * @param yXMix
     * 		Influence of input x-axis value on output y-axis value.
     * @param yScale
     * 		Influence of input y-axis value on output y-axis value.
     * @param yOffset
     * 		Constant offset to be applied to output y-axis value.
     */
    public TouchCalibration(float xScale, float xyMix, float xOffset, float yxMix, float yScale, float yOffset) {
        mXScale = xScale;
        mXYMix = xyMix;
        mXOffset = xOffset;
        mYXMix = yxMix;
        mYScale = yScale;
        mYOffset = yOffset;
    }

    public TouchCalibration(android.os.Parcel in) {
        mXScale = in.readFloat();
        mXYMix = in.readFloat();
        mXOffset = in.readFloat();
        mYXMix = in.readFloat();
        mYScale = in.readFloat();
        mYOffset = in.readFloat();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeFloat(mXScale);
        dest.writeFloat(mXYMix);
        dest.writeFloat(mXOffset);
        dest.writeFloat(mYXMix);
        dest.writeFloat(mYScale);
        dest.writeFloat(mYOffset);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public float[] getAffineTransform() {
        return new float[]{ mXScale, mXYMix, mXOffset, mYXMix, mYScale, mYOffset };
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        } else
            if (obj instanceof android.hardware.input.TouchCalibration) {
                android.hardware.input.TouchCalibration cal = ((android.hardware.input.TouchCalibration) (obj));
                return (((((cal.mXScale == mXScale) && (cal.mXYMix == mXYMix)) && (cal.mXOffset == mXOffset)) && (cal.mYXMix == mYXMix)) && (cal.mYScale == mYScale)) && (cal.mYOffset == mYOffset);
            } else {
                return false;
            }

    }

    @java.lang.Override
    public int hashCode() {
        return ((((java.lang.Float.floatToIntBits(mXScale) ^ java.lang.Float.floatToIntBits(mXYMix)) ^ java.lang.Float.floatToIntBits(mXOffset)) ^ java.lang.Float.floatToIntBits(mYXMix)) ^ java.lang.Float.floatToIntBits(mYScale)) ^ java.lang.Float.floatToIntBits(mYOffset);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("[%f, %f, %f, %f, %f, %f]", mXScale, mXYMix, mXOffset, mYXMix, mYScale, mYOffset);
    }
}

