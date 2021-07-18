/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.security.keymaster;


/**
 *
 *
 * @unknown 
 */
class KeymasterBooleanArgument extends android.security.keymaster.KeymasterArgument {
    // Boolean arguments are always true if they exist and false if they don't.
    public final boolean value = true;

    public KeymasterBooleanArgument(int tag) {
        super(tag);
        switch (android.security.keymaster.KeymasterDefs.getTagType(tag)) {
            case android.security.keymaster.KeymasterDefs.KM_BOOL :
                break;// OK.

            default :
                throw new java.lang.IllegalArgumentException("Bad bool tag " + tag);
        }
    }

    public KeymasterBooleanArgument(int tag, android.os.Parcel in) {
        super(tag);
    }

    @java.lang.Override
    public void writeValue(android.os.Parcel out) {
        // Do nothing, value is implicit.
    }
}

