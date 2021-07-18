/**
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package android.content.res;


/**
 * Provides a set of utility methods for dealing with Resource IDs.
 *
 * @unknown 
 */
public final class ResourceId {
    /**
     * Checks whether the integer {@code id} is a valid resource ID, as generated by AAPT.
     * <p>Note that a negative integer is not necessarily an invalid resource ID, and custom
     * validations that compare the {@code id} against {@code 0} are incorrect.</p>
     *
     * @param id
     * 		The integer to validate.
     * @return {@code true} if the integer is a valid resource ID.
     */
    public static boolean isValid(@android.annotation.AnyRes
    int id) {
        // With the introduction of packages with IDs > 0x7f, resource IDs can be negative when
        // represented as a signed Java int. Some legacy code assumes -1 is an invalid resource ID,
        // despite the existing documentation.
        return ((id != (-1)) && ((id & 0xff000000) != 0)) && ((id & 0xff0000) != 0);
    }
}

