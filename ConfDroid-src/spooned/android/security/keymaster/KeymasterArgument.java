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
 * Base class for the Java side of a Keymaster tagged argument.
 * <p>
 * Serialization code for this and subclasses must be kept in sync with system/security/keystore
 * and with hardware/libhardware/include/hardware/keymaster_defs.h
 *
 * @unknown 
 */
abstract class KeymasterArgument implements android.os.Parcelable {
    public final int tag;

    public static final android.os.Parcelable.Creator<android.security.keymaster.KeymasterArgument> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.KeymasterArgument>() {
        @java.lang.Override
        public android.security.keymaster.KeymasterArgument createFromParcel(android.os.Parcel in) {
            final int pos = in.dataPosition();
            final int tag = in.readInt();
            switch (android.security.keymaster.KeymasterDefs.getTagType(tag)) {
                case android.security.keymaster.KeymasterDefs.KM_ENUM :
                case android.security.keymaster.KeymasterDefs.KM_ENUM_REP :
                case android.security.keymaster.KeymasterDefs.KM_UINT :
                case android.security.keymaster.KeymasterDefs.KM_UINT_REP :
                    return new android.security.keymaster.KeymasterIntArgument(tag, in);
                case android.security.keymaster.KeymasterDefs.KM_ULONG :
                case android.security.keymaster.KeymasterDefs.KM_ULONG_REP :
                    return new android.security.keymaster.KeymasterLongArgument(tag, in);
                case android.security.keymaster.KeymasterDefs.KM_DATE :
                    return new android.security.keymaster.KeymasterDateArgument(tag, in);
                case android.security.keymaster.KeymasterDefs.KM_BYTES :
                case android.security.keymaster.KeymasterDefs.KM_BIGNUM :
                    return new android.security.keymaster.KeymasterBlobArgument(tag, in);
                case android.security.keymaster.KeymasterDefs.KM_BOOL :
                    return new android.security.keymaster.KeymasterBooleanArgument(tag, in);
                default :
                    throw new android.os.ParcelFormatException((("Bad tag: " + tag) + " at ") + pos);
            }
        }

        @java.lang.Override
        public android.security.keymaster.KeymasterArgument[] newArray(int size) {
            return new android.security.keymaster.KeymasterArgument[size];
        }
    };

    protected KeymasterArgument(int tag) {
        this.tag = tag;
    }

    /**
     * Writes the value of this argument, if any, to the provided parcel.
     */
    public abstract void writeValue(android.os.Parcel out);

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(tag);
        writeValue(out);
    }
}

