/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Special private access for certain globals related to a process.
 *
 * @unknown 
 */
public class AppGlobals {
    /**
     * Return the first Application object made in the process.
     * NOTE: Only works on the main thread.
     */
    public static android.app.Application getInitialApplication() {
        return android.app.ActivityThread.currentApplication();
    }

    /**
     * Return the package name of the first .apk loaded into the process.
     * NOTE: Only works on the main thread.
     */
    public static java.lang.String getInitialPackage() {
        return android.app.ActivityThread.currentPackageName();
    }

    /**
     * Return the raw interface to the package manager.
     *
     * @return The package manager.
     */
    public static android.content.pm.IPackageManager getPackageManager() {
        return android.app.ActivityThread.getPackageManager();
    }

    /**
     * Gets the value of an integer core setting.
     *
     * @param key
     * 		The setting key.
     * @param defaultValue
     * 		The setting default value.
     * @return The core settings.
     */
    public static int getIntCoreSetting(java.lang.String key, int defaultValue) {
        android.app.ActivityThread currentActivityThread = android.app.ActivityThread.currentActivityThread();
        if (currentActivityThread != null) {
            return currentActivityThread.getIntCoreSetting(key, defaultValue);
        } else {
            return defaultValue;
        }
    }
}

