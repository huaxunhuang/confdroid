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
 * This class provides information for a shared library. There are
 * three types of shared libraries: builtin - non-updatable part of
 * the OS; dynamic - updatable backwards-compatible dynamically linked;
 * static - non backwards-compatible emulating static linking.
 */
public final class SharedLibraryInfo implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static android.content.pm.SharedLibraryInfo createForStatic(android.content.pm.PackageParser.Package pkg) {
        return new android.content.pm.SharedLibraryInfo(null, pkg.packageName, pkg.getAllCodePaths(), pkg.staticSharedLibName, pkg.staticSharedLibVersion, android.content.pm.SharedLibraryInfo.TYPE_STATIC, new android.content.pm.VersionedPackage(pkg.manifestPackageName, pkg.getLongVersionCode()), null, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.content.pm.SharedLibraryInfo createForDynamic(android.content.pm.PackageParser.Package pkg, java.lang.String name) {
        return new android.content.pm.SharedLibraryInfo(null, pkg.packageName, pkg.getAllCodePaths(), name, ((long) (android.content.pm.SharedLibraryInfo.VERSION_UNDEFINED)), android.content.pm.SharedLibraryInfo.TYPE_DYNAMIC, new android.content.pm.VersionedPackage(pkg.packageName, pkg.getLongVersionCode()), null, null);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "TYPE_" }, value = { android.content.pm.SharedLibraryInfo.TYPE_BUILTIN, android.content.pm.SharedLibraryInfo.TYPE_DYNAMIC, android.content.pm.SharedLibraryInfo.TYPE_STATIC })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface Type {}

    /**
     * Shared library type: this library is a part of the OS
     * and cannot be updated or uninstalled.
     */
    public static final int TYPE_BUILTIN = 0;

    /**
     * Shared library type: this library is backwards-compatible, can
     * be updated, and updates can be uninstalled. Clients link against
     * the latest version of the library.
     */
    public static final int TYPE_DYNAMIC = 1;

    /**
     * Shared library type: this library is <strong>not</strong> backwards
     * -compatible, can be updated and updates can be uninstalled. Clients
     * link against a specific version of the library.
     */
    public static final int TYPE_STATIC = 2;

    /**
     * Constant for referring to an undefined version.
     */
    public static final int VERSION_UNDEFINED = -1;

    private final java.lang.String mPath;

    private final java.lang.String mPackageName;

    private final java.lang.String mName;

    private final java.util.List<java.lang.String> mCodePaths;

    private final long mVersion;

    @android.content.pm.SharedLibraryInfo.Type
    private final int mType;

    private final android.content.pm.VersionedPackage mDeclaringPackage;

    private final java.util.List<android.content.pm.VersionedPackage> mDependentPackages;

    private java.util.List<android.content.pm.SharedLibraryInfo> mDependencies;

    /**
     * Creates a new instance.
     *
     * @param codePaths
     * 		For a non {@link #TYPE_BUILTIN builtin} library, the locations of jars of
     * 		this shared library. Null for builtin library.
     * @param name
     * 		The lib name.
     * @param version
     * 		The lib version if not builtin.
     * @param type
     * 		The lib type.
     * @param declaringPackage
     * 		The package that declares the library.
     * @param dependentPackages
     * 		The packages that depend on the library.
     * @unknown 
     */
    public SharedLibraryInfo(java.lang.String path, java.lang.String packageName, java.util.List<java.lang.String> codePaths, java.lang.String name, long version, int type, android.content.pm.VersionedPackage declaringPackage, java.util.List<android.content.pm.VersionedPackage> dependentPackages, java.util.List<android.content.pm.SharedLibraryInfo> dependencies) {
        mPath = path;
        mPackageName = packageName;
        mCodePaths = codePaths;
        mName = name;
        mVersion = version;
        mType = type;
        mDeclaringPackage = declaringPackage;
        mDependentPackages = dependentPackages;
        mDependencies = dependencies;
    }

    private SharedLibraryInfo(android.os.Parcel parcel) {
        this(parcel.readString(), parcel.readString(), parcel.readArrayList(null), parcel.readString(), parcel.readLong(), parcel.readInt(), parcel.readParcelable(null), parcel.readArrayList(null), parcel.createTypedArrayList(CREATOR));
    }

    /**
     * Gets the type of this library.
     *
     * @return The library type.
     */
    @android.content.pm.SharedLibraryInfo.Type
    public int getType() {
        return mType;
    }

    /**
     * Gets the library name an app defines in its manifest
     * to depend on the library.
     *
     * @return The name.
     */
    public java.lang.String getName() {
        return mName;
    }

