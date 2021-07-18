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
 * All of the package name installed on the system.
 * <p>A self observable list that automatically removes the listener when it goes out of scope.
 *
 * @unknown Only for use within the system server.
 */
public class PackageList implements android.content.pm.PackageManagerInternal.PackageListObserver , java.lang.AutoCloseable {
    private final android.content.pm.PackageManagerInternal.PackageListObserver mWrappedObserver;

    private final java.util.List<java.lang.String> mPackageNames;

    /**
     * Create a new object.
     * <p>Ownership of the given {@link List} transfers to this object and should not
     * be modified by the caller.
     */
    public PackageList(@android.annotation.NonNull
    java.util.List<java.lang.String> packageNames, @android.annotation.Nullable
    android.content.pm.PackageManagerInternal.PackageListObserver observer) {
        mPackageNames = packageNames;
        mWrappedObserver = observer;
    }

    @java.lang.Override
    public void onPackageAdded(java.lang.String packageName, int uid) {
        if (mWrappedObserver != null) {
            mWrappedObserver.onPackageAdded(packageName, uid);
        }
    }

    @java.lang.Override
    public void onPackageRemoved(java.lang.String packageName, int uid) {
        if (mWrappedObserver != null) {
            mWrappedObserver.onPackageRemoved(packageName, uid);
        }
    }

    @java.lang.Override
    public void close() throws java.lang.Exception {
        com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class).removePackageListObserver(this);
    }

    /**
     * Returns the names of packages installed on the system.
     * <p>The list is a copy-in-time and the actual set of installed packages may differ. Real
     * time updates to the package list are sent via the {@link PackageListObserver} callback.
     */
    @android.annotation.NonNull
    public java.util.List<java.lang.String> getPackageNames() {
        return mPackageNames;
    }
}

