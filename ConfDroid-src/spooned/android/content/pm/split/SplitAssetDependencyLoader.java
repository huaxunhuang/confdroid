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
 * Loads AssetManagers for splits and their dependencies. This SplitAssetLoader implementation
 * is to be used when an application opts-in to isolated split loading.
 *
 * @unknown 
 */
public class SplitAssetDependencyLoader extends android.content.pm.split.SplitDependencyLoader<android.content.pm.PackageParser.PackageParserException> implements android.content.pm.split.SplitAssetLoader {
    private final java.lang.String[] mSplitPaths;

    @android.content.pm.PackageParser.ParseFlags
    private final int mFlags;

    private final android.content.res.ApkAssets[][] mCachedSplitApks;

    private final android.content.res.AssetManager[] mCachedAssetManagers;

    public SplitAssetDependencyLoader(android.content.pm.PackageParser.PackageLite pkg, android.util.SparseArray<int[]> dependencies, @android.content.pm.PackageParser.ParseFlags
    int flags) {
        super(dependencies);
        // The base is inserted into index 0, so we need to shift all the splits by 1.
        mSplitPaths = new java.lang.String[pkg.splitCodePaths.length + 1];
        mSplitPaths[0] = pkg.baseCodePath;
        java.lang.System.arraycopy(pkg.splitCodePaths, 0, mSplitPaths, 1, pkg.splitCodePaths.length);
        mFlags = flags;
        mCachedSplitApks = new android.content.res.ApkAssets[mSplitPaths.length][];
        mCachedAssetManagers = new android.content.res.AssetManager[mSplitPaths.length];
    }

    @java.lang.Override
    protected boolean isSplitCached(int splitIdx) {
        return mCachedAssetManagers[splitIdx] != null;
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

    private static android.content.res.AssetManager createAssetManagerWithAssets(android.content.res.ApkAssets[] apkAssets) {
        final android.content.res.AssetManager assets = new android.content.res.AssetManager();
        assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        /* invalidateCaches */
        assets.setApkAssets(apkAssets, false);
        return assets;
    }

    @java.lang.Override
    protected void constructSplit(int splitIdx, @android.annotation.NonNull
    int[] configSplitIndices, int parentSplitIdx) throws android.content.pm.PackageParser.PackageParserException {
        final java.util.ArrayList<android.content.res.ApkAssets> assets = new java.util.ArrayList<>();
        // Include parent ApkAssets.
        if (parentSplitIdx >= 0) {
            java.util.Collections.addAll(assets, mCachedSplitApks[parentSplitIdx]);
        }
        // Include this ApkAssets.
        assets.add(android.content.pm.split.SplitAssetDependencyLoader.loadApkAssets(mSplitPaths[splitIdx], mFlags));
        // Load and include all config splits for this feature.
        for (int configSplitIdx : configSplitIndices) {
            assets.add(android.content.pm.split.SplitAssetDependencyLoader.loadApkAssets(mSplitPaths[configSplitIdx], mFlags));
        }
        // Cache the results.
        mCachedSplitApks[splitIdx] = assets.toArray(new android.content.res.ApkAssets[assets.size()]);
        mCachedAssetManagers[splitIdx] = android.content.pm.split.SplitAssetDependencyLoader.createAssetManagerWithAssets(mCachedSplitApks[splitIdx]);
    }

    @java.lang.Override
    public android.content.res.AssetManager getBaseAssetManager() throws android.content.pm.PackageParser.PackageParserException {
        loadDependenciesForSplit(0);
        return mCachedAssetManagers[0];
    }

    @java.lang.Override
    public android.content.res.AssetManager getSplitAssetManager(int idx) throws android.content.pm.PackageParser.PackageParserException {
        // Since we insert the base at position 0, and PackageParser keeps splits separate from
        // the base, we need to adjust the index.
        loadDependenciesForSplit(idx + 1);
        return mCachedAssetManagers[idx + 1];
    }

    @java.lang.Override
    public void close() throws java.lang.Exception {
        for (android.content.res.AssetManager assets : mCachedAssetManagers) {
            libcore.io.IoUtils.closeQuietly(assets);
        }
    }
}

