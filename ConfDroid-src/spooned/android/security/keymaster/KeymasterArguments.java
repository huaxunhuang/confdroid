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
 * Utility class for the java side of user specified Keymaster arguments.
 * <p>
 * Serialization code for this and subclasses must be kept in sync with system/security/keystore
 *
 * @unknown 
 */
public class KeymasterArguments implements android.os.Parcelable {
    private static final long UINT32_RANGE = 1L << 32;

    public static final long UINT32_MAX_VALUE = android.security.keymaster.KeymasterArguments.UINT32_RANGE - 1;

    private static final java.math.BigInteger UINT64_RANGE = java.math.BigInteger.ONE.shiftLeft(64);

    public static final java.math.BigInteger UINT64_MAX_VALUE = android.security.keymaster.KeymasterArguments.UINT64_RANGE.subtract(java.math.BigInteger.ONE);

    private java.util.List<android.security.keymaster.KeymasterArgument> mArguments;

    public static final android.os.Parcelable.Creator<android.security.keymaster.KeymasterArguments> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.KeymasterArguments>() {
        @java.lang.Override
        public android.security.keymaster.KeymasterArguments createFromParcel(android.os.Parcel in) {
            return new android.security.keymaster.KeymasterArguments(in);
        }

        @java.lang.Override
        public android.security.keymaster.KeymasterArguments[] newArray(int size) {
            return new android.security.keymaster.KeymasterArguments[size];
        }
    };

    public KeymasterArguments() {
        mArguments = new java.util.ArrayList<android.security.keymaster.KeymasterArgument>();
    }

    private KeymasterArguments(android.os.Parcel in) {
        mArguments = in.createTypedArrayList(android.security.keymaster.KeymasterArgument.CREATOR);
    }

    /**
     * Adds an enum tag with the provided value.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an enum tag.
     */
    public void addEnum(int tag, int value) {
        int tagType = android.security.keymaster.KeymasterDefs.getTagType(tag);
        if ((tagType != android.security.keymaster.KeymasterDefs.KM_ENUM) && (tagType != android.security.keymaster.KeymasterDefs.KM_ENUM_REP)) {
            throw new java.lang.IllegalArgumentException("Not an enum or repeating enum tag: " + tag);
        }
        addEnumTag(tag, value);
    }

