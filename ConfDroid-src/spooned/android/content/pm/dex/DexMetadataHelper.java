/**
 * Copyright 2018 The Android Open Source Project
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
package android.content.pm.dex;


/**
 * Helper class used to compute and validate the location of dex metadata files.
 *
 * @unknown 
 */
public class DexMetadataHelper {
    private static final java.lang.String DEX_METADATA_FILE_EXTENSION = ".dm";

    private DexMetadataHelper() {
    }

    /**
     * Return true if the given file is a dex metadata file.
     */
    public static boolean isDexMetadataFile(java.io.File file) {
        return android.content.pm.dex.DexMetadataHelper.isDexMetadataPath(file.getName());
    }

    /**
     * Return true if the given path is a dex metadata path.
     */
    private static boolean isDexMetadataPath(java.lang.String path) {
        return path.endsWith(android.content.pm.dex.DexMetadataHelper.DEX_METADATA_FILE_EXTENSION);
    }

    /**
     * Return the size (in bytes) of all dex metadata files associated with the given package.
     */
    public static long getPackageDexMetadataSize(android.content.pm.PackageParser.PackageLite pkg) {
        long sizeBytes = 0;
        java.util.Collection<java.lang.String> dexMetadataList = android.content.pm.dex.DexMetadataHelper.getPackageDexMetadata(pkg).values();
        for (java.lang.String dexMetadata : dexMetadataList) {
            sizeBytes += new java.io.File(dexMetadata).length();
        }
        return sizeBytes;
    }

    /**
     * Search for the dex metadata file associated with the given target file.
     * If it exists, the method returns the dex metadata file; otherwise it returns null.
     *
     * Note that this performs a loose matching suitable to be used in the InstallerSession logic.
     * i.e. the method will attempt to match the {@code dmFile} regardless of {@code targetFile}
     * extension (e.g. 'foo.dm' will match 'foo' or 'foo.apk').
     */
    public static java.io.File findDexMetadataForFile(java.io.File targetFile) {
        java.lang.String dexMetadataPath = android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForFile(targetFile);
        java.io.File dexMetadataFile = new java.io.File(dexMetadataPath);
        return dexMetadataFile.exists() ? dexMetadataFile : null;
    }

    /**
     * Return the dex metadata files for the given package as a map
     * [code path -> dex metadata path].
     *
     * NOTE: involves I/O checks.
     */
    public static java.util.Map<java.lang.String, java.lang.String> getPackageDexMetadata(android.content.pm.PackageParser.Package pkg) {
        return android.content.pm.dex.DexMetadataHelper.buildPackageApkToDexMetadataMap(pkg.getAllCodePaths());
    }

    /**
     * Return the dex metadata files for the given package as a map
     * [code path -> dex metadata path].
     *
     * NOTE: involves I/O checks.
     */
    private static java.util.Map<java.lang.String, java.lang.String> getPackageDexMetadata(android.content.pm.PackageParser.PackageLite pkg) {
        return android.content.pm.dex.DexMetadataHelper.buildPackageApkToDexMetadataMap(pkg.getAllCodePaths());
    }

    /**
     * Look up the dex metadata files for the given code paths building the map
     * [code path -> dex metadata].
     *
     * For each code path (.apk) the method checks if a matching dex metadata file (.dm) exists.
     * If it does it adds the pair to the returned map.
     *
     * Note that this method will do a loose
     * matching based on the extension ('foo.dm' will match 'foo.apk' or 'foo').
     *
     * This should only be used for code paths extracted from a package structure after the naming
     * was enforced in the installer.
     */
    private static java.util.Map<java.lang.String, java.lang.String> buildPackageApkToDexMetadataMap(java.util.List<java.lang.String> codePaths) {
        android.util.ArrayMap<java.lang.String, java.lang.String> result = new android.util.ArrayMap();
        for (int i = codePaths.size() - 1; i >= 0; i--) {
            java.lang.String codePath = codePaths.get(i);
            java.lang.String dexMetadataPath = android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForFile(new java.io.File(codePath));
            if (java.nio.file.Files.exists(java.nio.file.Paths.get(dexMetadataPath))) {
                result.put(codePath, dexMetadataPath);
            }
        }
        return result;
    }