    /**
     * If the shared library is a jar file, returns the path of that jar. Null otherwise.
     * Only libraries with TYPE_BUILTIN are in jar files.
     *
     * @return The path.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String getPath() {
        return mPath;
    }

    /**
     * If the shared library is an apk, returns the package name. Null otherwise.
     * Only libraries with TYPE_DYNAMIC or TYPE_STATIC are in apks.
     *
     * @return The package name.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     * Get all code paths for that library.
     *
     * @return All code paths.
     * @unknown 
     */
    public java.util.List<java.lang.String> getAllCodePaths() {
        if (getPath() != null) {
            // Builtin library.
            java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
            list.add(getPath());
            return list;
        } else {
            // Static or dynamic library.
            return mCodePaths;
        }
    }

    /**
     * Add a library dependency to that library. Note that this
     * should be called under the package manager lock.
     *
     * @unknown 
     */
    public void addDependency(@android.annotation.Nullable
    android.content.pm.SharedLibraryInfo info) {
        if (info == null) {
            // For convenience of the caller, allow null to be passed.
            // This can happen when we create the dependencies of builtin
            // libraries.
            return;
        }
        if (mDependencies == null) {
            mDependencies = new java.util.ArrayList<>();
        }
        mDependencies.add(info);
    }

    /**
     * Clear all dependencies.
     *
     * @unknown 
     */
    public void clearDependencies() {
        mDependencies = null;
    }

    /**
     * Gets the libraries this library directly depends on. Note that
     * the package manager prevents recursive dependencies when installing
     * a package.
     *
     * @return The dependencies.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.util.List<android.content.pm.SharedLibraryInfo> getDependencies() {
        return mDependencies;
    }

    /**
     *
     *
     * @deprecated Use {@link #getLongVersion()} instead.
     */
    @java.lang.Deprecated
    @android.annotation.IntRange(from = -1)
    public int getVersion() {
        return mVersion < 0 ? ((int) (mVersion)) : ((int) (mVersion & 0x7fffffff));
    }

    /**
     * Gets the version of the library. For {@link #TYPE_STATIC static} libraries
     * this is the declared version and for {@link #TYPE_DYNAMIC dynamic} and
     * {@link #TYPE_BUILTIN builtin} it is {@link #VERSION_UNDEFINED} as these
     * are not versioned.
     *
     * @return The version.
     */
    @android.annotation.IntRange(from = -1)
    public long getLongVersion() {
        return mVersion;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isBuiltin() {
        return mType == android.content.pm.SharedLibraryInfo.TYPE_BUILTIN;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isDynamic() {
        return mType == android.content.pm.SharedLibraryInfo.TYPE_DYNAMIC;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isStatic() {
        return mType == android.content.pm.SharedLibraryInfo.TYPE_STATIC;
    }

    /**
     * Gets the package that declares the library.
     *
     * @return The package declaring the library.
     */
    @android.annotation.NonNull
    public android.content.pm.VersionedPackage getDeclaringPackage() {
        return mDeclaringPackage;
    }

    /**
     * Gets the packages that depend on the library.
     *
     * @return The dependent packages.
     */
    @android.annotation.NonNull
    public java.util.List<android.content.pm.VersionedPackage> getDependentPackages() {
        if (mDependentPackages == null) {
            return java.util.Collections.emptyList();
        }
        return mDependentPackages;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("SharedLibraryInfo{name:" + mName) + ", type:") + android.content.pm.SharedLibraryInfo.typeToString(mType)) + ", version:") + mVersion) + (!getDependentPackages().isEmpty() ? " has dependents" : "")) + "}";
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mPath);
        parcel.writeString(mPackageName);
        parcel.writeList(mCodePaths);
        parcel.writeString(mName);
        parcel.writeLong(mVersion);
        parcel.writeInt(mType);
        parcel.writeParcelable(mDeclaringPackage, flags);
        parcel.writeList(mDependentPackages);
        parcel.writeTypedList(mDependencies);
    }

    private static java.lang.String typeToString(int type) {
        switch (type) {
            case android.content.pm.SharedLibraryInfo.TYPE_BUILTIN :
                {
                    return "builtin";
                }
            case android.content.pm.SharedLibraryInfo.TYPE_DYNAMIC :
                {
                    return "dynamic";
                }
            case android.content.pm.SharedLibraryInfo.TYPE_STATIC :
                {
                    return "static";
                }
            default :
                {
                    return "unknown";
                }
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.SharedLibraryInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.SharedLibraryInfo>() {
        public android.content.pm.SharedLibraryInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.SharedLibraryInfo(source);
        }

        public android.content.pm.SharedLibraryInfo[] newArray(int size) {
            return new android.content.pm.SharedLibraryInfo[size];
        }
    };
}