    /**
     * Adds a repeated enum tag with the provided values.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a repeating enum tag.
     */
    public void addEnums(int tag, int... values) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_ENUM_REP) {
            throw new java.lang.IllegalArgumentException("Not a repeating enum tag: " + tag);
        }
        for (int value : values) {
            addEnumTag(tag, value);
        }
    }

    /**
     * Returns the value of the specified enum tag or {@code defaultValue} if the tag is not
     * present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an enum tag.
     */
    public int getEnum(int tag, int defaultValue) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_ENUM) {
            throw new java.lang.IllegalArgumentException("Not an enum tag: " + tag);
        }
        android.security.keymaster.KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return getEnumTagValue(arg);
    }

    /**
     * Returns all values of the specified repeating enum tag.
     *
     * throws IllegalArgumentException if {@code tag} is not a repeating enum tag.
     */
    public java.util.List<java.lang.Integer> getEnums(int tag) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_ENUM_REP) {
            throw new java.lang.IllegalArgumentException("Not a repeating enum tag: " + tag);
        }
        java.util.List<java.lang.Integer> values = new java.util.ArrayList<java.lang.Integer>();
        for (android.security.keymaster.KeymasterArgument arg : mArguments) {
            if (arg.tag == tag) {
                values.add(getEnumTagValue(arg));
            }
        }
        return values;
    }

    private void addEnumTag(int tag, int value) {
        mArguments.add(new android.security.keymaster.KeymasterIntArgument(tag, value));
    }

    private int getEnumTagValue(android.security.keymaster.KeymasterArgument arg) {
        return ((android.security.keymaster.KeymasterIntArgument) (arg)).value;
    }

    /**
     * Adds an unsigned 32-bit int tag with the provided value.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an unsigned 32-bit int tag or if
     * 		{@code value} is outside of the permitted range [0; 2^32).
     */
    public void addUnsignedInt(int tag, long value) {
        int tagType = android.security.keymaster.KeymasterDefs.getTagType(tag);
        if ((tagType != android.security.keymaster.KeymasterDefs.KM_UINT) && (tagType != android.security.keymaster.KeymasterDefs.KM_UINT_REP)) {
            throw new java.lang.IllegalArgumentException("Not an int or repeating int tag: " + tag);
        }
        // Keymaster's KM_UINT is unsigned 32 bit.
        if ((value < 0) || (value > android.security.keymaster.KeymasterArguments.UINT32_MAX_VALUE)) {
            throw new java.lang.IllegalArgumentException("Int tag value out of range: " + value);
        }
        mArguments.add(new android.security.keymaster.KeymasterIntArgument(tag, ((int) (value))));
    }

    /**
     * Returns the value of the specified unsigned 32-bit int tag or {@code defaultValue} if the tag
     * is not present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an unsigned 32-bit int tag.
     */
    public long getUnsignedInt(int tag, long defaultValue) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_UINT) {
            throw new java.lang.IllegalArgumentException("Not an int tag: " + tag);
        }
        android.security.keymaster.KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        // Keymaster's KM_UINT is unsigned 32 bit.
        return ((android.security.keymaster.KeymasterIntArgument) (arg)).value & 0xffffffffL;
    }

    /**
     * Adds an unsigned 64-bit long tag with the provided value.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not an unsigned 64-bit long tag or if
     * 		{@code value} is outside of the permitted range [0; 2^64).
     */
    public void addUnsignedLong(int tag, java.math.BigInteger value) {
        int tagType = android.security.keymaster.KeymasterDefs.getTagType(tag);
        if ((tagType != android.security.keymaster.KeymasterDefs.KM_ULONG) && (tagType != android.security.keymaster.KeymasterDefs.KM_ULONG_REP)) {
            throw new java.lang.IllegalArgumentException("Not a long or repeating long tag: " + tag);
        }
        addLongTag(tag, value);
    }

    /**
     * Returns all values of the specified repeating unsigned 64-bit long tag.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a repeating unsigned 64-bit long tag.
     */
    public java.util.List<java.math.BigInteger> getUnsignedLongs(int tag) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_ULONG_REP) {
            throw new java.lang.IllegalArgumentException("Tag is not a repeating long: " + tag);
        }
        java.util.List<java.math.BigInteger> values = new java.util.ArrayList<java.math.BigInteger>();
        for (android.security.keymaster.KeymasterArgument arg : mArguments) {
            if (arg.tag == tag) {
                values.add(getLongTagValue(arg));
            }
        }
        return values;
    }

    private void addLongTag(int tag, java.math.BigInteger value) {
        // Keymaster's KM_ULONG is unsigned 64 bit.
        if ((value.signum() == (-1)) || (value.compareTo(android.security.keymaster.KeymasterArguments.UINT64_MAX_VALUE) > 0)) {
            throw new java.lang.IllegalArgumentException("Long tag value out of range: " + value);
        }
        mArguments.add(new android.security.keymaster.KeymasterLongArgument(tag, value.longValue()));
    }

    private java.math.BigInteger getLongTagValue(android.security.keymaster.KeymasterArgument arg) {
        // Keymaster's KM_ULONG is unsigned 64 bit. We're forced to use BigInteger for type safety
        // because there's no unsigned long type.
        return android.security.keymaster.KeymasterArguments.toUint64(((android.security.keymaster.KeymasterLongArgument) (arg)).value);
    }

    /**
     * Adds the provided boolean tag. Boolean tags are considered to be set to {@code true} if
     * present and {@code false} if absent.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a boolean tag.
     */
    public void addBoolean(int tag) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_BOOL) {
            throw new java.lang.IllegalArgumentException("Not a boolean tag: " + tag);
        }
        mArguments.add(new android.security.keymaster.KeymasterBooleanArgument(tag));
    }

    /**
     * Returns {@code true} if the provided boolean tag is present, {@code false} if absent.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a boolean tag.
     */
    public boolean getBoolean(int tag) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_BOOL) {
            throw new java.lang.IllegalArgumentException("Not a boolean tag: " + tag);
        }
        android.security.keymaster.KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return false;
        }
        return true;
    }

    /**
     * Adds a bytes tag with the provided value.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a bytes tag.
     */
    public void addBytes(int tag, byte[] value) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_BYTES) {
            throw new java.lang.IllegalArgumentException("Not a bytes tag: " + tag);
        }
        if (value == null) {
            throw new java.lang.NullPointerException("value == nulll");
        }
        mArguments.add(new android.security.keymaster.KeymasterBlobArgument(tag, value));
    }

    /**
     * Returns the value of the specified bytes tag or {@code defaultValue} if the tag is not
     * present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a bytes tag.
     */
    public byte[] getBytes(int tag, byte[] defaultValue) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_BYTES) {
            throw new java.lang.IllegalArgumentException("Not a bytes tag: " + tag);
        }
        android.security.keymaster.KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((android.security.keymaster.KeymasterBlobArgument) (arg)).blob;
    }

    /**
     * Adds a date tag with the provided value.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a date tag or if {@code value} is
     * 		before the start of Unix epoch.
     */
    public void addDate(int tag, java.util.Date value) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_DATE) {
            throw new java.lang.IllegalArgumentException("Not a date tag: " + tag);
        }
        if (value == null) {
            throw new java.lang.NullPointerException("value == nulll");
        }
        // Keymaster's KM_DATE is unsigned, but java.util.Date is signed, thus preventing us from
        // using values larger than 2^63 - 1.
        if (value.getTime() < 0) {
            throw new java.lang.IllegalArgumentException("Date tag value out of range: " + value);
        }
        mArguments.add(new android.security.keymaster.KeymasterDateArgument(tag, value));
    }

    /**
     * Adds a date tag with the provided value, if the value is not {@code null}. Does nothing if
     * the {@code value} is null.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a date tag or if {@code value} is
     * 		before the start of Unix epoch.
     */
    public void addDateIfNotNull(int tag, java.util.Date value) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_DATE) {
            throw new java.lang.IllegalArgumentException("Not a date tag: " + tag);
        }
        if (value != null) {
            addDate(tag, value);
        }
    }

    /**
     * Returns the value of the specified date tag or {@code defaultValue} if the tag is not
     * present.
     *
     * @throws IllegalArgumentException
     * 		if {@code tag} is not a date tag or if the tag's value
     * 		represents a time instant which is after {@code 2^63 - 1} milliseconds since Unix
     * 		epoch.
     */
    public java.util.Date getDate(int tag, java.util.Date defaultValue) {
        if (android.security.keymaster.KeymasterDefs.getTagType(tag) != android.security.keymaster.KeymasterDefs.KM_DATE) {
            throw new java.lang.IllegalArgumentException("Tag is not a date type: " + tag);
        }
        android.security.keymaster.KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        java.util.Date result = ((android.security.keymaster.KeymasterDateArgument) (arg)).date;
        // Keymaster's KM_DATE is unsigned, but java.util.Date is signed, thus preventing us from
        // using values larger than 2^63 - 1.
        if (result.getTime() < 0) {
            throw new java.lang.IllegalArgumentException("Tag value too large. Tag: " + tag);
        }
        return result;
    }

    private android.security.keymaster.KeymasterArgument getArgumentByTag(int tag) {
        for (android.security.keymaster.KeymasterArgument arg : mArguments) {
            if (arg.tag == tag) {
                return arg;
            }
        }
        return null;
    }

    public boolean containsTag(int tag) {
        return getArgumentByTag(tag) != null;
    }

    public int size() {
        return mArguments.size();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeTypedList(mArguments);
    }

    public void readFromParcel(android.os.Parcel in) {
        in.readTypedList(mArguments, android.security.keymaster.KeymasterArgument.CREATOR);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Converts the provided value to non-negative {@link BigInteger}, treating the sign bit of the
     * provided value as the most significant bit of the result.
     */
    public static java.math.BigInteger toUint64(long value) {
        if (value >= 0) {
            return java.math.BigInteger.valueOf(value);
        } else {
            return java.math.BigInteger.valueOf(value).add(android.security.keymaster.KeymasterArguments.UINT64_RANGE);
        }
    }
}

