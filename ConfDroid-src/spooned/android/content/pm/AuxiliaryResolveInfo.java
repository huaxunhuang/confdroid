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
package android.content.pm;


/**
 * Auxiliary application resolution response.
 * <p>
 * Used when resolution occurs, but, the target is not actually on the device.
 * This happens resolving instant apps that haven't been installed yet or if
 * the application consists of multiple feature splits and the needed split
 * hasn't been installed.
 *
 * @unknown 
 */
public final class AuxiliaryResolveInfo {
    /**
     * The activity to launch if there's an installation failure.
     */
    public final android.content.ComponentName installFailureActivity;

    /**
     * Whether or not instant resolution needs the second phase
     */
    public final boolean needsPhaseTwo;

    /**
     * Opaque token to track the instant application resolution
     */
    public final java.lang.String token;

    /**
     * An intent to start upon failure to install
     */
    public final android.content.Intent failureIntent;

    /**
     * The matching filters for this resolve info.
     */
    public final java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> filters;

    /**
     * Create a response for installing an instant application.
     */
    public AuxiliaryResolveInfo(@android.annotation.NonNull
    java.lang.String token, boolean needsPhase2, @android.annotation.Nullable
    android.content.Intent failureIntent, @android.annotation.Nullable
    java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> filters) {
        this.token = token;
        this.needsPhaseTwo = needsPhase2;
        this.failureIntent = failureIntent;
        this.filters = filters;
        this.installFailureActivity = null;
    }

    /**
     * Create a response for installing a split on demand.
     */
    public AuxiliaryResolveInfo(@android.annotation.Nullable
    android.content.ComponentName failureActivity, @android.annotation.Nullable
    android.content.Intent failureIntent, @android.annotation.Nullable
    java.util.List<android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter> filters) {
        super();
        this.installFailureActivity = failureActivity;
        this.filters = filters;
        this.token = null;
        this.needsPhaseTwo = false;
        this.failureIntent = failureIntent;
    }

    /**
     * Create a response for installing a split on demand.
     */
    public AuxiliaryResolveInfo(@android.annotation.Nullable
    android.content.ComponentName failureActivity, java.lang.String packageName, long versionCode, java.lang.String splitName) {
        this(failureActivity, null, java.util.Collections.singletonList(new android.content.pm.AuxiliaryResolveInfo.AuxiliaryFilter(packageName, versionCode, splitName)));
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class AuxiliaryFilter extends android.content.IntentFilter {
        /**
         * Resolved information returned from the external instant resolver
         */
        public final android.content.pm.InstantAppResolveInfo resolveInfo;

        /**
         * The resolved package. Copied from {@link #resolveInfo}.
         */
        public final java.lang.String packageName;

        /**
         * The version code of the package
         */
        public final long versionCode;

        /**
         * The resolve split. Copied from the matched filter in {@link #resolveInfo}.
         */
        public final java.lang.String splitName;

        /**
         * The extras to pass on to the installer for this filter.
         */
        public final android.os.Bundle extras;

        public AuxiliaryFilter(android.content.IntentFilter orig, android.content.pm.InstantAppResolveInfo resolveInfo, java.lang.String splitName, android.os.Bundle extras) {
            super(orig);
            this.resolveInfo = resolveInfo;
            this.packageName = resolveInfo.getPackageName();
            this.versionCode = resolveInfo.getLongVersionCode();
            this.splitName = splitName;
            this.extras = extras;
        }

        public AuxiliaryFilter(android.content.pm.InstantAppResolveInfo resolveInfo, java.lang.String splitName, android.os.Bundle extras) {
            this.resolveInfo = resolveInfo;
            this.packageName = resolveInfo.getPackageName();
            this.versionCode = resolveInfo.getLongVersionCode();
            this.splitName = splitName;
            this.extras = extras;
        }

        public AuxiliaryFilter(java.lang.String packageName, long versionCode, java.lang.String splitName) {
            this.resolveInfo = null;
            this.packageName = packageName;
            this.versionCode = versionCode;
            this.splitName = splitName;
            this.extras = null;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((("AuxiliaryFilter{" + "packageName='") + packageName) + '\'') + ", versionCode=") + versionCode) + ", splitName='") + splitName) + '\'') + '}';
        }
    }
}

