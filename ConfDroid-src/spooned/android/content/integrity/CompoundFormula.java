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
 * Represents a compound formula formed by joining other simple and complex formulas with boolean
 * connectors.
 *
 * <p>Instances of this class are immutable.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting
public final class CompoundFormula extends android.content.integrity.IntegrityFormula implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.content.integrity.CompoundFormula.AND, android.content.integrity.CompoundFormula.OR, android.content.integrity.CompoundFormula.NOT })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Connector {}

    /**
     * Boolean AND operator.
     */
    public static final int AND = 0;

    /**
     * Boolean OR operator.
     */
    public static final int OR = 1;

    /**
     * Boolean NOT operator.
     */
    public static final int NOT = 2;

    @android.content.integrity.CompoundFormula.Connector
    private final int mConnector;

    @android.annotation.NonNull
    private final java.util.List<android.content.integrity.IntegrityFormula> mFormulas;

    @android.annotation.NonNull
    public static final android.content.integrity.Creator<android.content.integrity.CompoundFormula> CREATOR = new android.content.integrity.Creator<android.content.integrity.CompoundFormula>() {
        @java.lang.Override
        public android.content.integrity.CompoundFormula createFromParcel(android.os.Parcel in) {
            return new android.content.integrity.CompoundFormula(in);
        }

        @java.lang.Override
        public android.content.integrity.CompoundFormula[] newArray(int size) {
            return new android.content.integrity.CompoundFormula[size];
        }
    };

    /**
     * Create a new formula from operator and operands.
     *
     * @throws IllegalArgumentException
     * 		if the number of operands is not matching the requirements
     * 		for that operator (at least 2 for {@link #AND} and {@link #OR}, 1 for {@link #NOT}).
     */
    public CompoundFormula(@android.content.integrity.CompoundFormula.Connector
    int connector, java.util.List<android.content.integrity.IntegrityFormula> formulas) {
        checkArgument(android.content.integrity.CompoundFormula.isValidConnector(connector), java.lang.String.format("Unknown connector: %d", connector));
        android.content.integrity.CompoundFormula.validateFormulas(connector, formulas);
        this.mConnector = connector;
        this.mFormulas = java.util.Collections.unmodifiableList(formulas);
    }

    CompoundFormula(android.os.Parcel in) {
        mConnector = in.readInt();
        int length = in.readInt();
        checkArgument(length >= 0, "Must have non-negative length. Got " + length);
        mFormulas = new java.util.ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            mFormulas.add(android.content.integrity.IntegrityFormula.readFromParcel(in));
        }
        android.content.integrity.CompoundFormula.validateFormulas(mConnector, mFormulas);
    }

    @android.content.integrity.CompoundFormula.Connector
    public int getConnector() {
        return mConnector;
    }

    @android.annotation.NonNull
    public java.util.List<android.content.integrity.IntegrityFormula> getFormulas() {
        return mFormulas;
    }

    @java.lang.Override
    public int getTag() {
        return android.content.integrity.IntegrityFormula.COMPOUND_FORMULA_TAG;
    }

    @java.lang.Override
    public boolean matches(android.content.integrity.AppInstallMetadata appInstallMetadata) {
        switch (getConnector()) {
            case android.content.integrity.CompoundFormula.NOT :
                return !getFormulas().get(0).matches(appInstallMetadata);
            case android.content.integrity.CompoundFormula.AND :
                return getFormulas().stream().allMatch(( formula) -> formula.matches(appInstallMetadata));
            case android.content.integrity.CompoundFormula.OR :
                return getFormulas().stream().anyMatch(( formula) -> formula.matches(appInstallMetadata));
            default :
                throw new java.lang.IllegalArgumentException("Unknown connector " + getConnector());
        }
    }

    @java.lang.Override
    public boolean isAppCertificateFormula() {
        return getFormulas().stream().anyMatch(( formula) -> formula.isAppCertificateFormula());
    }

    @java.lang.Override
    public boolean isInstallerFormula() {
        return getFormulas().stream().anyMatch(( formula) -> formula.isInstallerFormula());
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (mFormulas.size() == 1) {
            sb.append(java.lang.String.format("%s ", android.content.integrity.CompoundFormula.connectorToString(mConnector)));
            sb.append(mFormulas.get(0).toString());
        } else {
            for (int i = 0; i < mFormulas.size(); i++) {
                if (i > 0) {
                    sb.append(java.lang.String.format(" %s ", android.content.integrity.CompoundFormula.connectorToString(mConnector)));
                }
                sb.append(mFormulas.get(i).toString());
            }
        }
        return sb.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.content.integrity.CompoundFormula that = ((android.content.integrity.CompoundFormula) (o));
        return (mConnector == that.mConnector) && mFormulas.equals(that.mFormulas);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mConnector, mFormulas);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        dest.writeInt(mConnector);
        dest.writeInt(mFormulas.size());
        for (android.content.integrity.IntegrityFormula formula : mFormulas) {
            android.content.integrity.IntegrityFormula.writeToParcel(formula, dest, flags);
        }
    }

    private static void validateFormulas(@android.content.integrity.CompoundFormula.Connector
    int connector, java.util.List<android.content.integrity.IntegrityFormula> formulas) {
        switch (connector) {
            case android.content.integrity.CompoundFormula.AND :
            case android.content.integrity.CompoundFormula.OR :
                checkArgument(formulas.size() >= 2, java.lang.String.format("Connector %s must have at least 2 formulas", android.content.integrity.CompoundFormula.connectorToString(connector)));
                break;
            case android.content.integrity.CompoundFormula.NOT :
                checkArgument(formulas.size() == 1, java.lang.String.format("Connector %s must have 1 formula only", android.content.integrity.CompoundFormula.connectorToString(connector)));
                break;
        }
    }

    private static java.lang.String connectorToString(int connector) {
        switch (connector) {
            case android.content.integrity.CompoundFormula.AND :
                return "AND";
            case android.content.integrity.CompoundFormula.OR :
                return "OR";
            case android.content.integrity.CompoundFormula.NOT :
                return "NOT";
            default :
                throw new java.lang.IllegalArgumentException("Unknown connector " + connector);
        }
    }

    private static boolean isValidConnector(int connector) {
        return ((connector == android.content.integrity.CompoundFormula.AND) || (connector == android.content.integrity.CompoundFormula.OR)) || (connector == android.content.integrity.CompoundFormula.NOT);
    }
}

