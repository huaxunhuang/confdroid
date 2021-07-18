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
 * Point holds two integer coordinates
 */
public class Point implements android.os.Parcelable {
    public int x;

    public int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(@android.annotation.NonNull
    android.graphics.Point src) {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Negate the point's coordinates
     */
    public final void negate() {
        x = -x;
        y = -y;
    }

    /**
     * Offset the point's coordinates by dx, dy
     */
    public final void offset(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(int x, int y) {
        return (this.x == x) && (this.y == y);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.Point point = ((android.graphics.Point) (o));
        if (x != point.x)
            return false;

        if (y != point.y)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = x;
        result = (31 * result) + y;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("Point(" + x) + ", ") + y) + ")";
    }

    /**
     *
     *
     * @unknown 
     */
    public void printShortString(@android.annotation.NonNull
    java.io.PrintWriter pw) {
        pw.print("[");
        pw.print(x);
        pw.print(",");
        pw.print(y);
        pw.print("]");
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
        out.writeInt(x);
        out.writeInt(y);
    }

    /**
     * Write to a protocol buffer output stream.
     * Protocol buffer message definition at {@link android.graphics.PointProto}
     *
     * @param protoOutputStream
     * 		Stream to write the Rect object to.
     * @param fieldId
     * 		Field Id of the Rect as defined in the parent message
     * @unknown 
     */
    public void writeToProto(@android.annotation.NonNull
    android.util.proto.ProtoOutputStream protoOutputStream, long fieldId) {
        final long token = protoOutputStream.start(fieldId);
        protoOutputStream.write(PointProto.X, x);
        protoOutputStream.write(PointProto.Y, y);
        protoOutputStream.end(token);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.graphics.Point> CREATOR = new android.os.Parcelable.Creator<android.graphics.Point>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        @java.lang.Override
        public android.graphics.Point createFromParcel(android.os.Parcel in) {
            android.graphics.Point r = new android.graphics.Point();
            r.readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        @java.lang.Override
        public android.graphics.Point[] newArray(int size) {
            return new android.graphics.Point[size];
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
        x = in.readInt();
        y = in.readInt();
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public static android.graphics.Point convert(@android.annotation.NonNull
    android.util.Size size) {
        return new android.graphics.Point(size.getWidth(), size.getHeight());
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public static android.util.Size convert(@android.annotation.NonNull
    android.graphics.Point point) {
        return new android.util.Size(point.x, point.y);
    }
}

