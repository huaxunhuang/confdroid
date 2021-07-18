/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.integrity;


/**
 * Represents a simple formula consisting of an app install metadata field and a value.
 *
 * <p>Instances of this class are immutable.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting
public abstract class AtomicFormula extends android.content.integrity.IntegrityFormula {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.content.integrity.AtomicFormula.PACKAGE_NAME, android.content.integrity.AtomicFormula.APP_CERTIFICATE, android.content.integrity.AtomicFormula.INSTALLER_NAME, android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE, android.content.integrity.AtomicFormula.VERSION_CODE, android.content.integrity.AtomicFormula.PRE_INSTALLED, android.content.integrity.AtomicFormula.STAMP_TRUSTED, android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Key {}

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.content.integrity.AtomicFormula.EQ, android.content.integrity.AtomicFormula.GT, android.content.integrity.AtomicFormula.GTE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Operator {}

    /**
     * Package name of the app.
     *
     * <p>Can only be used in {@link StringAtomicFormula}.
     */
    public static final int PACKAGE_NAME = 0;

    /**
     * SHA-256 of the app certificate of the app.
     *
     * <p>Can only be used in {@link StringAtomicFormula}.
     */
    public static final int APP_CERTIFICATE = 1;

    /**
     * Package name of the installer. Will be empty string if installed by the system (e.g., adb).
     *
     * <p>Can only be used in {@link StringAtomicFormula}.
     */
    public static final int INSTALLER_NAME = 2;

    /**
     * SHA-256 of the cert of the installer. Will be empty string if installed by the system (e.g.,
     * adb).
     *
     * <p>Can only be used in {@link StringAtomicFormula}.
     */
    public static final int INSTALLER_CERTIFICATE = 3;

    /**
     * Version code of the app.
     *
     * <p>Can only be used in {@link LongAtomicFormula}.
     */
    public static final int VERSION_CODE = 4;

    /**
     * If the app is pre-installed on the device.
     *
     * <p>Can only be used in {@link BooleanAtomicFormula}.
     */
    public static final int PRE_INSTALLED = 5;

    /**
     * If the APK has an embedded trusted stamp.
     *
     * <p>Can only be used in {@link BooleanAtomicFormula}.
     */
    public static final int STAMP_TRUSTED = 6;

    /**
     * SHA-256 of the certificate used to sign the stamp embedded in the APK.
     *
     * <p>Can only be used in {@link StringAtomicFormula}.
     */
    public static final int STAMP_CERTIFICATE_HASH = 7;

    public static final int EQ = 0;

    public static final int GT = 1;

    public static final int GTE = 2;

    @android.content.integrity.AtomicFormula.Key
    private final int mKey;

    public AtomicFormula(@android.content.integrity.AtomicFormula.Key
    int key) {
        checkArgument(android.content.integrity.AtomicFormula.isValidKey(key), java.lang.String.format("Unknown key: %d", key));
        mKey = key;
    }

    /**
     * An {@link AtomicFormula} with an key and long value.
     */
    public static final class LongAtomicFormula extends android.content.integrity.AtomicFormula implements android.os.Parcelable {
        private final java.lang.Long mValue;

        @android.content.integrity.AtomicFormula.Operator
        private final java.lang.Integer mOperator;

        /**
         * Constructs an empty {@link LongAtomicFormula}. This should only be used as a base.
         *
         * <p>This formula will always return false.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with long value
         */
        public LongAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key) {
            super(key);
            checkArgument(key == android.content.integrity.AtomicFormula.VERSION_CODE, java.lang.String.format("Key %s cannot be used with LongAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            mValue = null;
            mOperator = null;
        }

        /**
         * Constructs a new {@link LongAtomicFormula}.
         *
         * <p>This formula will hold if and only if the corresponding information of an install
         * specified by {@code key} is of the correct relationship to {@code value} as specified by
         * {@code operator}.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with long value
         */
        public LongAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key, @android.content.integrity.AtomicFormula.Operator
        int operator, long value) {
            super(key);
            checkArgument(key == android.content.integrity.AtomicFormula.VERSION_CODE, java.lang.String.format("Key %s cannot be used with LongAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            checkArgument(android.content.integrity.AtomicFormula.LongAtomicFormula.isValidOperator(operator), java.lang.String.format("Unknown operator: %d", operator));
            mOperator = operator;
            mValue = value;
        }

        LongAtomicFormula(android.os.Parcel in) {
            super(in.readInt());
            mValue = in.readLong();
            mOperator = in.readInt();
        }

        @android.annotation.NonNull
        public static final android.content.integrity.Creator<android.content.integrity.AtomicFormula.LongAtomicFormula> CREATOR = new android.content.integrity.Creator<android.content.integrity.AtomicFormula.LongAtomicFormula>() {
            @java.lang.Override
            public android.content.integrity.AtomicFormula.LongAtomicFormula createFromParcel(android.os.Parcel in) {
                return new android.content.integrity.AtomicFormula.LongAtomicFormula(in);
            }

            @java.lang.Override
            public android.content.integrity.AtomicFormula.LongAtomicFormula[] newArray(int size) {
                return new android.content.integrity.AtomicFormula.LongAtomicFormula[size];
            }
        };

        @java.lang.Override
        public int getTag() {
            return android.content.integrity.IntegrityFormula.LONG_ATOMIC_FORMULA_TAG;
        }

        @java.lang.Override
        public boolean matches(android.content.integrity.AppInstallMetadata appInstallMetadata) {
            if ((mValue == null) || (mOperator == null)) {
                return false;
            }
            long metadataValue = android.content.integrity.AtomicFormula.LongAtomicFormula.getLongMetadataValue(appInstallMetadata, getKey());
            switch (mOperator) {
                case android.content.integrity.AtomicFormula.EQ :
                    return metadataValue == mValue;
                case android.content.integrity.AtomicFormula.GT :
                    return metadataValue > mValue;
                case android.content.integrity.AtomicFormula.GTE :
                    return metadataValue >= mValue;
                default :
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected operator %d", mOperator));
            }
        }

        @java.lang.Override
        public boolean isAppCertificateFormula() {
            return false;
        }

        @java.lang.Override
        public boolean isInstallerFormula() {
            return false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            if ((mValue == null) || (mOperator == null)) {
                return java.lang.String.format("(%s)", android.content.integrity.AtomicFormula.keyToString(getKey()));
            }
            return java.lang.String.format("(%s %s %s)", android.content.integrity.AtomicFormula.keyToString(getKey()), android.content.integrity.AtomicFormula.operatorToString(mOperator), mValue);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.content.integrity.AtomicFormula.LongAtomicFormula that = ((android.content.integrity.AtomicFormula.LongAtomicFormula) (o));
            return ((getKey() == that.getKey()) && (mValue == that.mValue)) && (mOperator == that.mOperator);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(getKey(), mOperator, mValue);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(@android.annotation.NonNull
        android.os.Parcel dest, int flags) {
            if ((mValue == null) || (mOperator == null)) {
                throw new java.lang.IllegalStateException("Cannot write an empty LongAtomicFormula.");
            }
            dest.writeInt(getKey());
            dest.writeLong(mValue);
            dest.writeInt(mOperator);
        }

        public java.lang.Long getValue() {
            return mValue;
        }

        public java.lang.Integer getOperator() {
            return mOperator;
        }

        private static boolean isValidOperator(int operator) {
            return ((operator == android.content.integrity.AtomicFormula.EQ) || (operator == android.content.integrity.AtomicFormula.GT)) || (operator == android.content.integrity.AtomicFormula.GTE);
        }

        private static long getLongMetadataValue(android.content.integrity.AppInstallMetadata appInstallMetadata, int key) {
            switch (key) {
                case android.content.integrity.AtomicFormula.VERSION_CODE :
                    return appInstallMetadata.getVersionCode();
                default :
                    throw new java.lang.IllegalStateException("Unexpected key in IntAtomicFormula" + key);
            }
        }
    }

    /**
     * An {@link AtomicFormula} with a key and string value.
     */
    public static final class StringAtomicFormula extends android.content.integrity.AtomicFormula implements android.os.Parcelable {
        private final java.lang.String mValue;

        // Indicates whether the value is the actual value or the hashed value.
        private final java.lang.Boolean mIsHashedValue;

        /**
         * Constructs an empty {@link StringAtomicFormula}. This should only be used as a base.
         *
         * <p>An empty formula will always match to false.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with string value
         */
        public StringAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key) {
            super(key);
            checkArgument(((((key == android.content.integrity.AtomicFormula.PACKAGE_NAME) || (key == android.content.integrity.AtomicFormula.APP_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_NAME)) || (key == android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH), java.lang.String.format("Key %s cannot be used with StringAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            mValue = null;
            mIsHashedValue = null;
        }

        /**
         * Constructs a new {@link StringAtomicFormula}.
         *
         * <p>This formula will hold if and only if the corresponding information of an install
         * specified by {@code key} equals {@code value}.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with string value
         */
        public StringAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key, @android.annotation.NonNull
        java.lang.String value, boolean isHashed) {
            super(key);
            checkArgument(((((key == android.content.integrity.AtomicFormula.PACKAGE_NAME) || (key == android.content.integrity.AtomicFormula.APP_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_NAME)) || (key == android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH), java.lang.String.format("Key %s cannot be used with StringAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            mValue = value;
            mIsHashedValue = isHashed;
        }

        /**
         * Constructs a new {@link StringAtomicFormula} together with handling the necessary hashing
         * for the given key.
         *
         * <p>The value will be automatically hashed with SHA256 and the hex digest will be computed
         * when the key is PACKAGE_NAME or INSTALLER_NAME and the value is more than 32 characters.
         *
         * <p>The APP_CERTIFICATES, INSTALLER_CERTIFICATES, and STAMP_CERTIFICATE_HASH are always
         * delivered in hashed form. So the isHashedValue is set to true by default.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with string value.
         */
        public StringAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key, @android.annotation.NonNull
        java.lang.String value) {
            super(key);
            checkArgument(((((key == android.content.integrity.AtomicFormula.PACKAGE_NAME) || (key == android.content.integrity.AtomicFormula.APP_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_NAME)) || (key == android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH), java.lang.String.format("Key %s cannot be used with StringAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            mValue = android.content.integrity.AtomicFormula.StringAtomicFormula.hashValue(key, value);
            mIsHashedValue = (((key == android.content.integrity.AtomicFormula.APP_CERTIFICATE) || (key == android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH)) || (!mValue.equals(value));
        }

        StringAtomicFormula(android.os.Parcel in) {
            super(in.readInt());
            mValue = in.readStringNoHelper();
            mIsHashedValue = in.readByte() != 0;
        }

        @android.annotation.NonNull
        public static final android.content.integrity.Creator<android.content.integrity.AtomicFormula.StringAtomicFormula> CREATOR = new android.content.integrity.Creator<android.content.integrity.AtomicFormula.StringAtomicFormula>() {
            @java.lang.Override
            public android.content.integrity.AtomicFormula.StringAtomicFormula createFromParcel(android.os.Parcel in) {
                return new android.content.integrity.AtomicFormula.StringAtomicFormula(in);
            }

            @java.lang.Override
            public android.content.integrity.AtomicFormula.StringAtomicFormula[] newArray(int size) {
                return new android.content.integrity.AtomicFormula.StringAtomicFormula[size];
            }
        };

        @java.lang.Override
        public int getTag() {
            return android.content.integrity.IntegrityFormula.STRING_ATOMIC_FORMULA_TAG;
        }

        @java.lang.Override
        public boolean matches(android.content.integrity.AppInstallMetadata appInstallMetadata) {
            if ((mValue == null) || (mIsHashedValue == null)) {
                return false;
            }
            return android.content.integrity.AtomicFormula.StringAtomicFormula.getMetadataValue(appInstallMetadata, getKey()).contains(mValue);
        }

        @java.lang.Override
        public boolean isAppCertificateFormula() {
            return getKey() == android.content.integrity.AtomicFormula.APP_CERTIFICATE;
        }

        @java.lang.Override
        public boolean isInstallerFormula() {
            return (getKey() == android.content.integrity.AtomicFormula.INSTALLER_NAME) || (getKey() == android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE);
        }

        @java.lang.Override
        public java.lang.String toString() {
            if ((mValue == null) || (mIsHashedValue == null)) {
                return java.lang.String.format("(%s)", android.content.integrity.AtomicFormula.keyToString(getKey()));
            }
            return java.lang.String.format("(%s %s %s)", android.content.integrity.AtomicFormula.keyToString(getKey()), android.content.integrity.AtomicFormula.operatorToString(android.content.integrity.AtomicFormula.EQ), mValue);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.content.integrity.AtomicFormula.StringAtomicFormula that = ((android.content.integrity.AtomicFormula.StringAtomicFormula) (o));
            return (getKey() == that.getKey()) && java.util.Objects.equals(mValue, that.mValue);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(getKey(), mValue);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(@android.annotation.NonNull
        android.os.Parcel dest, int flags) {
            if ((mValue == null) || (mIsHashedValue == null)) {
                throw new java.lang.IllegalStateException("Cannot write an empty StringAtomicFormula.");
            }
            dest.writeInt(getKey());
            dest.writeStringNoHelper(mValue);
            dest.writeByte(((byte) (mIsHashedValue ? 1 : 0)));
        }

        public java.lang.String getValue() {
            return mValue;
        }

        public java.lang.Boolean getIsHashedValue() {
            return mIsHashedValue;
        }

        private static java.util.List<java.lang.String> getMetadataValue(android.content.integrity.AppInstallMetadata appInstallMetadata, int key) {
            switch (key) {
                case android.content.integrity.AtomicFormula.PACKAGE_NAME :
                    return java.util.Collections.singletonList(appInstallMetadata.getPackageName());
                case android.content.integrity.AtomicFormula.APP_CERTIFICATE :
                    return appInstallMetadata.getAppCertificates();
                case android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE :
                    return appInstallMetadata.getInstallerCertificates();
                case android.content.integrity.AtomicFormula.INSTALLER_NAME :
                    return java.util.Collections.singletonList(appInstallMetadata.getInstallerName());
                case android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH :
                    return java.util.Collections.singletonList(appInstallMetadata.getStampCertificateHash());
                default :
                    throw new java.lang.IllegalStateException("Unexpected key in StringAtomicFormula: " + key);
            }
        }

        private static java.lang.String hashValue(@android.content.integrity.AtomicFormula.Key
        int key, java.lang.String value) {
            // Hash the string value if it is a PACKAGE_NAME or INSTALLER_NAME and the value is
            // greater than 32 characters.
            if (value.length() > 32) {
                if ((key == android.content.integrity.AtomicFormula.PACKAGE_NAME) || (key == android.content.integrity.AtomicFormula.INSTALLER_NAME)) {
                    return android.content.integrity.AtomicFormula.StringAtomicFormula.hash(value);
                }
            }
            return value;
        }

        private static java.lang.String hash(java.lang.String value) {
            try {
                java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = messageDigest.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                return android.content.integrity.IntegrityUtils.getHexDigest(hashBytes);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.lang.RuntimeException("SHA-256 algorithm not found", e);
            }
        }
    }

    /**
     * An {@link AtomicFormula} with a key and boolean value.
     */
    public static final class BooleanAtomicFormula extends android.content.integrity.AtomicFormula implements android.os.Parcelable {
        private final java.lang.Boolean mValue;

        /**
         * Constructs an empty {@link BooleanAtomicFormula}. This should only be used as a base.
         *
         * <p>An empty formula will always match to false.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with boolean value
         */
        public BooleanAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key) {
            super(key);
            checkArgument((key == android.content.integrity.AtomicFormula.PRE_INSTALLED) || (key == android.content.integrity.AtomicFormula.STAMP_TRUSTED), java.lang.String.format("Key %s cannot be used with BooleanAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            mValue = null;
        }

        /**
         * Constructs a new {@link BooleanAtomicFormula}.
         *
         * <p>This formula will hold if and only if the corresponding information of an install
         * specified by {@code key} equals {@code value}.
         *
         * @throws IllegalArgumentException
         * 		if {@code key} cannot be used with boolean value
         */
        public BooleanAtomicFormula(@android.content.integrity.AtomicFormula.Key
        int key, boolean value) {
            super(key);
            checkArgument((key == android.content.integrity.AtomicFormula.PRE_INSTALLED) || (key == android.content.integrity.AtomicFormula.STAMP_TRUSTED), java.lang.String.format("Key %s cannot be used with BooleanAtomicFormula", android.content.integrity.AtomicFormula.keyToString(key)));
            mValue = value;
        }

        BooleanAtomicFormula(android.os.Parcel in) {
            super(in.readInt());
            mValue = in.readByte() != 0;
        }

        @android.annotation.NonNull
        public static final android.content.integrity.Creator<android.content.integrity.AtomicFormula.BooleanAtomicFormula> CREATOR = new android.content.integrity.Creator<android.content.integrity.AtomicFormula.BooleanAtomicFormula>() {
            @java.lang.Override
            public android.content.integrity.AtomicFormula.BooleanAtomicFormula createFromParcel(android.os.Parcel in) {
                return new android.content.integrity.AtomicFormula.BooleanAtomicFormula(in);
            }

            @java.lang.Override
            public android.content.integrity.AtomicFormula.BooleanAtomicFormula[] newArray(int size) {
                return new android.content.integrity.AtomicFormula.BooleanAtomicFormula[size];
            }
        };

        @java.lang.Override
        public int getTag() {
            return android.content.integrity.IntegrityFormula.BOOLEAN_ATOMIC_FORMULA_TAG;
        }

        @java.lang.Override
        public boolean matches(android.content.integrity.AppInstallMetadata appInstallMetadata) {
            if (mValue == null) {
                return false;
            }
            return android.content.integrity.AtomicFormula.BooleanAtomicFormula.getBooleanMetadataValue(appInstallMetadata, getKey()) == mValue;
        }

        @java.lang.Override
        public boolean isAppCertificateFormula() {
            return false;
        }

        @java.lang.Override
        public boolean isInstallerFormula() {
            return false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            if (mValue == null) {
                return java.lang.String.format("(%s)", android.content.integrity.AtomicFormula.keyToString(getKey()));
            }
            return java.lang.String.format("(%s %s %s)", android.content.integrity.AtomicFormula.keyToString(getKey()), android.content.integrity.AtomicFormula.operatorToString(android.content.integrity.AtomicFormula.EQ), mValue);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.content.integrity.AtomicFormula.BooleanAtomicFormula that = ((android.content.integrity.AtomicFormula.BooleanAtomicFormula) (o));
            return (getKey() == that.getKey()) && (mValue == that.mValue);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(getKey(), mValue);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(@android.annotation.NonNull
        android.os.Parcel dest, int flags) {
            if (mValue == null) {
                throw new java.lang.IllegalStateException("Cannot write an empty BooleanAtomicFormula.");
            }
            dest.writeInt(getKey());
            dest.writeByte(((byte) (mValue ? 1 : 0)));
        }

        public java.lang.Boolean getValue() {
            return mValue;
        }

        private static boolean getBooleanMetadataValue(android.content.integrity.AppInstallMetadata appInstallMetadata, int key) {
            switch (key) {
                case android.content.integrity.AtomicFormula.PRE_INSTALLED :
                    return appInstallMetadata.isPreInstalled();
                case android.content.integrity.AtomicFormula.STAMP_TRUSTED :
                    return appInstallMetadata.isStampTrusted();
                default :
                    throw new java.lang.IllegalStateException("Unexpected key in BooleanAtomicFormula: " + key);
            }
        }
    }

    public int getKey() {
        return mKey;
    }

    static java.lang.String keyToString(int key) {
        switch (key) {
            case android.content.integrity.AtomicFormula.PACKAGE_NAME :
                return "PACKAGE_NAME";
            case android.content.integrity.AtomicFormula.APP_CERTIFICATE :
                return "APP_CERTIFICATE";
            case android.content.integrity.AtomicFormula.VERSION_CODE :
                return "VERSION_CODE";
            case android.content.integrity.AtomicFormula.INSTALLER_NAME :
                return "INSTALLER_NAME";
            case android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE :
                return "INSTALLER_CERTIFICATE";
            case android.content.integrity.AtomicFormula.PRE_INSTALLED :
                return "PRE_INSTALLED";
            case android.content.integrity.AtomicFormula.STAMP_TRUSTED :
                return "STAMP_TRUSTED";
            case android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH :
                return "STAMP_CERTIFICATE_HASH";
            default :
                throw new java.lang.IllegalArgumentException("Unknown key " + key);
        }
    }

    static java.lang.String operatorToString(int op) {
        switch (op) {
            case android.content.integrity.AtomicFormula.EQ :
                return "EQ";
            case android.content.integrity.AtomicFormula.GT :
                return "GT";
            case android.content.integrity.AtomicFormula.GTE :
                return "GTE";
            default :
                throw new java.lang.IllegalArgumentException("Unknown operator " + op);
        }
    }

    private static boolean isValidKey(int key) {
        return (((((((key == android.content.integrity.AtomicFormula.PACKAGE_NAME) || (key == android.content.integrity.AtomicFormula.APP_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.VERSION_CODE)) || (key == android.content.integrity.AtomicFormula.INSTALLER_NAME)) || (key == android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE)) || (key == android.content.integrity.AtomicFormula.PRE_INSTALLED)) || (key == android.content.integrity.AtomicFormula.STAMP_TRUSTED)) || (key == android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH);
    }
}

