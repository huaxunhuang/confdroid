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
package android.content.pm;


/**
 * Describes an externally resolvable instant application. There are three states that this class
 * can represent: <p/>
 * <ul>
 *     <li>
 *         The first, usable only for non http/s intents, implies that the resolver cannot
 *         immediately resolve this intent and would prefer that resolution be deferred to the
 *         instant app installer. Represent this state with {@link #InstantAppResolveInfo(Bundle)}.
 *         If the {@link android.content.Intent} has the scheme set to http/s and a set of digest
 *         prefixes were passed into one of the resolve methods in
 *         {@link android.app.InstantAppResolverService}, this state cannot be used.
 *     </li>
 *     <li>
 *         The second represents a partial match and is constructed with any of the other
 *         constructors. By setting one or more of the {@link Nullable}arguments to null, you
 *         communicate to the resolver in response to
 *         {@link android.app.InstantAppResolverService#onGetInstantAppResolveInfo(Intent, int[],
 *                String, InstantAppResolverService.InstantAppResolutionCallback)}
 *         that you need a 2nd round of resolution to complete the request.
 *     </li>
 *     <li>
 *         The third represents a complete match and is constructed with all @Nullable parameters
 *         populated.
 *     </li>
 * </ul>
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class InstantAppResolveInfo implements android.os.Parcelable {
    /**
     * Algorithm that will be used to generate the domain digest
     */
    private static final java.lang.String SHA_ALGORITHM = "SHA-256";

    private static final byte[] EMPTY_DIGEST = new byte[0];

    private final android.content.pm.InstantAppResolveInfo.InstantAppDigest mDigest;

    private final java.lang.String mPackageName;

    /**
     * The filters used to match domain
     */
    private final java.util.List<android.content.pm.InstantAppIntentFilter> mFilters;

    /**
     * The version code of the app that this class resolves to
     */
    private final long mVersionCode;

    /**
     * Data about the app that should be passed along to the Instant App installer on resolve
     */
    private final android.os.Bundle mExtras;

    /**
     * A flag that indicates that the resolver is aware that an app may match, but would prefer
     * that the installer get the sanitized intent to decide.
     */
    private final boolean mShouldLetInstallerDecide;

    /**
     * Constructor for intent-based InstantApp resolution results.
     */
    public InstantAppResolveInfo(@android.annotation.NonNull
    android.content.pm.InstantAppResolveInfo.InstantAppDigest digest, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.InstantAppIntentFilter> filters, int versionCode) {
        /* extras */
        this(digest, packageName, filters, ((long) (versionCode)), null);
    }

    /**
     * Constructor for intent-based InstantApp resolution results with extras.
     */
    public InstantAppResolveInfo(@android.annotation.NonNull
    android.content.pm.InstantAppResolveInfo.InstantAppDigest digest, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.InstantAppIntentFilter> filters, long versionCode, @android.annotation.Nullable
    android.os.Bundle extras) {
        this(digest, packageName, filters, versionCode, extras, false);
    }

    /**
     * Constructor for intent-based InstantApp resolution results by hostname.
     */
    public InstantAppResolveInfo(@android.annotation.NonNull
    java.lang.String hostName, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.InstantAppIntentFilter> filters) {
        /* versionCode */
        /* extras */
        this(new android.content.pm.InstantAppResolveInfo.InstantAppDigest(hostName), packageName, filters, -1, null);
    }

    /**
     * Constructor that indicates that resolution could be delegated to the installer when the
     * sanitized intent contains enough information to resolve completely.
     */
    public InstantAppResolveInfo(@android.annotation.Nullable
    android.os.Bundle extras) {
        this(android.content.pm.InstantAppResolveInfo.InstantAppDigest.UNDEFINED, null, null, -1, extras, true);
    }

    private InstantAppResolveInfo(@android.annotation.NonNull
    android.content.pm.InstantAppResolveInfo.InstantAppDigest digest, @android.annotation.Nullable
    java.lang.String packageName, @android.annotation.Nullable
    java.util.List<android.content.pm.InstantAppIntentFilter> filters, long versionCode, @android.annotation.Nullable
    android.os.Bundle extras, boolean shouldLetInstallerDecide) {
        // validate arguments
        if (((packageName == null) && ((filters != null) && (filters.size() != 0))) || ((packageName != null) && ((filters == null) || (filters.size() == 0)))) {
            throw new java.lang.IllegalArgumentException();
        }
        mDigest = digest;
        if (filters != null) {
            mFilters = new java.util.ArrayList<>(filters.size());
            mFilters.addAll(filters);
        } else {
            mFilters = null;
        }
        mPackageName = packageName;
        mVersionCode = versionCode;
        mExtras = extras;
        mShouldLetInstallerDecide = shouldLetInstallerDecide;
    }

    InstantAppResolveInfo(android.os.Parcel in) {
        mShouldLetInstallerDecide = in.readBoolean();
        mExtras = in.readBundle();
        if (mShouldLetInstallerDecide) {
            mDigest = android.content.pm.InstantAppResolveInfo.InstantAppDigest.UNDEFINED;
            mPackageName = null;
            mFilters = java.util.Collections.emptyList();
            mVersionCode = -1;
        } else {
            mDigest = /* loader */
            in.readParcelable(null);
            mPackageName = in.readString();
            mFilters = new java.util.ArrayList<>();
            /* loader */
            in.readList(mFilters, null);
            mVersionCode = in.readLong();
        }
    }

    /**
     * Returns true if the resolver is aware that an app may match, but would prefer
     * that the installer get the sanitized intent to decide. This should not be true for
     * resolutions that include a host and will be ignored in such cases.
     */
    public boolean shouldLetInstallerDecide() {
        return mShouldLetInstallerDecide;
    }

    public byte[] getDigestBytes() {
        return mDigest.mDigestBytes.length > 0 ? mDigest.getDigestBytes()[0] : android.content.pm.InstantAppResolveInfo.EMPTY_DIGEST;
    }

    public int getDigestPrefix() {
        return mDigest.getDigestPrefix()[0];
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    public java.util.List<android.content.pm.InstantAppIntentFilter> getIntentFilters() {
        return mFilters;
    }

    /**
     *
     *
     * @deprecated Use {@link #getLongVersionCode} instead.
     */
    @java.lang.Deprecated
    public int getVersionCode() {
        return ((int) (mVersionCode & 0xffffffff));
    }

    public long getLongVersionCode() {
        return mVersionCode;
    }

    @android.annotation.Nullable
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeBoolean(mShouldLetInstallerDecide);
        out.writeBundle(mExtras);
        if (mShouldLetInstallerDecide) {
            return;
        }
        out.writeParcelable(mDigest, flags);
        out.writeString(mPackageName);
        out.writeList(mFilters);
        out.writeLong(mVersionCode);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.InstantAppResolveInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.InstantAppResolveInfo>() {
        public android.content.pm.InstantAppResolveInfo createFromParcel(android.os.Parcel in) {
            return new android.content.pm.InstantAppResolveInfo(in);
        }

        public android.content.pm.InstantAppResolveInfo[] newArray(int size) {
            return new android.content.pm.InstantAppResolveInfo[size];
        }
    };

    /**
     * Helper class to generate and store each of the digests and prefixes
     * sent to the Instant App Resolver.
     * <p>
     * Since intent filters may want to handle multiple hosts within a
     * domain [eg “*.google.com”], the resolver is presented with multiple
     * hash prefixes. For example, "a.b.c.d.e" generates digests for
     * "d.e", "c.d.e", "b.c.d.e" and "a.b.c.d.e".
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class InstantAppDigest implements android.os.Parcelable {
        static final int DIGEST_MASK = 0xfffff000;

        /**
         * A special instance that represents and undefined digest used for cases that a host was
         * not provided or is irrelevant to the response.
         */
        public static final android.content.pm.InstantAppResolveInfo.InstantAppDigest UNDEFINED = new android.content.pm.InstantAppResolveInfo.InstantAppDigest(new byte[][]{  }, new int[]{  });

        private static java.util.Random sRandom = null;

        static {
            try {
                android.content.pm.InstantAppResolveInfo.InstantAppDigest.sRandom = java.security.SecureRandom.getInstance("SHA1PRNG");
            } catch (java.security.NoSuchAlgorithmException e) {
                // oh well
                android.content.pm.InstantAppResolveInfo.InstantAppDigest.sRandom = new java.util.Random();
            }
        }

        /**
         * Full digest of the domain hashes
         */
        private final byte[][] mDigestBytes;

        /**
         * The first 5 bytes of the domain hashes
         */
        private final int[] mDigestPrefix;

        /**
         * The first 5 bytes of the domain hashes interspersed with random data
         */
        private int[] mDigestPrefixSecure;

        public InstantAppDigest(@android.annotation.NonNull
        java.lang.String hostName) {
            /* maxDigests */
            this(hostName, -1);
        }

        /**
         *
         *
         * @unknown 
         */
        public InstantAppDigest(@android.annotation.NonNull
        java.lang.String hostName, int maxDigests) {
            if (hostName == null) {
                throw new java.lang.IllegalArgumentException();
            }
            mDigestBytes = android.content.pm.InstantAppResolveInfo.InstantAppDigest.generateDigest(hostName.toLowerCase(java.util.Locale.ENGLISH), maxDigests);
            mDigestPrefix = new int[mDigestBytes.length];
            for (int i = 0; i < mDigestBytes.length; i++) {
                mDigestPrefix[i] = (((((mDigestBytes[i][0] & 0xff) << 24) | ((mDigestBytes[i][1] & 0xff) << 16)) | ((mDigestBytes[i][2] & 0xff) << 8)) | ((mDigestBytes[i][3] & 0xff) << 0)) & android.content.pm.InstantAppResolveInfo.InstantAppDigest.DIGEST_MASK;
            }
        }

        private InstantAppDigest(byte[][] digestBytes, int[] prefix) {
            this.mDigestPrefix = prefix;
            this.mDigestBytes = digestBytes;
        }

        private static byte[][] generateDigest(java.lang.String hostName, int maxDigests) {
            java.util.ArrayList<byte[]> digests = new java.util.ArrayList<>();
            try {
                final java.security.MessageDigest digest = java.security.MessageDigest.getInstance(android.content.pm.InstantAppResolveInfo.SHA_ALGORITHM);
                if (maxDigests <= 0) {
                    final byte[] hostBytes = hostName.getBytes();
                    digests.add(digest.digest(hostBytes));
                } else {
                    int prevDot = hostName.lastIndexOf('.');
                    prevDot = hostName.lastIndexOf('.', prevDot - 1);
                    // shortcut for short URLs
                    if (prevDot < 0) {
                        digests.add(digest.digest(hostName.getBytes()));
                    } else {
                        byte[] hostBytes = hostName.substring(prevDot + 1, hostName.length()).getBytes();
                        digests.add(digest.digest(hostBytes));
                        int digestCount = 1;
                        while ((prevDot >= 0) && (digestCount < maxDigests)) {
                            prevDot = hostName.lastIndexOf('.', prevDot - 1);
                            hostBytes = hostName.substring(prevDot + 1, hostName.length()).getBytes();
                            digests.add(digest.digest(hostBytes));
                            digestCount++;
                        } 
                    }
                }
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.lang.IllegalStateException("could not find digest algorithm");
            }
            return digests.toArray(new byte[digests.size()][]);
        }

        InstantAppDigest(android.os.Parcel in) {
            final int digestCount = in.readInt();
            if (digestCount == (-1)) {
                mDigestBytes = null;
            } else {
                mDigestBytes = new byte[digestCount][];
                for (int i = 0; i < digestCount; i++) {
                    mDigestBytes[i] = in.createByteArray();
                }
            }
            mDigestPrefix = in.createIntArray();
            mDigestPrefixSecure = in.createIntArray();
        }

        public byte[][] getDigestBytes() {
            return mDigestBytes;
        }

        public int[] getDigestPrefix() {
            return mDigestPrefix;
        }

        /**
         * Returns a digest prefix with additional random prefixes interspersed.
         *
         * @unknown 
         */
        public int[] getDigestPrefixSecure() {
            if (this == android.content.pm.InstantAppResolveInfo.InstantAppDigest.UNDEFINED) {
                return getDigestPrefix();
            } else
                if (mDigestPrefixSecure == null) {
                    // let's generate some random data to intersperse throughout the set of prefixes
                    final int realSize = getDigestPrefix().length;
                    final int manufacturedSize = (realSize + 10) + android.content.pm.InstantAppResolveInfo.InstantAppDigest.sRandom.nextInt(10);
                    mDigestPrefixSecure = java.util.Arrays.copyOf(getDigestPrefix(), manufacturedSize);
                    for (int i = realSize; i < manufacturedSize; i++) {
                        mDigestPrefixSecure[i] = android.content.pm.InstantAppResolveInfo.InstantAppDigest.sRandom.nextInt() & android.content.pm.InstantAppResolveInfo.InstantAppDigest.DIGEST_MASK;
                    }
                    java.util.Arrays.sort(mDigestPrefixSecure);
                }

            return mDigestPrefixSecure;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            final boolean isUndefined = this == android.content.pm.InstantAppResolveInfo.InstantAppDigest.UNDEFINED;
            out.writeBoolean(isUndefined);
            if (isUndefined) {
                return;
            }
            if (mDigestBytes == null) {
                out.writeInt(-1);
            } else {
                out.writeInt(mDigestBytes.length);
                for (int i = 0; i < mDigestBytes.length; i++) {
                    out.writeByteArray(mDigestBytes[i]);
                }
            }
            out.writeIntArray(mDigestPrefix);
            out.writeIntArray(mDigestPrefixSecure);
        }

        @java.lang.SuppressWarnings("hiding")
        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.content.pm.InstantAppResolveInfo.InstantAppDigest> CREATOR = new android.os.Parcelable.Creator<android.content.pm.InstantAppResolveInfo.InstantAppDigest>() {
            @java.lang.Override
            public android.content.pm.InstantAppDigest createFromParcel(android.os.Parcel in) {
                /* is undefined */
                if (in.readBoolean()) {
                    return InstantAppDigest.UNDEFINED;
                }
                return new android.content.pm.InstantAppDigest(in);
            }

            @java.lang.Override
            public android.content.pm.InstantAppDigest[] newArray(int size) {
                return new android.content.pm.InstantAppDigest[size];
            }
        };
    }
}

