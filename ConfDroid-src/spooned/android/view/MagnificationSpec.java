/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.view;


/**
 * This class represents spec for performing screen magnification.
 *
 * @unknown 
 */
public class MagnificationSpec implements android.os.Parcelable {
    private static final int MAX_POOL_SIZE = 20;

    private static final android.util.Pools.SynchronizedPool<android.view.MagnificationSpec> sPool = new android.util.Pools.SynchronizedPool(android.view.MagnificationSpec.MAX_POOL_SIZE);

    /**
     * The magnification scaling factor.
     */
    public float scale = 1.0F;

    /**
     * The X coordinate, in unscaled screen-relative pixels, around which
     * magnification is focused.
     */
    public float offsetX;

    /**
     * The Y coordinate, in unscaled screen-relative pixels, around which
     * magnification is focused.
     */
    public float offsetY;

    private MagnificationSpec() {
        /* do nothing - reducing visibility */
    }

    public void initialize(float scale, float offsetX, float offsetY) {
        if (scale < 1) {
            throw new java.lang.IllegalArgumentException("Scale must be greater than or equal to one!");
        }
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public boolean isNop() {
        return ((scale == 1.0F) && (offsetX == 0)) && (offsetY == 0);
    }

    public static android.view.MagnificationSpec obtain(android.view.MagnificationSpec other) {
        android.view.MagnificationSpec info = android.view.MagnificationSpec.obtain();
        info.scale = other.scale;
        info.offsetX = other.offsetX;
        info.offsetY = other.offsetY;
        return info;
    }

    public static android.view.MagnificationSpec obtain() {
        android.view.MagnificationSpec spec = android.view.MagnificationSpec.sPool.acquire();
        return spec != null ? spec : new android.view.MagnificationSpec();
    }

    public void recycle() {
        clear();
        android.view.MagnificationSpec.sPool.release(this);
    }

    public void clear() {
        scale = 1.0F;
        offsetX = 0.0F;
        offsetY = 0.0F;
    }

    public void setTo(android.view.MagnificationSpec other) {
        scale = other.scale;
        offsetX = other.offsetX;
        offsetY = other.offsetY;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeFloat(scale);
        parcel.writeFloat(offsetX);
        parcel.writeFloat(offsetY);
        recycle();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        }
        final android.view.MagnificationSpec s = ((android.view.MagnificationSpec) (other));
        return ((scale == s.scale) && (offsetX == s.offsetX)) && (offsetY == s.offsetY);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (scale != (+0.0F)) ? java.lang.Float.floatToIntBits(scale) : 0;
        result = (31 * result) + (offsetX != (+0.0F) ? java.lang.Float.floatToIntBits(offsetX) : 0);
        result = (31 * result) + (offsetY != (+0.0F) ? java.lang.Float.floatToIntBits(offsetY) : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("<scale:");
        builder.append(java.lang.Float.toString(scale));
        builder.append(",offsetX:");
        builder.append(java.lang.Float.toString(offsetX));
        builder.append(",offsetY:");
        builder.append(java.lang.Float.toString(offsetY));
        builder.append(">");
        return builder.toString();
    }

    private void initFromParcel(android.os.Parcel parcel) {
        scale = parcel.readFloat();
        offsetX = parcel.readFloat();
        offsetY = parcel.readFloat();
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.MagnificationSpec> CREATOR = new android.view.Creator<android.view.MagnificationSpec>() {
        @java.lang.Override
        public android.view.MagnificationSpec[] newArray(int size) {
            return new android.view.MagnificationSpec[size];
        }

        @java.lang.Override
        public android.view.MagnificationSpec createFromParcel(android.os.Parcel parcel) {
            android.view.MagnificationSpec spec = android.view.MagnificationSpec.obtain();
            spec.initFromParcel(parcel);
            return spec;
        }
    };
}

