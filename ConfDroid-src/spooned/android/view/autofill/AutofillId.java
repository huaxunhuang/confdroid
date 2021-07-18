/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.autofill;


/**
 * A unique identifier for an autofill node inside an {@link android.app.Activity}.
 */
public final class AutofillId implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static final int NO_SESSION = 0;

    private static final int FLAG_IS_VIRTUAL_INT = 0x1;

    private static final int FLAG_IS_VIRTUAL_LONG = 0x2;

    private static final int FLAG_HAS_SESSION = 0x4;

    private final int mViewId;

    private int mFlags;

    private final int mVirtualIntId;

    private final long mVirtualLongId;

    private int mSessionId;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public AutofillId(int id) {
        /* flags= */
        this(0, id, android.view.View.NO_ID, android.view.autofill.AutofillId.NO_SESSION);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public AutofillId(@android.annotation.NonNull
    android.view.autofill.AutofillId hostId, int virtualChildId) {
        this(android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_INT, hostId.mViewId, virtualChildId, android.view.autofill.AutofillId.NO_SESSION);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public AutofillId(int hostId, int virtualChildId) {
        this(android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_INT, hostId, virtualChildId, android.view.autofill.AutofillId.NO_SESSION);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public AutofillId(@android.annotation.NonNull
    android.view.autofill.AutofillId hostId, long virtualChildId, int sessionId) {
        this(android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_LONG | android.view.autofill.AutofillId.FLAG_HAS_SESSION, hostId.mViewId, virtualChildId, sessionId);
    }

    private AutofillId(int flags, int parentId, long virtualChildId, int sessionId) {
        mFlags = flags;
        mViewId = parentId;
        mVirtualIntId = ((flags & android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_INT) != 0) ? ((int) (virtualChildId)) : android.view.View.NO_ID;
        mVirtualLongId = ((flags & android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_LONG) != 0) ? virtualChildId : android.view.View.NO_ID;
        mSessionId = sessionId;
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.view.autofill.AutofillId withoutSession(@android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        final int flags = id.mFlags & (~android.view.autofill.AutofillId.FLAG_HAS_SESSION);
        return new android.view.autofill.AutofillId(flags, id.mViewId, id.mVirtualLongId, android.view.autofill.AutofillId.NO_SESSION);
    }

    /**
     *
     *
     * @unknown 
     */
    public int getViewId() {
        return mViewId;
    }

    /**
     * Gets the virtual child id.
     *
     * <p>Should only be used on subsystems where such id is represented by an {@code int}
     * (Assist and Autofill).
     *
     * @unknown 
     */
    public int getVirtualChildIntId() {
        return mVirtualIntId;
    }

    /**
     * Gets the virtual child id.
     *
     * <p>Should only be used on subsystems where such id is represented by a {@code long}
     * (ContentCapture).
     *
     * @unknown 
     */
    public long getVirtualChildLongId() {
        return mVirtualLongId;
    }

    /**
     * Checks whether this node represents a virtual child, whose id is represented by an
     * {@code int}.
     *
     * <p>Should only be used on subsystems where such id is represented by an {@code int}
     * (Assist and Autofill).
     *
     * @unknown 
     */
    public boolean isVirtualInt() {
        return (mFlags & android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_INT) != 0;
    }

    /**
     * Checks whether this node represents a virtual child, whose id is represented by an
     * {@code long}.
     *
     * <p>Should only be used on subsystems where such id is represented by a {@code long}
     * (ContentCapture).
     *
     * @unknown 
     */
    public boolean isVirtualLong() {
        return (mFlags & android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_LONG) != 0;
    }

    /**
     * Checks whether this node represents a non-virtual child.
     *
     * @unknown 
     */
    public boolean isNonVirtual() {
        return (!isVirtualInt()) && (!isVirtualLong());
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasSession() {
        return (mFlags & android.view.autofill.AutofillId.FLAG_HAS_SESSION) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSessionId() {
        return mSessionId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setSessionId(int sessionId) {
        mFlags |= android.view.autofill.AutofillId.FLAG_HAS_SESSION;
        mSessionId = sessionId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void resetSessionId() {
        mFlags &= ~android.view.autofill.AutofillId.FLAG_HAS_SESSION;
        mSessionId = android.view.autofill.AutofillId.NO_SESSION;
    }

    // ///////////////////////////////
    // Object "contract" methods. //
    // ///////////////////////////////
    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mViewId;
        result = (prime * result) + mVirtualIntId;
        result = (prime * result) + ((int) (mVirtualLongId ^ (mVirtualLongId >>> 32)));
        result = (prime * result) + mSessionId;
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

        final android.view.autofill.AutofillId other = ((android.view.autofill.AutofillId) (obj));
        if (mViewId != other.mViewId)
            return false;

        if (mVirtualIntId != other.mVirtualIntId)
            return false;

        if (mVirtualLongId != other.mVirtualLongId)
            return false;

        if (mSessionId != other.mSessionId)
            return false;

        return true;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public boolean equalsIgnoreSession(@android.annotation.Nullable
    android.view.autofill.AutofillId other) {
        if (this == other)
            return true;

        if (other == null)
            return false;

        if (mViewId != other.mViewId)
            return false;

        if (mVirtualIntId != other.mVirtualIntId)
            return false;

        if (mVirtualLongId != other.mVirtualLongId)
            return false;

        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder().append(mViewId);
        if (isVirtualInt()) {
            builder.append(':').append(mVirtualIntId);
        } else
            if (isVirtualLong()) {
                builder.append(':').append(mVirtualLongId);
            }

        if (hasSession()) {
            builder.append('@').append(mSessionId);
        }
        return builder.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mViewId);
        parcel.writeInt(mFlags);
        if (hasSession()) {
            parcel.writeInt(mSessionId);
        }
        if (isVirtualInt()) {
            parcel.writeInt(mVirtualIntId);
        } else
            if (isVirtualLong()) {
                parcel.writeLong(mVirtualLongId);
            }

    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.autofill.AutofillId> CREATOR = new android.os.Parcelable.Creator<android.view.autofill.AutofillId>() {
        @java.lang.Override
        public android.view.autofill.AutofillId createFromParcel(android.os.Parcel source) {
            final int viewId = source.readInt();
            final int flags = source.readInt();
            final int sessionId = ((flags & android.view.autofill.AutofillId.FLAG_HAS_SESSION) != 0) ? source.readInt() : android.view.autofill.AutofillId.NO_SESSION;
            if ((flags & android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_INT) != 0) {
                return new android.view.autofill.AutofillId(flags, viewId, source.readInt(), sessionId);
            }
            if ((flags & android.view.autofill.AutofillId.FLAG_IS_VIRTUAL_LONG) != 0) {
                return new android.view.autofill.AutofillId(flags, viewId, source.readLong(), sessionId);
            }
            return new android.view.autofill.AutofillId(flags, viewId, View.NO_ID, sessionId);
        }

        @java.lang.Override
        public android.view.autofill.AutofillId[] newArray(int size) {
            return new android.view.autofill.AutofillId[size];
        }
    };
}

