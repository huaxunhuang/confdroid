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
 * Immutable data class encapsulating all parameters of a rule set.
 *
 * @unknown 
 */
@android.annotation.TestApi
@android.annotation.SystemApi
public class RuleSet {
    private final java.lang.String mVersion;

    private final java.util.List<android.content.integrity.Rule> mRules;

    private RuleSet(java.lang.String version, java.util.List<android.content.integrity.Rule> rules) {
        mVersion = version;
        mRules = java.util.Collections.unmodifiableList(rules);
    }

    /**
     *
     *
     * @see Builder#setVersion(String).
     */
    @android.annotation.NonNull
    public java.lang.String getVersion() {
        return mVersion;
    }

    /**
     *
     *
     * @see Builder#addRules(List).
     */
    @android.annotation.NonNull
    public java.util.List<android.content.integrity.Rule> getRules() {
        return mRules;
    }

    /**
     * Builder class for RuleSetUpdateRequest.
     */
    public static class Builder {
        private java.lang.String mVersion;

        private java.util.List<android.content.integrity.Rule> mRules;

        public Builder() {
            mRules = new java.util.ArrayList<>();
        }

        /**
         * Set a version string to identify this rule set. This can be retrieved by {@link AppIntegrityManager#getCurrentRuleSetVersion()}.
         */
        @android.annotation.NonNull
        public android.content.integrity.RuleSet.Builder setVersion(@android.annotation.NonNull
        java.lang.String version) {
            mVersion = version;
            return this;
        }

        /**
         * Add the rules to include.
         */
        @android.annotation.NonNull
        public android.content.integrity.RuleSet.Builder addRules(@android.annotation.NonNull
        java.util.List<android.content.integrity.Rule> rules) {
            mRules.addAll(rules);
            return this;
        }

        /**
         * Builds a {@link RuleSet}.
         *
         * @throws IllegalArgumentException
         * 		if version is null
         */
        @android.annotation.NonNull
        public android.content.integrity.RuleSet build() {
            java.util.Objects.requireNonNull(mVersion);
            return new android.content.integrity.RuleSet(mVersion, mRules);
        }
    }
}

