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
 * Represent rules to be used in the rule evaluation engine to match against app installs.
 *
 * <p>Instances of this class are immutable.
 *
 * @unknown 
 */
@android.annotation.TestApi
@android.annotation.SystemApi
@com.android.internal.annotations.VisibleForTesting
public final class Rule implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.content.integrity.Rule.DENY, android.content.integrity.Rule.FORCE_ALLOW })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Effect {}

    /**
     * If this rule matches the install, the install should be denied.
     */
    public static final int DENY = 0;

    /**
     * If this rule matches the install, the install will be allowed regardless of other matched
     * rules.
     */
    public static final int FORCE_ALLOW = 1;

    @android.annotation.NonNull
    private final android.content.integrity.IntegrityFormula mFormula;

    @android.content.integrity.Rule.Effect
    private final int mEffect;

    public Rule(@android.annotation.NonNull
    android.content.integrity.IntegrityFormula formula, @android.content.integrity.Rule.Effect
    int effect) {
        checkArgument(android.content.integrity.Rule.isValidEffect(effect), java.lang.String.format("Unknown effect: %d", effect));
        this.mFormula = java.util.Objects.requireNonNull(formula);
        this.mEffect = effect;
    }

    Rule(android.os.Parcel in) {
        mFormula = android.content.integrity.IntegrityFormula.readFromParcel(in);
        mEffect = in.readInt();
    }

    @android.annotation.NonNull
    public static final android.content.integrity.Creator<android.content.integrity.Rule> CREATOR = new android.content.integrity.Creator<android.content.integrity.Rule>() {
        @java.lang.Override
        public android.content.integrity.Rule createFromParcel(android.os.Parcel in) {
            return new android.content.integrity.Rule(in);
        }

        @java.lang.Override
        public android.content.integrity.Rule[] newArray(int size) {
            return new android.content.integrity.Rule[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        android.content.integrity.IntegrityFormula.writeToParcel(mFormula, dest, flags);
        dest.writeInt(mEffect);
    }

    @android.annotation.NonNull
    public android.content.integrity.IntegrityFormula getFormula() {
        return mFormula;
    }

    @android.content.integrity.Rule.Effect
    public int getEffect() {
        return mEffect;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("Rule: %s, %s", mFormula, android.content.integrity.Rule.effectToString(mEffect));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.content.integrity.Rule that = ((android.content.integrity.Rule) (o));
        return (mEffect == that.mEffect) && java.util.Objects.equals(mFormula, that.mFormula);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mFormula, mEffect);
    }

    private static java.lang.String effectToString(int effect) {
        switch (effect) {
            case android.content.integrity.Rule.DENY :
                return "DENY";
            case android.content.integrity.Rule.FORCE_ALLOW :
                return "FORCE_ALLOW";
            default :
                throw new java.lang.IllegalArgumentException("Unknown effect " + effect);
        }
    }

    private static boolean isValidEffect(int effect) {
        return (effect == android.content.integrity.Rule.DENY) || (effect == android.content.integrity.Rule.FORCE_ALLOW);
    }
}

