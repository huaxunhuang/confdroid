/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * PointF holds two float coordinates
 */
public class PointF implements android.os.Parcelable {
    public float x;

    public float y;

    public PointF() {
    }

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF(@android.annotation.NonNull
    android.graphics.Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public final void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the point's x and y coordinates to the coordinates of p
     */
    public final void set(@android.annotation.NonNull
    android.graphics.PointF p) {
        this.x = p.x;
        this.y = p.y;
    }

    public final void negate() {
        x = -x;
        y = -y;
    }

    public final void offset(float dx, float dy) {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(float x, float y) {
        return (this.x == x) && (this.y == y);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.PointF pointF = ((android.graphics.PointF) (o));
        if (java.lang.Float.compare(pointF.x, x) != 0)
            return false;

        if (java.lang.Float.compare(pointF.y, y) != 0)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (x != (+0.0F)) ? java.lang.Float.floatToIntBits(x) : 0;
        result = (31 * result) + (y != (+0.0F) ? java.lang.Float.floatToIntBits(y) : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("PointF(" + x) + ", ") + y) + ")";
    }

    /**
     * Return the euclidian distance from (0,0) to the point
     */
    public final float length() {
        return android.graphics.PointF.length(x, y);
    }

    /**
     * Returns the euclidian distance from (0,0) to (x,y)
     */
    public static float length(float x, float y) {
        return ((float) (java.lang.Math.hypot(x, y)));
    }

    /**
     * Parcelable interface methods
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this point to the specified parcel. To restore a point from
     * a parcel, use readFromParcel()
     *
     * @param out
     * 		The parcel to write the point's coordinates into
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeFloat(x);
        out.writeFloat(y);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.graphics.PointF> CREATOR = new android.os.Parcelable.Creator<android.graphics.PointF>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        @java.lang.Override
        public android.graphics.PointF createFromParcel(android.os.Parcel in) {
            android.graphics.PointF r = new android.graphics.PointF();
            r.readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        @java.lang.Override
        public android.graphics.PointF[] newArray(int size) {
            return new android.graphics.PointF[size];
        }
    };

    /**
     * Set the point's coordinates from the data stored in the specified
     * parcel. To write a point to a parcel, call writeToParcel().
     *
     * @param in
     * 		The parcel to read the point's coordinates from
     */
    public void readFromParcel(@android.annotation.NonNull
    android.os.Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }
}

