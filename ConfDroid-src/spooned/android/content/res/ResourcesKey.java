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
package android.content.res;


/**
 *
 *
 * @unknown 
 */
public final class ResourcesKey {
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    public final java.lang.String mResDir;

    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    public final java.lang.String[] mSplitResDirs;

    @android.annotation.Nullable
    public final java.lang.String[] mOverlayDirs;

    @android.annotation.Nullable
    public final java.lang.String[] mLibDirs;

    public final int mDisplayId;

    @android.annotation.NonNull
    public final android.content.res.Configuration mOverrideConfiguration;

    @android.annotation.NonNull
    public final android.content.res.CompatibilityInfo mCompatInfo;

    private final int mHash;

    @android.annotation.UnsupportedAppUsage
    public ResourcesKey(@android.annotation.Nullable
    java.lang.String resDir, @android.annotation.Nullable
    java.lang.String[] splitResDirs, @android.annotation.Nullable
    java.lang.String[] overlayDirs, @android.annotation.Nullable
    java.lang.String[] libDirs, int displayId, @android.annotation.Nullable
    android.content.res.Configuration overrideConfig, @android.annotation.Nullable
    android.content.res.CompatibilityInfo compatInfo) {
        mResDir = resDir;
        mSplitResDirs = splitResDirs;
        mOverlayDirs = overlayDirs;
        mLibDirs = libDirs;
        mDisplayId = displayId;
        mOverrideConfiguration = new android.content.res.Configuration(overrideConfig != null ? overrideConfig : android.content.res.Configuration.EMPTY);
        mCompatInfo = (compatInfo != null) ? compatInfo : android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        int hash = 17;
        hash = (31 * hash) + java.util.Objects.hashCode(mResDir);
        hash = (31 * hash) + java.util.Arrays.hashCode(mSplitResDirs);
        hash = (31 * hash) + java.util.Arrays.hashCode(mOverlayDirs);
        hash = (31 * hash) + java.util.Arrays.hashCode(mLibDirs);
        hash = (31 * hash) + mDisplayId;
        hash = (31 * hash) + java.util.Objects.hashCode(mOverrideConfiguration);
        hash = (31 * hash) + java.util.Objects.hashCode(mCompatInfo);
        mHash = hash;
    }

    public boolean hasOverrideConfiguration() {
        return !android.content.res.Configuration.EMPTY.equals(mOverrideConfiguration);
    }

    public boolean isPathReferenced(java.lang.String path) {
        if ((mResDir != null) && mResDir.startsWith(path)) {
            return true;
        } else {
            return (android.content.res.ResourcesKey.anyStartsWith(mSplitResDirs, path) || android.content.res.ResourcesKey.anyStartsWith(mOverlayDirs, path)) || android.content.res.ResourcesKey.anyStartsWith(mLibDirs, path);
        }
    }

    private static boolean anyStartsWith(java.lang.String[] list, java.lang.String prefix) {
        if (list != null) {
            for (java.lang.String s : list) {
                if ((s != null) && s.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return mHash;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.content.res.ResourcesKey)) {
            return false;
        }
        android.content.res.ResourcesKey peer = ((android.content.res.ResourcesKey) (obj));
        if (mHash != peer.mHash) {
            // If the hashes don't match, the objects can't match.
            return false;
        }
        if (!java.util.Objects.equals(mResDir, peer.mResDir)) {
            return false;
        }
        if (!java.util.Arrays.equals(mSplitResDirs, peer.mSplitResDirs)) {
            return false;
        }
        if (!java.util.Arrays.equals(mOverlayDirs, peer.mOverlayDirs)) {
            return false;
        }
        if (!java.util.Arrays.equals(mLibDirs, peer.mLibDirs)) {
            return false;
        }
        if (mDisplayId != peer.mDisplayId) {
            return false;
        }
        if (!java.util.Objects.equals(mOverrideConfiguration, peer.mOverrideConfiguration)) {
            return false;
        }
        if (!java.util.Objects.equals(mCompatInfo, peer.mCompatInfo)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder().append("ResourcesKey{");
        builder.append(" mHash=").append(java.lang.Integer.toHexString(mHash));
        builder.append(" mResDir=").append(mResDir);
        builder.append(" mSplitDirs=[");
        if (mSplitResDirs != null) {
            builder.append(android.text.TextUtils.join(",", mSplitResDirs));
        }
        builder.append("]");
        builder.append(" mOverlayDirs=[");
        if (mOverlayDirs != null) {
            builder.append(android.text.TextUtils.join(",", mOverlayDirs));
        }
        builder.append("]");
        builder.append(" mLibDirs=[");
        if (mLibDirs != null) {
            builder.append(android.text.TextUtils.join(",", mLibDirs));
        }
        builder.append("]");
        builder.append(" mDisplayId=").append(mDisplayId);
        builder.append(" mOverrideConfig=").append(android.content.res.Configuration.resourceQualifierString(mOverrideConfiguration));
        builder.append(" mCompatInfo=").append(mCompatInfo);
        builder.append("}");
        return builder.toString();
    }
}

