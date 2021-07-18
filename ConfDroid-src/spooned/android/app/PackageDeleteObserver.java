/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.app;


/**
 * {@hide }
 */
public class PackageDeleteObserver {
    private final IPackageDeleteObserver2.Stub mBinder = new android.content.pm.IPackageDeleteObserver2.Stub() {
        @java.lang.Override
        public void onUserActionRequired(android.content.Intent intent) {
            android.app.PackageDeleteObserver.this.onUserActionRequired(intent);
        }

        @java.lang.Override
        public void onPackageDeleted(java.lang.String basePackageName, int returnCode, java.lang.String msg) {
            android.app.PackageDeleteObserver.this.onPackageDeleted(basePackageName, returnCode, msg);
        }
    };

    /**
     * {@hide }
     */
    public android.content.pm.IPackageDeleteObserver2 getBinder() {
        return mBinder;
    }

    public void onUserActionRequired(android.content.Intent intent) {
    }

    public void onPackageDeleted(java.lang.String basePackageName, int returnCode, java.lang.String msg) {
    }
}

