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
public class KeyCharacteristics implements android.os.Parcelable {
    public android.security.keymaster.KeymasterArguments swEnforced;

    public android.security.keymaster.KeymasterArguments hwEnforced;

    public static final android.os.Parcelable.Creator<android.security.keymaster.KeyCharacteristics> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.KeyCharacteristics>() {
        @java.lang.Override
        public android.security.keymaster.KeyCharacteristics createFromParcel(android.os.Parcel in) {
            return new android.security.keymaster.KeyCharacteristics(in);
        }

        @java.lang.Override
        public android.security.keymaster.KeyCharacteristics[] newArray(int length) {
            return new android.security.keymaster.KeyCharacteristics[length];
        }
    };

    public KeyCharacteristics() {
    }

    protected KeyCharacteristics(android.os.Parcel in) {
        readFromParcel(in);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        swEnforced.writeToParcel(out, flags);
        hwEnforced.writeToParcel(out, flags);
    }

    public void readFromParcel(android.os.Parcel in) {
        swEnforced = android.security.keymaster.KeymasterArguments.CREATOR.createFromParcel(in);
        hwEnforced = android.security.keymaster.KeymasterArguments.CREATOR.createFromParcel(in);
    }

    /**
     * Returns the value of the specified enum tag or {@code defaultValue} if the tag is not
     * present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an enum tag.
     */
    public java.lang.Integer getEnum(int tag) {
        if (hwEnforced.containsTag(tag)) {
            return hwEnforced.getEnum(tag, -1);
        } else
            if (swEnforced.containsTag(tag)) {
                return swEnforced.getEnum(tag, -1);
            } else {
                return null;
            }

    }

    /**
     * Returns all values of the specified repeating enum tag.
     *
     * throws IllegalArgumentException if {@code tag} is not a repeating enum tag.
     */
    public java.util.List<java.lang.Integer> getEnums(int tag) {
        java.util.List<java.lang.Integer> result = new java.util.ArrayList<java.lang.Integer>();
        result.addAll(hwEnforced.getEnums(tag));
        result.addAll(swEnforced.getEnums(tag));
        return result;
    }

    /**
     * Returns the value of the specified unsigned 32-bit int tag or {@code defaultValue} if the tag
     * is not present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an unsigned 32-bit int tag.
     */
    public long getUnsignedInt(int tag, long defaultValue) {
        if (hwEnforced.containsTag(tag)) {
            return hwEnforced.getUnsignedInt(tag, defaultValue);
        } else {
            return swEnforced.getUnsignedInt(tag, defaultValue);
        }
    }

    /**
     * Returns all values of the specified repeating unsigned 64-bit long tag.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a repeating unsigned 64-bit long tag.
     */
    public java.util.List<java.math.BigInteger> getUnsignedLongs(int tag) {
        java.util.List<java.math.BigInteger> result = new java.util.ArrayList<java.math.BigInteger>();
        result.addAll(hwEnforced.getUnsignedLongs(tag));
        result.addAll(swEnforced.getUnsignedLongs(tag));
        return result;
    }

    /**
     * Returns the value of the specified date tag or {@code null} if the tag is not present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a date tag or if the tag's value
     * 		represents a time instant which is after {@code 2^63 - 1} milliseconds since Unix
     * 		epoch.
     */
    public java.util.Date getDate(int tag) {
        java.util.Date result = swEnforced.getDate(tag, null);
        if (result != null) {
            return result;
        }
        return hwEnforced.getDate(tag, null);
    }

    /**
     * Returns {@code true} if the provided boolean tag is present, {@code false} if absent.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a boolean tag.
     */
    public boolean getBoolean(int tag) {
        if (hwEnforced.containsTag(tag)) {
            return hwEnforced.getBoolean(tag);
        } else {
            return swEnforced.getBoolean(tag);
        }
    }
}

