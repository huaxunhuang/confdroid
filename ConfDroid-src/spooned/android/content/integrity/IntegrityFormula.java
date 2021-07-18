/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * Represents a rule logic/content.
 *
 * @unknown 
 */
@android.annotation.SystemApi
@android.annotation.TestApi
@com.android.internal.annotations.VisibleForTesting
public abstract class IntegrityFormula {
    /**
     * Factory class for creating integrity formulas based on the app being installed.
     */
    public static final class Application {
        /**
         * Returns an integrity formula that checks the equality to a package name.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula packageNameEquals(@android.annotation.NonNull
        java.lang.String packageName) {
            return new android.content.integrity.AtomicFormula.StringAtomicFormula(android.content.integrity.AtomicFormula.PACKAGE_NAME, packageName);
        }

        /**
         * Returns an integrity formula that checks if the app certificates contain {@code appCertificate}.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula certificatesContain(@android.annotation.NonNull
        java.lang.String appCertificate) {
            return new android.content.integrity.AtomicFormula.StringAtomicFormula(android.content.integrity.AtomicFormula.APP_CERTIFICATE, appCertificate);
        }

        /**
         * Returns an integrity formula that checks the equality to a version code.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula versionCodeEquals(@android.annotation.NonNull
        long versionCode) {
            return new android.content.integrity.AtomicFormula.LongAtomicFormula(android.content.integrity.AtomicFormula.VERSION_CODE, android.content.integrity.AtomicFormula.EQ, versionCode);
        }

        /**
         * Returns an integrity formula that checks the app's version code is greater than the
         * provided value.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula versionCodeGreaterThan(@android.annotation.NonNull
        long versionCode) {
            return new android.content.integrity.AtomicFormula.LongAtomicFormula(android.content.integrity.AtomicFormula.VERSION_CODE, android.content.integrity.AtomicFormula.GT, versionCode);
        }

        /**
         * Returns an integrity formula that checks the app's version code is greater than or equal
         * to the provided value.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula versionCodeGreaterThanOrEqualTo(@android.annotation.NonNull
        long versionCode) {
            return new android.content.integrity.AtomicFormula.LongAtomicFormula(android.content.integrity.AtomicFormula.VERSION_CODE, android.content.integrity.AtomicFormula.GTE, versionCode);
        }

        /**
         * Returns an integrity formula that is valid when app is pre-installed.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula isPreInstalled() {
            return new android.content.integrity.AtomicFormula.BooleanAtomicFormula(android.content.integrity.AtomicFormula.PRE_INSTALLED, true);
        }

        private Application() {
        }
    }

    /**
     * Factory class for creating integrity formulas based on installer.
     */
    public static final class Installer {
        /**
         * Returns an integrity formula that checks the equality to an installer name.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula packageNameEquals(@android.annotation.NonNull
        java.lang.String installerName) {
            return new android.content.integrity.AtomicFormula.StringAtomicFormula(android.content.integrity.AtomicFormula.INSTALLER_NAME, installerName);
        }

        /**
         * An static formula that evaluates to true if the installer is NOT allowed according to the
         * "allowed installer" field in the android manifest.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula notAllowedByManifest() {
            return android.content.integrity.IntegrityFormula.not(new android.content.integrity.InstallerAllowedByManifestFormula());
        }

        /**
         * Returns an integrity formula that checks if the installer certificates contain {@code installerCertificate}.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula certificatesContain(@android.annotation.NonNull
        java.lang.String installerCertificate) {
            return new android.content.integrity.AtomicFormula.StringAtomicFormula(android.content.integrity.AtomicFormula.INSTALLER_CERTIFICATE, installerCertificate);
        }

        private Installer() {
        }
    }

    /**
     * Factory class for creating integrity formulas based on source stamp.
     */
    public static final class SourceStamp {
        /**
         * Returns an integrity formula that checks the equality to a stamp certificate hash.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula stampCertificateHashEquals(@android.annotation.NonNull
        java.lang.String stampCertificateHash) {
            return new android.content.integrity.AtomicFormula.StringAtomicFormula(android.content.integrity.AtomicFormula.STAMP_CERTIFICATE_HASH, stampCertificateHash);
        }

        /**
         * Returns an integrity formula that is valid when stamp embedded in the APK is NOT trusted.
         */
        @android.annotation.NonNull
        public static android.content.integrity.IntegrityFormula notTrusted() {
            return /* value= */
            new android.content.integrity.AtomicFormula.BooleanAtomicFormula(android.content.integrity.AtomicFormula.STAMP_TRUSTED, false);
        }

        private SourceStamp() {
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.content.integrity.IntegrityFormula.COMPOUND_FORMULA_TAG, android.content.integrity.IntegrityFormula.STRING_ATOMIC_FORMULA_TAG, android.content.integrity.IntegrityFormula.LONG_ATOMIC_FORMULA_TAG, android.content.integrity.IntegrityFormula.BOOLEAN_ATOMIC_FORMULA_TAG, android.content.integrity.IntegrityFormula.INSTALLER_ALLOWED_BY_MANIFEST_FORMULA_TAG })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface Tag {}

