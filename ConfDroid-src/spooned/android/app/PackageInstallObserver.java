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
public class PackageInstallObserver {
    private final IPackageInstallObserver2.Stub mBinder = new android.content.pm.IPackageInstallObserver2.Stub() {
        @java.lang.Override
        public void onUserActionRequired(android.content.Intent intent) {
            android.app.PackageInstallObserver.this.onUserActionRequired(intent);
        }

        @java.lang.Override
        public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) {
            android.app.PackageInstallObserver.this.onPackageInstalled(basePackageName, returnCode, msg, extras);
        }
    };

    /**
     * {@hide }
     */
    public android.content.pm.IPackageInstallObserver2 getBinder() {
        return mBinder;
    }

    public void onUserActionRequired(android.content.Intent intent) {
    }

    /**
     * This method will be called to report the result of the package
     * installation attempt.
     *
     * @param basePackageName
     * 		Name of the package whose installation was
     * 		attempted
     * @param extras
     * 		If non-null, this Bundle contains extras providing
     * 		additional information about an install failure. See
     * 		{@link android.content.pm.PackageManager} for documentation
     * 		about which extras apply to various failures; in particular
     * 		the strings named EXTRA_FAILURE_*.
     * @param returnCode
     * 		The numeric success or failure code indicating the
     * 		basic outcome
     * @unknown 
     */
    public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) {
    }
}

