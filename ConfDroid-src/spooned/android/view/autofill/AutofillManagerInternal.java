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
package android.view.autofill;


/**
 * Autofill Manager local system service interface.
 *
 * @unknown Only for use within the system server.
 */
public abstract class AutofillManagerInternal {
    /**
     * Notifies the manager that the back key was pressed.
     */
    public abstract void onBackKeyPressed();

    /**
     * Gets autofill options for a package.
     *
     * <p><b>NOTE: </b>this method is called by the {@code ActivityManager} service and hence cannot
     * hold the main service lock.
     *
     * @param packageName
     * 		The package for which to query.
     * @param versionCode
     * 		The package version code.
     * @param userId
     * 		The user id for which to query.
     */
    @android.annotation.Nullable
    public abstract android.content.AutofillOptions getAutofillOptions(@android.annotation.NonNull
    java.lang.String packageName, long versionCode, @android.annotation.UserIdInt
    int userId);

    /**
     * Checks whether the given {@code uid} owns the
     * {@link android.service.autofill.augmented.AugmentedAutofillService} implementation associated
     * with the given {@code userId}.
     */
    public abstract boolean isAugmentedAutofillServiceForUser(@android.annotation.NonNull
    int callingUid, @android.annotation.UserIdInt
    int userId);
}

