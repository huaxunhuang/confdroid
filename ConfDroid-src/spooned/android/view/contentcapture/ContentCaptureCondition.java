/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * Defines a condition for when content capture should be allowed.
 *
 * <p>See {@link ContentCaptureManager#getContentCaptureConditions()} for more.
 */
public final class ContentCaptureCondition implements android.os.Parcelable {
    /**
     * When set, package should use the {@link LocusId#getId()} as a regular expression (using the
     * {@link java.util.regex.Pattern} format).
     */
    public static final int FLAG_IS_REGEX = 0x2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "FLAG" }, flag = true, value = { android.view.contentcapture.ContentCaptureCondition.FLAG_IS_REGEX })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface Flags {}

    @android.annotation.NonNull
    private final android.content.LocusId mLocusId;

    @android.view.contentcapture.ContentCaptureCondition.Flags
    private final int mFlags;

    /**
     * Default constructor.
     *
     * @param locusId
     * 		id of the condition, as defined by
     * 		{@link ContentCaptureContext#getLocusId()}.
     * @param flags
     * 		either {@link ContentCaptureCondition#FLAG_IS_REGEX} (to use a regular
     * 		expression match) or {@code 0} (in which case the {@code LocusId} must be an exact match of
     * 		the {@code LocusId} used in the {@link ContentCaptureContext}).
     */
    public ContentCaptureCondition(@android.annotation.NonNull
    android.content.LocusId locusId, @android.view.contentcapture.ContentCaptureCondition.Flags
    int flags) {
        this.mLocusId = com.android.internal.util.Preconditions.checkNotNull(locusId);
        this.mFlags = flags;
    }

    /**
     * Gets the {@code LocusId} per se.
     */
    @android.annotation.NonNull
    public android.content.LocusId getLocusId() {
        return mLocusId;
    }

    /**
     * Gets the flags associates with this condition.
     *
     * @return either {@link ContentCaptureCondition#FLAG_IS_REGEX} or {@code 0}.
     */
    @android.view.contentcapture.ContentCaptureCondition.Flags
    public int getFlags() {
        return mFlags;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mFlags;
        result = (prime * result) + (mLocusId == null ? 0 : mLocusId.hashCode());
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

        final android.view.contentcapture.ContentCaptureCondition other = ((android.view.contentcapture.ContentCaptureCondition) (obj));
        if (mFlags != other.mFlags)
            return false;

        if (mLocusId == null) {
            if (other.mLocusId != null)
                return false;

        } else {
            if (!mLocusId.equals(other.mLocusId))
                return false;

        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder string = new java.lang.StringBuilder(mLocusId.toString());// LocusID is PII safe

        if (mFlags != 0) {
            string.append(" (").append(android.util.DebugUtils.flagsToString(android.view.contentcapture.ContentCaptureCondition.class, "FLAG_", mFlags)).append(')');
        }
        return string.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mLocusId, flags);
        parcel.writeInt(mFlags);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.contentcapture.ContentCaptureCondition> CREATOR = new android.os.Parcelable.Creator<android.view.contentcapture.ContentCaptureCondition>() {
        @java.lang.Override
        public android.view.contentcapture.ContentCaptureCondition createFromParcel(@android.annotation.NonNull
        android.os.Parcel parcel) {
            return new android.view.contentcapture.ContentCaptureCondition(parcel.readParcelable(null), parcel.readInt());
        }

        @java.lang.Override
        public android.view.contentcapture.ContentCaptureCondition[] newArray(int size) {
            return new android.view.contentcapture.ContentCaptureCondition[size];
        }
    };
}

