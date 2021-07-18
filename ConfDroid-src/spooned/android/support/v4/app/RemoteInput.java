/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v4.app;


/**
 * Helper for using the {@link android.app.RemoteInput} API
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class RemoteInput extends android.support.v4.app.RemoteInputCompatBase.RemoteInput {
    private static final java.lang.String TAG = "RemoteInput";

    /**
     * Label used to denote the clip data type used for remote input transport
     */
    public static final java.lang.String RESULTS_CLIP_LABEL = android.support.v4.app.RemoteInputCompatJellybean.RESULTS_CLIP_LABEL;

    /**
     * Extra added to a clip data intent object to hold the results bundle.
     */
    public static final java.lang.String EXTRA_RESULTS_DATA = android.support.v4.app.RemoteInputCompatJellybean.EXTRA_RESULTS_DATA;

    private final java.lang.String mResultKey;

    private final java.lang.CharSequence mLabel;

    private final java.lang.CharSequence[] mChoices;

    private final boolean mAllowFreeFormInput;

    private final android.os.Bundle mExtras;

    RemoteInput(java.lang.String resultKey, java.lang.CharSequence label, java.lang.CharSequence[] choices, boolean allowFreeFormInput, android.os.Bundle extras) {
        this.mResultKey = resultKey;
        this.mLabel = label;
        this.mChoices = choices;
        this.mAllowFreeFormInput = allowFreeFormInput;
        this.mExtras = extras;
    }

    /**
     * Get the key that the result of this input will be set in from the Bundle returned by
     * {@link #getResultsFromIntent} when the {@link android.app.PendingIntent} is sent.
     */
    @java.lang.Override
    public java.lang.String getResultKey() {
        return mResultKey;
    }

    /**
     * Get the label to display to users when collecting this input.
     */
    @java.lang.Override
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * Get possible input choices. This can be {@code null} if there are no choices to present.
     */
    @java.lang.Override
    public java.lang.CharSequence[] getChoices() {
        return mChoices;
    }

    /**
     * Get whether or not users can provide an arbitrary value for
     * input. If you set this to {@code false}, users must select one of the
     * choices in {@link #getChoices}. An {@link IllegalArgumentException} is thrown
     * if you set this to false and {@link #getChoices} returns {@code null} or empty.
     */
    @java.lang.Override
    public boolean getAllowFreeFormInput() {
        return mAllowFreeFormInput;
    }

    /**
     * Get additional metadata carried around with this remote input.
     */
    @java.lang.Override
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Builder class for {@link android.support.v4.app.RemoteInput} objects.
     */
    public static final class Builder {
        private final java.lang.String mResultKey;

        private java.lang.CharSequence mLabel;

        private java.lang.CharSequence[] mChoices;

        private boolean mAllowFreeFormInput = true;

        private android.os.Bundle mExtras = new android.os.Bundle();

        /**
         * Create a builder object for {@link android.support.v4.app.RemoteInput} objects.
         *
         * @param resultKey
         * 		the Bundle key that refers to this input when collected from the user
         */
        public Builder(java.lang.String resultKey) {
            if (resultKey == null) {
                throw new java.lang.IllegalArgumentException("Result key can't be null");
            }
            mResultKey = resultKey;
        }

        /**
         * Set a label to be displayed to the user when collecting this input.
         *
         * @param label
         * 		The label to show to users when they input a response.
         * @return this object for method chaining
         */
        public android.support.v4.app.RemoteInput.Builder setLabel(java.lang.CharSequence label) {
            mLabel = label;
            return this;
        }

        /**
         * Specifies choices available to the user to satisfy this input.
         *
         * @param choices
         * 		an array of pre-defined choices for users input.
         * 		You must provide a non-null and non-empty array if
         * 		you disabled free form input using {@link #setAllowFreeFormInput}.
         * @return this object for method chaining
         */
        public android.support.v4.app.RemoteInput.Builder setChoices(java.lang.CharSequence[] choices) {
            mChoices = choices;
            return this;
        }

        /**
         * Specifies whether the user can provide arbitrary values.
         *
         * @param allowFreeFormInput
         * 		The default is {@code true}.
         * 		If you specify {@code false}, you must provide a non-null
         * 		and non-empty array to {@link #setChoices} or an
         * 		{@link IllegalArgumentException} is thrown.
         * @return this object for method chaining
         */
        public android.support.v4.app.RemoteInput.Builder setAllowFreeFormInput(boolean allowFreeFormInput) {
            mAllowFreeFormInput = allowFreeFormInput;
            return this;
        }

        /**
         * Merge additional metadata into this builder.
         *
         * <p>Values within the Bundle will replace existing extras values in this Builder.
         *
         * @see RemoteInput#getExtras
         */
        public android.support.v4.app.RemoteInput.Builder addExtras(android.os.Bundle extras) {
            if (extras != null) {
                mExtras.putAll(extras);
            }
            return this;
        }

        /**
         * Get the metadata Bundle used by this Builder.
         *
         * <p>The returned Bundle is shared with this Builder.
         */
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Combine all of the options that have been set and return a new
         * {@link android.support.v4.app.RemoteInput} object.
         */
        public android.support.v4.app.RemoteInput build() {
            return new android.support.v4.app.RemoteInput(mResultKey, mLabel, mChoices, mAllowFreeFormInput, mExtras);
        }
    }

    /**
     * Get the remote input results bundle from an intent. The returned Bundle will
     * contain a key/value for every result key populated by remote input collector.
     * Use the {@link Bundle#getCharSequence(String)} method to retrieve a value.
     *
     * @param intent
     * 		The intent object that fired in response to an action or content intent
     * 		which also had one or more remote input requested.
     */
    public static android.os.Bundle getResultsFromIntent(android.content.Intent intent) {
        return android.support.v4.app.RemoteInput.IMPL.getResultsFromIntent(intent);
    }

    /**
     * Populate an intent object with the results gathered from remote input. This method
     * should only be called by remote input collection services when sending results to a
     * pending intent.
     *
     * @param remoteInputs
     * 		The remote inputs for which results are being provided
     * @param intent
     * 		The intent to add remote inputs to. The {@link android.content.ClipData}
     * 		field of the intent will be modified to contain the results.
     * @param results
     * 		A bundle holding the remote input results. This bundle should
     * 		be populated with keys matching the result keys specified in
     * 		{@code remoteInputs} with values being the result per key.
     */
    public static void addResultsToIntent(android.support.v4.app.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results) {
        android.support.v4.app.RemoteInput.IMPL.addResultsToIntent(remoteInputs, intent, results);
    }

    private static final android.support.v4.app.RemoteInput.Impl IMPL;

    interface Impl {
        android.os.Bundle getResultsFromIntent(android.content.Intent intent);

        void addResultsToIntent(android.support.v4.app.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results);
    }

    static class ImplBase implements android.support.v4.app.RemoteInput.Impl {
        @java.lang.Override
        public android.os.Bundle getResultsFromIntent(android.content.Intent intent) {
            android.util.Log.w(android.support.v4.app.RemoteInput.TAG, "RemoteInput is only supported from API Level 16");
            return null;
        }

        @java.lang.Override
        public void addResultsToIntent(android.support.v4.app.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results) {
            android.util.Log.w(android.support.v4.app.RemoteInput.TAG, "RemoteInput is only supported from API Level 16");
        }
    }

    static class ImplJellybean implements android.support.v4.app.RemoteInput.Impl {
        @java.lang.Override
        public android.os.Bundle getResultsFromIntent(android.content.Intent intent) {
            return android.support.v4.app.RemoteInputCompatJellybean.getResultsFromIntent(intent);
        }

        @java.lang.Override
        public void addResultsToIntent(android.support.v4.app.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results) {
            android.support.v4.app.RemoteInputCompatJellybean.addResultsToIntent(remoteInputs, intent, results);
        }
    }

    static class ImplApi20 implements android.support.v4.app.RemoteInput.Impl {
        @java.lang.Override
        public android.os.Bundle getResultsFromIntent(android.content.Intent intent) {
            return android.support.v4.app.RemoteInputCompatApi20.getResultsFromIntent(intent);
        }

        @java.lang.Override
        public void addResultsToIntent(android.support.v4.app.RemoteInput[] remoteInputs, android.content.Intent intent, android.os.Bundle results) {
            android.support.v4.app.RemoteInputCompatApi20.addResultsToIntent(remoteInputs, intent, results);
        }
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 20) {
            IMPL = new android.support.v4.app.RemoteInput.ImplApi20();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                IMPL = new android.support.v4.app.RemoteInput.ImplJellybean();
            } else {
                IMPL = new android.support.v4.app.RemoteInput.ImplBase();
            }

    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static final android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory FACTORY = new android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory() {
        @java.lang.Override
        public android.support.v4.app.RemoteInput build(java.lang.String resultKey, java.lang.CharSequence label, java.lang.CharSequence[] choices, boolean allowFreeFormInput, android.os.Bundle extras) {
            return new android.support.v4.app.RemoteInput(resultKey, label, choices, allowFreeFormInput, extras);
        }

        @java.lang.Override
        public android.support.v4.app.RemoteInput[] newArray(int size) {
            return new android.support.v4.app.RemoteInput[size];
        }
    };
}

