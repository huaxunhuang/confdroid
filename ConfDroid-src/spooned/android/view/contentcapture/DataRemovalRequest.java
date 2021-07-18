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
 * Class used by apps to remove content capture data associated with {@link LocusId LocusIds}.
 *
 * <p>An app which has tagged data with a LocusId can therefore delete them later. This is intended
 * to let apps propagate deletions of user data into the operating system.
 */
public final class DataRemovalRequest implements android.os.Parcelable {
    /**
     * When set, the {@link LocusId#getId()} is the prefix for the data to be removed.
     */
    public static final int FLAG_IS_PREFIX = 0x1;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "FLAG" }, flag = true, value = { android.view.contentcapture.DataRemovalRequest.FLAG_IS_PREFIX })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface Flags {}

    private final java.lang.String mPackageName;

    private final boolean mForEverything;

    private java.util.ArrayList<android.view.contentcapture.DataRemovalRequest.LocusIdRequest> mLocusIdRequests;

    private DataRemovalRequest(@android.annotation.NonNull
    android.view.contentcapture.DataRemovalRequest.Builder builder) {
        mPackageName = android.app.ActivityThread.currentActivityThread().getApplication().getPackageName();
        mForEverything = builder.mForEverything;
        if (builder.mLocusIds != null) {
            final int size = builder.mLocusIds.size();
            mLocusIdRequests = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                mLocusIdRequests.add(new android.view.contentcapture.DataRemovalRequest.LocusIdRequest(builder.mLocusIds.get(i), builder.mFlags.get(i)));
            }
        }
    }

    private DataRemovalRequest(@android.annotation.NonNull
    android.os.Parcel parcel) {
        mPackageName = parcel.readString();
        mForEverything = parcel.readBoolean();
        if (!mForEverything) {
            final int size = parcel.readInt();
            mLocusIdRequests = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                mLocusIdRequests.add(new android.view.contentcapture.DataRemovalRequest.LocusIdRequest(((android.content.LocusId) (parcel.readValue(null))), parcel.readInt()));
            }
        }
    }

    /**
     * Gets the name of the app that's making the request.
     */
    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     * Checks if app is requesting to remove content capture data associated with its package.
     */
    public boolean isForEverything() {
        return mForEverything;
    }

    /**
     * Gets the list of {@code LousId}s the apps is requesting to remove.
     */
    @android.annotation.NonNull
    public java.util.List<android.view.contentcapture.DataRemovalRequest.LocusIdRequest> getLocusIdRequests() {
        return mLocusIdRequests;
    }

    /**
     * Builder for {@link DataRemovalRequest} objects.
     */
    public static final class Builder {
        private boolean mForEverything;

        private java.util.ArrayList<android.content.LocusId> mLocusIds;

        private android.util.IntArray mFlags;

        private boolean mDestroyed;

        /**
         * Requests to remove all content capture data associated with the app's package.
         *
         * @return this builder
         */
        @android.annotation.NonNull
        public android.view.contentcapture.DataRemovalRequest.Builder forEverything() {
            throwIfDestroyed();
            com.android.internal.util.Preconditions.checkState(mLocusIds == null, "Already added LocusIds");
            mForEverything = true;
            return this;
        }

        /**
         * Request service to remove data associated with a given {@link LocusId}.
         *
         * @param locusId
         * 		the {@link LocusId} being requested to be removed.
         * @param flags
         * 		either {@link DataRemovalRequest#FLAG_IS_PREFIX} or {@code 0}
         * @return this builder
         */
        @android.annotation.NonNull
        public android.view.contentcapture.DataRemovalRequest.Builder addLocusId(@android.annotation.NonNull
        android.content.LocusId locusId, @android.view.contentcapture.DataRemovalRequest.Flags
        int flags) {
            throwIfDestroyed();
            com.android.internal.util.Preconditions.checkState(!mForEverything, "Already is for everything");
            com.android.internal.util.Preconditions.checkNotNull(locusId);
            // felipeal: check flags
            if (mLocusIds == null) {
                mLocusIds = new java.util.ArrayList<>();
                mFlags = new android.util.IntArray();
            }
            mLocusIds.add(locusId);
            mFlags.add(flags);
            return this;
        }

        /**
         * Builds the {@link DataRemovalRequest}.
         */
        @android.annotation.NonNull
        public android.view.contentcapture.DataRemovalRequest build() {
            throwIfDestroyed();
            com.android.internal.util.Preconditions.checkState(mForEverything || (mLocusIds != null), "must call either #forEverything() or add one #addLocusId()");
            mDestroyed = true;
            return new android.view.contentcapture.DataRemovalRequest(this);
        }

        private void throwIfDestroyed() {
            com.android.internal.util.Preconditions.checkState(!mDestroyed, "Already destroyed!");
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mPackageName);
        parcel.writeBoolean(mForEverything);
        if (!mForEverything) {
            final int size = mLocusIdRequests.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; i++) {
                final android.view.contentcapture.DataRemovalRequest.LocusIdRequest request = mLocusIdRequests.get(i);
                parcel.writeValue(request.getLocusId());
                parcel.writeInt(request.getFlags());
            }
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.contentcapture.DataRemovalRequest> CREATOR = new android.os.Parcelable.Creator<android.view.contentcapture.DataRemovalRequest>() {
        @java.lang.Override
        @android.annotation.NonNull
        public android.view.contentcapture.DataRemovalRequest createFromParcel(android.os.Parcel parcel) {
            return new android.view.contentcapture.DataRemovalRequest(parcel);
        }

        @java.lang.Override
        @android.annotation.NonNull
        public android.view.contentcapture.DataRemovalRequest[] newArray(int size) {
            return new android.view.contentcapture.DataRemovalRequest[size];
        }
    };

    /**
     * Representation of a request to remove data associated with a {@link LocusId}.
     */
    public final class LocusIdRequest {
        @android.annotation.NonNull
        private final android.content.LocusId mLocusId;

        @android.view.contentcapture.DataRemovalRequest.Flags
        private final int mFlags;

        private LocusIdRequest(@android.annotation.NonNull
        android.content.LocusId locusId, @android.view.contentcapture.DataRemovalRequest.Flags
        int flags) {
            this.mLocusId = locusId;
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
         * Gets the flags associates with request.
         *
         * @return either {@link DataRemovalRequest#FLAG_IS_PREFIX} or {@code 0}.
         */
        @android.annotation.NonNull
        @android.view.contentcapture.DataRemovalRequest.Flags
        public int getFlags() {
            return mFlags;
        }
    }
}

