/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.content.pm;


/**
 * Information about an ephemeral application.
 *
 * @unknown 
 * @unknown 
 */
@java.lang.Deprecated
@android.annotation.SystemApi
public final class EphemeralResolveInfo implements android.os.Parcelable {
    /**
     * Algorithm that will be used to generate the domain digest
     */
    public static final java.lang.String SHA_ALGORITHM = "SHA-256";

    private final android.content.pm.InstantAppResolveInfo mInstantAppResolveInfo;

    @java.lang.Deprecated
    private final java.util.List<android.content.IntentFilter> mLegacyFilters;

    @java.lang.Deprecated
    public EphemeralResolveInfo(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String packageName, @android.annotation.NonNull
    java.util.List<android.content.IntentFilter> filters) {
        if ((((uri == null) || (packageName == null)) || (filters == null)) || filters.isEmpty()) {
            throw new java.lang.IllegalArgumentException();
        }
        final java.util.List<android.content.pm.EphemeralIntentFilter> ephemeralFilters = new java.util.ArrayList<>(1);
        ephemeralFilters.add(new android.content.pm.EphemeralIntentFilter(packageName, filters));
        mInstantAppResolveInfo = new android.content.pm.InstantAppResolveInfo(uri.getHost(), packageName, android.content.pm.EphemeralResolveInfo.createInstantAppIntentFilterList(ephemeralFilters));
        mLegacyFilters = new java.util.ArrayList<android.content.IntentFilter>(filters.size());
        mLegacyFilters.addAll(filters);
    }

    @java.lang.Deprecated
    public EphemeralResolveInfo(@android.annotation.NonNull
    android.content.pm.EphemeralResolveInfo.EphemeralDigest digest, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.EphemeralIntentFilter> filters) {
        /* versionCode */
        this(digest, packageName, filters, -1);
    }

    public EphemeralResolveInfo(@android.annotation.NonNull
    android.content.pm.EphemeralResolveInfo.EphemeralDigest digest, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.EphemeralIntentFilter> filters, int versionCode) {
        mInstantAppResolveInfo = new android.content.pm.InstantAppResolveInfo(digest.getInstantAppDigest(), packageName, android.content.pm.EphemeralResolveInfo.createInstantAppIntentFilterList(filters), versionCode);
        mLegacyFilters = null;
    }

    public EphemeralResolveInfo(@android.annotation.NonNull
    java.lang.String hostName, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.EphemeralIntentFilter> filters) {
        this(new android.content.pm.EphemeralResolveInfo.EphemeralDigest(hostName), packageName, filters);
    }

    EphemeralResolveInfo(android.os.Parcel in) {
        mInstantAppResolveInfo = /* loader */
        in.readParcelable(null);
        mLegacyFilters = new java.util.ArrayList<android.content.IntentFilter>();
        /* loader */
        in.readList(mLegacyFilters, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.pm.InstantAppResolveInfo getInstantAppResolveInfo() {
        return mInstantAppResolveInfo;
    }

    private static java.util.List<android.content.pm.InstantAppIntentFilter> createInstantAppIntentFilterList(java.util.List<android.content.pm.EphemeralIntentFilter> filters) {
        if (filters == null) {
            return null;
        }
        final int filterCount = filters.size();
        final java.util.List<android.content.pm.InstantAppIntentFilter> returnList = new java.util.ArrayList<>(filterCount);
        for (int i = 0; i < filterCount; i++) {
            returnList.add(filters.get(i).getInstantAppIntentFilter());
        }
        return returnList;
    }

    public byte[] getDigestBytes() {
        return mInstantAppResolveInfo.getDigestBytes();
    }

    public int getDigestPrefix() {
        return mInstantAppResolveInfo.getDigestPrefix();
    }

    public java.lang.String getPackageName() {
        return mInstantAppResolveInfo.getPackageName();
    }

    public java.util.List<android.content.pm.EphemeralIntentFilter> getIntentFilters() {
        final java.util.List<android.content.pm.InstantAppIntentFilter> filters = mInstantAppResolveInfo.getIntentFilters();
        final int filterCount = filters.size();
        final java.util.List<android.content.pm.EphemeralIntentFilter> returnList = new java.util.ArrayList<>(filterCount);
        for (int i = 0; i < filterCount; i++) {
            returnList.add(new android.content.pm.EphemeralIntentFilter(filters.get(i)));
        }
        return returnList;
    }

    public int getVersionCode() {
        return mInstantAppResolveInfo.getVersionCode();
    }

    @java.lang.Deprecated
    public java.util.List<android.content.IntentFilter> getFilters() {
        return mLegacyFilters;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeParcelable(mInstantAppResolveInfo, flags);
        out.writeList(mLegacyFilters);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.EphemeralResolveInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.EphemeralResolveInfo>() {
        @java.lang.Override
        public android.content.pm.EphemeralResolveInfo createFromParcel(android.os.Parcel in) {
            return new android.content.pm.EphemeralResolveInfo(in);
        }

        @java.lang.Override
        public android.content.pm.EphemeralResolveInfo[] newArray(int size) {
            return new android.content.pm.EphemeralResolveInfo[size];
        }
    };

    /**
     * Helper class to generate and store each of the digests and prefixes
     * sent to the Ephemeral Resolver.
     * <p>
     * Since intent filters may want to handle multiple hosts within a
     * domain [eg “*.google.com”], the resolver is presented with multiple
     * hash prefixes. For example, "a.b.c.d.e" generates digests for
     * "d.e", "c.d.e", "b.c.d.e" and "a.b.c.d.e".
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class EphemeralDigest implements android.os.Parcelable {
        private final android.content.pm.InstantAppResolveInfo.InstantAppDigest mInstantAppDigest;

        public EphemeralDigest(@android.annotation.NonNull
        java.lang.String hostName) {
            /* maxDigests */
            this(hostName, -1);
        }

        /**
         *
         *
         * @unknown 
         */
        public EphemeralDigest(@android.annotation.NonNull
        java.lang.String hostName, int maxDigests) {
            mInstantAppDigest = new android.content.pm.InstantAppResolveInfo.InstantAppDigest(hostName, maxDigests);
        }

        EphemeralDigest(android.os.Parcel in) {
            mInstantAppDigest = /* loader */
            in.readParcelable(null);
        }

        /**
         *
         *
         * @unknown 
         */
        android.content.pm.InstantAppResolveInfo.InstantAppDigest getInstantAppDigest() {
            return mInstantAppDigest;
        }

        public byte[][] getDigestBytes() {
            return mInstantAppDigest.getDigestBytes();
        }

        public int[] getDigestPrefix() {
            return mInstantAppDigest.getDigestPrefix();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeParcelable(mInstantAppDigest, flags);
        }

        @java.lang.SuppressWarnings("hiding")
        public static final android.os.Parcelable.Creator<android.content.pm.EphemeralResolveInfo.EphemeralDigest> CREATOR = new android.os.Parcelable.Creator<android.content.pm.EphemeralResolveInfo.EphemeralDigest>() {
            @java.lang.Override
            public android.content.pm.EphemeralDigest createFromParcel(android.os.Parcel in) {
                return new android.content.pm.EphemeralDigest(in);
            }

            @java.lang.Override
            public android.content.pm.EphemeralDigest[] newArray(int size) {
                return new android.content.pm.EphemeralDigest[size];
            }
        };
    }
}