    /**
     *
     *
     * @unknown 
     */
    public static final int COMPOUND_FORMULA_TAG = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int STRING_ATOMIC_FORMULA_TAG = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int LONG_ATOMIC_FORMULA_TAG = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int BOOLEAN_ATOMIC_FORMULA_TAG = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int INSTALLER_ALLOWED_BY_MANIFEST_FORMULA_TAG = 4;

    /**
     * Returns the tag that identifies the current class.
     *
     * @unknown 
     */
    @android.content.integrity.IntegrityFormula.Tag
    public abstract int getTag();

    /**
     * Returns true when the integrity formula is satisfied by the {@code appInstallMetadata}.
     *
     * @unknown 
     */
    public abstract boolean matches(android.content.integrity.AppInstallMetadata appInstallMetadata);

    /**
     * Returns true when the formula (or one of its atomic formulas) has app certificate as key.
     *
     * @unknown 
     */
    public abstract boolean isAppCertificateFormula();

    /**
     * Returns true when the formula (or one of its atomic formulas) has installer package name or
     * installer certificate as key.
     *
     * @unknown 
     */
    public abstract boolean isInstallerFormula();

    /**
     * Write an {@link IntegrityFormula} to {@link android.os.Parcel}.
     *
     * <p>This helper method is needed because non-final class/interface are not allowed to be
     * {@link Parcelable}.
     *
     * @throws IllegalArgumentException
     * 		if {@link IntegrityFormula} is not a recognized subclass
     * @unknown 
     */
    public static void writeToParcel(@android.annotation.NonNull
    android.content.integrity.IntegrityFormula formula, @android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        dest.writeInt(formula.getTag());
        ((android.os.Parcelable) (formula)).writeToParcel(dest, flags);
    }

    /**
     * Read a {@link IntegrityFormula} from a {@link android.os.Parcel}.
     *
     * <p>We need this (hacky) helper method because non-final class/interface cannot be {@link Parcelable} (api lint error).
     *
     * @throws IllegalArgumentException
     * 		if the parcel cannot be parsed
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.content.integrity.IntegrityFormula readFromParcel(@android.annotation.NonNull
    android.os.Parcel in) {
        int tag = in.readInt();
        switch (tag) {
            case android.content.integrity.IntegrityFormula.COMPOUND_FORMULA_TAG :
                return android.content.integrity.CompoundFormula.CREATOR.createFromParcel(in);
            case android.content.integrity.IntegrityFormula.STRING_ATOMIC_FORMULA_TAG :
                return android.content.integrity.AtomicFormula.StringAtomicFormula.CREATOR.createFromParcel(in);
            case android.content.integrity.IntegrityFormula.LONG_ATOMIC_FORMULA_TAG :
                return android.content.integrity.AtomicFormula.LongAtomicFormula.CREATOR.createFromParcel(in);
            case android.content.integrity.IntegrityFormula.BOOLEAN_ATOMIC_FORMULA_TAG :
                return android.content.integrity.AtomicFormula.BooleanAtomicFormula.CREATOR.createFromParcel(in);
            case android.content.integrity.IntegrityFormula.INSTALLER_ALLOWED_BY_MANIFEST_FORMULA_TAG :
                return android.content.integrity.InstallerAllowedByManifestFormula.CREATOR.createFromParcel(in);
            default :
                throw new java.lang.IllegalArgumentException("Unknown formula tag " + tag);
        }
    }

    /**
     * Returns a formula that evaluates to true when any formula in {@code formulae} evaluates to
     * true.
     *
     * <p>Throws an {@link IllegalArgumentException} if formulae has less than two elements.
     */
    @android.annotation.NonNull
    public static android.content.integrity.IntegrityFormula any(@android.annotation.NonNull
    android.content.integrity.IntegrityFormula... formulae) {
        return new android.content.integrity.CompoundFormula(android.content.integrity.CompoundFormula.OR, java.util.Arrays.asList(formulae));
    }

    /**
     * Returns a formula that evaluates to true when all formula in {@code formulae} evaluates to
     * true.
     *
     * <p>Throws an {@link IllegalArgumentException} if formulae has less than two elements.
     */
    @android.annotation.NonNull
    public static android.content.integrity.IntegrityFormula all(@android.annotation.NonNull
    android.content.integrity.IntegrityFormula... formulae) {
        return new android.content.integrity.CompoundFormula(android.content.integrity.CompoundFormula.AND, java.util.Arrays.asList(formulae));
    }

    /**
     * Returns a formula that evaluates to true when {@code formula} evaluates to false.
     */
    @android.annotation.NonNull
    public static android.content.integrity.IntegrityFormula not(@android.annotation.NonNull
    android.content.integrity.IntegrityFormula formula) {
        return new android.content.integrity.CompoundFormula(android.content.integrity.CompoundFormula.NOT, java.util.Arrays.asList(formula));
    }

    // Constructor is package private so it cannot be inherited outside of this package.
    IntegrityFormula() {
    }
}