    /**
     * Return the dex metadata path associated with the given code path.
     * (replaces '.apk' extension with '.dm')
     *
     * @throws IllegalArgumentException
     * 		if the code path is not an .apk.
     */
    public static java.lang.String buildDexMetadataPathForApk(java.lang.String codePath) {
        if (!android.content.pm.PackageParser.isApkPath(codePath)) {
            throw new java.lang.IllegalStateException("Corrupted package. Code path is not an apk " + codePath);
        }
        return codePath.substring(0, codePath.length() - android.content.pm.PackageParser.APK_FILE_EXTENSION.length()) + android.content.pm.dex.DexMetadataHelper.DEX_METADATA_FILE_EXTENSION;
    }

    /**
     * Return the dex metadata path corresponding to the given {@code targetFile} using a loose
     * matching.
     * i.e. the method will attempt to match the {@code dmFile} regardless of {@code targetFile}
     * extension (e.g. 'foo.dm' will match 'foo' or 'foo.apk').
     */
    private static java.lang.String buildDexMetadataPathForFile(java.io.File targetFile) {
        return android.content.pm.PackageParser.isApkFile(targetFile) ? android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForApk(targetFile.getPath()) : targetFile.getPath() + android.content.pm.dex.DexMetadataHelper.DEX_METADATA_FILE_EXTENSION;
    }

    /**
     * Validate the dex metadata files installed for the given package.
     *
     * @throws PackageParserException
     * 		in case of errors.
     */
    public static void validatePackageDexMetadata(android.content.pm.PackageParser.Package pkg) throws android.content.pm.PackageParser.PackageParserException {
        java.util.Collection<java.lang.String> apkToDexMetadataList = android.content.pm.dex.DexMetadataHelper.getPackageDexMetadata(pkg).values();
        for (java.lang.String dexMetadata : apkToDexMetadataList) {
            android.content.pm.dex.DexMetadataHelper.validateDexMetadataFile(dexMetadata);
        }
    }

    /**
     * Validate that the given file is a dex metadata archive.
     * This is just a sanity validation that the file is a zip archive.
     *
     * @throws PackageParserException
     * 		if the file is not a .dm file.
     */
    private static void validateDexMetadataFile(java.lang.String dmaPath) throws android.content.pm.PackageParser.PackageParserException {
        android.util.jar.StrictJarFile jarFile = null;
        try {
            jarFile = new android.util.jar.StrictJarFile(dmaPath, false, false);
        } catch (java.io.IOException e) {
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "Error opening " + dmaPath, e);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (java.io.IOException ignored) {
                }
            }
        }
    }

    /**
     * Validates that all dex metadata paths in the given list have a matching apk.
     * (for any foo.dm there should be either a 'foo' of a 'foo.apk' file).
     * If that's not the case it throws {@code IllegalStateException}.
     *
     * This is used to perform a basic sanity check during adb install commands.
     * (The installer does not support stand alone .dm files)
     */
    public static void validateDexPaths(java.lang.String[] paths) {
        java.util.ArrayList<java.lang.String> apks = new java.util.ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            if (android.content.pm.PackageParser.isApkPath(paths[i])) {
                apks.add(paths[i]);
            }
        }
        java.util.ArrayList<java.lang.String> unmatchedDmFiles = new java.util.ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            java.lang.String dmPath = paths[i];
            if (android.content.pm.dex.DexMetadataHelper.isDexMetadataPath(dmPath)) {
                boolean valid = false;
                for (int j = apks.size() - 1; j >= 0; j--) {
                    if (dmPath.equals(android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForFile(new java.io.File(apks.get(j))))) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    unmatchedDmFiles.add(dmPath);
                }
            }
        }
        if (!unmatchedDmFiles.isEmpty()) {
            throw new java.lang.IllegalStateException("Unmatched .dm files: " + unmatchedDmFiles);
        }
    }
}

