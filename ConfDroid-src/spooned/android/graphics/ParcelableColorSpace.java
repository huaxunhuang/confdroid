/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.graphics;


/**
 * A {@link Parcelable} {@link ColorSpace}. In order to enable parceling, the ColorSpace
 * must be either a {@link ColorSpace.Named Named} ColorSpace or a {@link ColorSpace.Rgb} instance
 * that has an ICC parametric transfer function as returned by {@link Rgb#getTransferParameters()}.
 * TODO: Make public
 *
 * @unknown 
 */
public final class ParcelableColorSpace extends android.graphics.ColorSpace implements android.os.Parcelable {
    private final android.graphics.ColorSpace mColorSpace;

    /**
     * Checks if the given ColorSpace is able to be parceled. A ColorSpace can only be
     * parceled if it is a {@link ColorSpace.Named Named} ColorSpace or a {@link ColorSpace.Rgb}
     * instance that has an ICC parametric transfer function as returned by
     * {@link Rgb#getTransferParameters()}
     */
    public static boolean isParcelable(@android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        if (colorSpace.getId() == android.graphics.ColorSpace.MIN_ID) {
            if (!(colorSpace instanceof android.graphics.ColorSpace.Rgb)) {
                return false;
            }
            android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (colorSpace));
            if (rgb.getTransferParameters() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Constructs a new ParcelableColorSpace that wraps the provided ColorSpace.
     *
     * @param colorSpace
     * 		The ColorSpace to wrap. The ColorSpace must be either named or be an
     * 		RGB ColorSpace with an ICC parametric transfer function.
     * @throws IllegalArgumentException
     * 		If the provided ColorSpace does not satisfy the requirements
     * 		to be parceled. See {@link #isParcelable(ColorSpace)}.
     */
    public ParcelableColorSpace(@android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        super(colorSpace.getName(), colorSpace.getModel(), colorSpace.getId());
        mColorSpace = colorSpace;
        if (mColorSpace.getId() == android.graphics.ColorSpace.MIN_ID) {
            if (!(mColorSpace instanceof android.graphics.ColorSpace.Rgb)) {
                throw new java.lang.IllegalArgumentException("Unable to parcel unknown ColorSpaces that are not ColorSpace.Rgb");
            }
            android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (mColorSpace));
            if (rgb.getTransferParameters() == null) {
                throw new java.lang.IllegalArgumentException("ColorSpace must use an ICC " + "parametric transfer function to be parcelable");
            }
        }
    }

    @android.annotation.NonNull
    public android.graphics.ColorSpace getColorSpace() {
        return mColorSpace;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        final int id = mColorSpace.getId();
        dest.writeInt(id);
        if (id == android.graphics.ColorSpace.MIN_ID) {
            // Not a named color space. We have to actually write, like, stuff. And things. Ugh.
            // Cast is safe because this was asserted in the constructor
            android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (mColorSpace));
            dest.writeString(rgb.getName());
            dest.writeFloatArray(rgb.getPrimaries());
            dest.writeFloatArray(rgb.getWhitePoint());
            android.graphics.ColorSpace.Rgb.TransferParameters transferParameters = rgb.getTransferParameters();
            dest.writeDouble(transferParameters.a);
            dest.writeDouble(transferParameters.b);
            dest.writeDouble(transferParameters.c);
            dest.writeDouble(transferParameters.d);
            dest.writeDouble(transferParameters.e);
            dest.writeDouble(transferParameters.f);
            dest.writeDouble(transferParameters.g);
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.graphics.ParcelableColorSpace> CREATOR = new android.os.Parcelable.Creator<android.graphics.ParcelableColorSpace>() {
        @android.annotation.NonNull
        public android.graphics.ParcelableColorSpace createFromParcel(@android.annotation.NonNull
        android.os.Parcel in) {
            final int id = in.readInt();
            if (id == ColorSpace.MIN_ID) {
                java.lang.String name = in.readString();
                float[] primaries = in.createFloatArray();
                float[] whitePoint = in.createFloatArray();
                double a = in.readDouble();
                double b = in.readDouble();
                double c = in.readDouble();
                double d = in.readDouble();
                double e = in.readDouble();
                double f = in.readDouble();
                double g = in.readDouble();
                android.graphics.ColorSpace.Rgb.TransferParameters function = new android.graphics.ColorSpace.Rgb.TransferParameters(a, b, c, d, e, f, g);
                return new android.graphics.ParcelableColorSpace(new android.graphics.ColorSpace.Rgb(name, primaries, whitePoint, function));
            } else {
                return new android.graphics.ParcelableColorSpace(android.graphics.ColorSpace.get(id));
            }
        }

        public android.graphics.ParcelableColorSpace[] newArray(int size) {
            return new android.graphics.ParcelableColorSpace[size];
        }
    };

    @java.lang.Override
    public boolean isWideGamut() {
        return mColorSpace.isWideGamut();
    }

    @java.lang.Override
    public float getMinValue(int component) {
        return mColorSpace.getMinValue(component);
    }

    @java.lang.Override
    public float getMaxValue(int component) {
        return mColorSpace.getMaxValue(component);
    }

    @java.lang.Override
    @android.annotation.NonNull
    public float[] toXyz(@android.annotation.NonNull
    float[] v) {
        return mColorSpace.toXyz(v);
    }

    @java.lang.Override
    @android.annotation.NonNull
    public float[] fromXyz(@android.annotation.NonNull
    float[] v) {
        return mColorSpace.fromXyz(v);
    }

    @java.lang.Override
    public boolean equals(@android.annotation.Nullable
    java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.ParcelableColorSpace other = ((android.graphics.ParcelableColorSpace) (o));
        return mColorSpace.equals(other.mColorSpace);
    }

    @java.lang.Override
    public int hashCode() {
        return mColorSpace.hashCode();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    long getNativeInstance() {
        return mColorSpace.getNativeInstance();
    }
}

