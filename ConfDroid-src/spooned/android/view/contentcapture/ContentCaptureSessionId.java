/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view.contentcapture;


/**
 * Identifier for a Content Capture session.
 */
public final class ContentCaptureSessionId implements android.os.Parcelable {
    @android.annotation.NonNull
    private final int mValue;

    /**
     * Creates a new instance.
     *
     * @param value
     * 		The internal value.
     * @unknown 
     */
    public ContentCaptureSessionId(@android.annotation.NonNull
    int value) {
        mValue = value;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getValue() {
        return mValue;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mValue;
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final android.view.contentcapture.ContentCaptureSessionId other = ((android.view.contentcapture.ContentCaptureSessionId) (obj));
        if (mValue != other.mValue)
            return false;

        return true;
    }

    /**
     * {@inheritDoc }
     *
     * <p><b>NOTE: </b>this method is only useful for debugging purposes and is not guaranteed to
     * be stable, hence it should not be used to identify the session.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.Integer.toString(mValue);
    }

    /**
     *
     *
     * @unknown 
     */
    // TODO(b/111276913): dump to proto as well
    public void dump(java.io.PrintWriter pw) {
        pw.print(mValue);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mValue);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.contentcapture.ContentCaptureSessionId> CREATOR = new android.os.Parcelable.Creator<android.view.contentcapture.ContentCaptureSessionId>() {
        @java.lang.Override
        @android.annotation.NonNull
        public android.view.contentcapture.ContentCaptureSessionId createFromParcel(android.os.Parcel parcel) {
            return new android.view.contentcapture.ContentCaptureSessionId(parcel.readInt());
        }

        @java.lang.Override
        @android.annotation.NonNull
        public android.view.contentcapture.ContentCaptureSessionId[] newArray(int size) {
            return new android.view.contentcapture.ContentCaptureSessionId[size];
        }
    };
}

