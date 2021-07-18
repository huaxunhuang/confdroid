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
class KeymasterBlobArgument extends android.security.keymaster.KeymasterArgument {
    public final byte[] blob;

    public KeymasterBlobArgument(int tag, byte[] blob) {
        super(tag);
        switch (android.security.keymaster.KeymasterDefs.getTagType(tag)) {
            case android.security.keymaster.KeymasterDefs.KM_BIGNUM :
            case android.security.keymaster.KeymasterDefs.KM_BYTES :
                break;// OK.

            default :
                throw new java.lang.IllegalArgumentException("Bad blob tag " + tag);
        }
        this.blob = blob;
    }

    public KeymasterBlobArgument(int tag, android.os.Parcel in) {
        super(tag);
        blob = in.createByteArray();
    }

    @java.lang.Override
    public void writeValue(android.os.Parcel out) {
        out.writeByteArray(blob);
    }
}

