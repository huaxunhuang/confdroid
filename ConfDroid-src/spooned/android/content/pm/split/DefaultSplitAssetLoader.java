/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.content.pm.split;


/**
 * Loads the base and split APKs into a single AssetManager.
 *
 * @unknown 
 */
public class DefaultSplitAssetLoader implements android.content.pm.split.SplitAssetLoader {
    private final java.lang.String mBaseCodePath;

    private final java.lang.String[] mSplitCodePaths;

    @android.content.pm.PackageParser.ParseFlags
    private final int mFlags;

    private android.content.res.AssetManager mCachedAssetManager;

    public DefaultSplitAssetLoader(android.content.pm.PackageParser.PackageLite pkg, @android.content.pm.PackageParser.ParseFlags
    int flags) {
        mBaseCodePath = pkg.baseCodePath;
        mSplitCodePaths = pkg.splitCodePaths;
        mFlags = flags;
    }

    private static android.content.res.ApkAssets loadApkAssets(java.lang.String path, @android.content.pm.PackageParser.ParseFlags
    int flags) throws android.content.pm.PackageParser.PackageParserException {
        if (((flags & android.content.pm.PackageParser.PARSE_MUST_BE_APK) != 0) && (!android.content.pm.PackageParser.isApkPath(path))) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_NOT_APK, "Invalid package file: " + path);
        }
        try {
            return android.content.res.ApkAssets.loadFromPath(path);
        } catch (java.io.IOException e) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_FAILED_INVALID_APK, "Failed to load APK at path " + path, e);
        }
    }

    @java.lang.Override
    public android.content.res.AssetManager getBaseAssetManager() throws android.content.pm.PackageParser.PackageParserException {
        if (mCachedAssetManager != null) {
            return mCachedAssetManager;
        }
        android.content.res.ApkAssets[] apkAssets = new android.content.res.ApkAssets[(mSplitCodePaths != null ? mSplitCodePaths.length : 0) + 1];
        // Load the base.
        int splitIdx = 0;
        apkAssets[splitIdx++] = android.content.pm.split.DefaultSplitAssetLoader.loadApkAssets(mBaseCodePath, mFlags);
        // Load any splits.
        if (!com.android.internal.util.ArrayUtils.isEmpty(mSplitCodePaths)) {
            for (java.lang.String apkPath : mSplitCodePaths) {
                apkAssets[splitIdx++] = android.content.pm.split.DefaultSplitAssetLoader.loadApkAssets(apkPath, mFlags);
            }
        }
        android.content.res.AssetManager assets = new android.content.res.AssetManager();
        assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        /* invalidateCaches */
        assets.setApkAssets(apkAssets, false);
        mCachedAssetManager = assets;
        return mCachedAssetManager;
    }

    @java.lang.Override
    public android.content.res.AssetManager getSplitAssetManager(int splitIdx) throws android.content.pm.PackageParser.PackageParserException {
        return getBaseAssetManager();
    }

    @java.lang.Override
    public void close() throws java.lang.Exception {
        libcore.io.IoUtils.closeQuietly(mCachedAssetManager);
    }
}

