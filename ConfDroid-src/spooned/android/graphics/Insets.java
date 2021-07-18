/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * An Insets instance holds four integer offsets which describe changes to the four
 * edges of a Rectangle. By convention, positive values move edges towards the
 * centre of the rectangle.
 * <p>
 * Insets are immutable so may be treated as values.
 */
public final class Insets implements android.os.Parcelable {
    @android.annotation.NonNull
    public static final android.graphics.Insets NONE = new android.graphics.Insets(0, 0, 0, 0);

    public final int left;

    public final int top;

    public final int right;

    public final int bottom;

    private Insets(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    // Factory methods
    /**
     * Return an Insets instance with the appropriate values.
     *
     * @param left
     * 		the left inset
     * @param top
     * 		the top inset
     * @param right
     * 		the right inset
     * @param bottom
     * 		the bottom inset
     * @return Insets instance with the appropriate values
     */
    @android.annotation.NonNull
    public static android.graphics.Insets of(int left, int top, int right, int bottom) {
        if ((((left == 0) && (top == 0)) && (right == 0)) && (bottom == 0)) {
            return android.graphics.Insets.NONE;
        }
        return new android.graphics.Insets(left, top, right, bottom);
    }

    /**
     * Return an Insets instance with the appropriate values.
     *
     * @param r
     * 		the rectangle from which to take the values
     * @return an Insets instance with the appropriate values
     */
    @android.annotation.NonNull
    public static android.graphics.Insets of(@android.annotation.Nullable
    android.graphics.Rect r) {
        return r == null ? android.graphics.Insets.NONE : android.graphics.Insets.of(r.left, r.top, r.right, r.bottom);
    }

    /**
     * Returns a Rect instance with the appropriate values.
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.graphics.Rect toRect() {
        return new android.graphics.Rect(left, top, right, bottom);
    }

    /**
     * Add two Insets.
     *
     * @param a
     * 		The first Insets to add.
     * @param b
     * 		The second Insets to add.
     * @return a + b, i. e. all insets on every side are added together.
     */
    @android.annotation.NonNull
    public static android.graphics.Insets add(@android.annotation.NonNull
    android.graphics.Insets a, @android.annotation.NonNull
    android.graphics.Insets b) {
        return android.graphics.Insets.of(a.left + b.left, a.top + b.top, a.right + b.right, a.bottom + b.bottom);
    }

    /**
     * Subtract two Insets.
     *
     * @param a
     * 		The minuend.
     * @param b
     * 		The subtrahend.
     * @return a - b, i. e. all insets on every side are subtracted from each other.
     */
    @android.annotation.NonNull
    public static android.graphics.Insets subtract(@android.annotation.NonNull
    android.graphics.Insets a, @android.annotation.NonNull
    android.graphics.Insets b) {
        return android.graphics.Insets.of(a.left - b.left, a.top - b.top, a.right - b.right, a.bottom - b.bottom);
    }

    /**
     * Retrieves the maximum of two Insets.
     *
     * @param a
     * 		The first Insets.
     * @param b
     * 		The second Insets.
     * @return max(a, b), i. e. the larger of every inset on every side is taken for the result.
     */
    @android.annotation.NonNull
    public static android.graphics.Insets max(@android.annotation.NonNull
    android.graphics.Insets a, @android.annotation.NonNull
    android.graphics.Insets b) {
        return android.graphics.Insets.of(java.lang.Math.max(a.left, b.left), java.lang.Math.max(a.top, b.top), java.lang.Math.max(a.right, b.right), java.lang.Math.max(a.bottom, b.bottom));
    }

    /**
     * Retrieves the minimum of two Insets.
     *
     * @param a
     * 		The first Insets.
     * @param b
     * 		The second Insets.
     * @return min(a, b), i. e. the smaller of every inset on every side is taken for the result.
     */
    @android.annotation.NonNull
    public static android.graphics.Insets min(@android.annotation.NonNull
    android.graphics.Insets a, @android.annotation.NonNull
    android.graphics.Insets b) {
        return android.graphics.Insets.of(java.lang.Math.min(a.left, b.left), java.lang.Math.min(a.top, b.top), java.lang.Math.min(a.right, b.right), java.lang.Math.min(a.bottom, b.bottom));
    }

    /**
     * Two Insets instances are equal iff they belong to the same class and their fields are
     * pairwise equal.
     *
     * @param o
     * 		the object to compare this instance with.
     * @return true iff this object is equal {@code o}
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.Insets insets = ((android.graphics.Insets) (o));
        if (bottom != insets.bottom)
            return false;

        if (left != insets.left)
            return false;

        if (right != insets.right)
            return false;

        if (top != insets.top)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = left;
        result = (31 * result) + top;
        result = (31 * result) + right;
        result = (31 * result) + bottom;
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((("Insets{" + "left=") + left) + ", top=") + top) + ", right=") + right) + ", bottom=") + bottom) + '}';
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(left);
        out.writeInt(top);
        out.writeInt(right);
        out.writeInt(bottom);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.graphics.Insets> CREATOR = new android.os.Parcelable.Creator<android.graphics.Insets>() {
        @java.lang.Override
        public android.graphics.Insets createFromParcel(android.os.Parcel in) {
            return new android.graphics.Insets(in.readInt(), in.readInt(), in.readInt(), in.readInt());
        }

        @java.lang.Override
        public android.graphics.Insets[] newArray(int size) {
            return new android.graphics.Insets[size];
        }
    };
}

