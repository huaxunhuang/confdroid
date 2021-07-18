/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.print;


/**
 * Represents a range of pages. The start and end page indices of
 * the range are zero based and inclusive.
 */
public final class PageRange implements android.os.Parcelable {
    /**
     * Constant for specifying all pages.
     */
    public static final android.print.PageRange ALL_PAGES = new android.print.PageRange(0, java.lang.Integer.MAX_VALUE);

    /**
     *
     *
     * @unknown 
     */
    public static final android.print.PageRange[] ALL_PAGES_ARRAY = new android.print.PageRange[]{ android.print.PageRange.ALL_PAGES };

    private final int mStart;

    private final int mEnd;

    /**
     * Creates a new instance.
     *
     * @param start
     * 		The start page index (zero based and inclusive).
     * @param end
     * 		The end page index (zero based and inclusive).
     * @throws IllegalArgumentException
     * 		If start is less than zero or end
     * 		is less than zero or start greater than end.
     */
    public PageRange(@android.annotation.IntRange(from = 0)
    int start, @android.annotation.IntRange(from = 0)
    int end) {
        if (start < 0) {
            throw new java.lang.IllegalArgumentException("start cannot be less than zero.");
        }
        if (end < 0) {
            throw new java.lang.IllegalArgumentException("end cannot be less than zero.");
        }
        if (start > end) {
            throw new java.lang.IllegalArgumentException("start must be lesser than end.");
        }
        mStart = start;
        mEnd = end;
    }

    private PageRange(@android.annotation.NonNull
    android.os.Parcel parcel) {
        this(parcel.readInt(), parcel.readInt());
    }

    /**
     * Gets the start page index (zero based and inclusive).
     *
     * @return The start page index.
     */
    @android.annotation.IntRange(from = 0)
    public int getStart() {
        return mStart;
    }

    /**
     * Gets the end page index (zero based and inclusive).
     *
     * @return The end page index.
     */
    @android.annotation.IntRange(from = 0)
    public int getEnd() {
        return mEnd;
    }

    /**
     * Gets whether a page range contains a a given page.
     *
     * @param pageIndex
     * 		The page index.
     * @return True if the page is within this range.
     * @unknown 
     */
    public boolean contains(int pageIndex) {
        return (pageIndex >= mStart) && (pageIndex <= mEnd);
    }

    /**
     * Get the size of this range which is the number of
     * pages it contains.
     *
     * @return The size of the range.
     * @unknown 
     */
    public int getSize() {
        return (mEnd - mStart) + 1;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mStart);
        parcel.writeInt(mEnd);
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mEnd;
        result = (prime * result) + mStart;
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.print.PageRange other = ((android.print.PageRange) (obj));
        if (mEnd != other.mEnd) {
            return false;
        }
        if (mStart != other.mStart) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        if ((mStart == 0) && (mEnd == java.lang.Integer.MAX_VALUE)) {
            return "PageRange[<all pages>]";
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PageRange[").append(mStart).append(" - ").append(mEnd).append("]");
        return builder.toString();
    }

    public static final android.os.Parcelable.Creator<android.print.PageRange> CREATOR = new android.os.Parcelable.Creator<android.print.PageRange>() {
        @java.lang.Override
        public android.print.PageRange createFromParcel(android.os.Parcel parcel) {
            return new android.print.PageRange(parcel);
        }

        @java.lang.Override
        public android.print.PageRange[] newArray(int size) {
            return new android.print.PageRange[size];
        }
    };
}

