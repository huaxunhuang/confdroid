/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view.textclassifier;


/**
 * Represents the result of language detection of a piece of text.
 * <p>
 * This contains a list of locales, each paired with a confidence score, sorted in decreasing
 * order of those scores. E.g., for a given input text, the model may return
 * {@code [<"en", 0.85>, <"fr", 0.15>]}. This sample result means the model reports that it is
 * 85% likely that the entire text is in English and 15% likely that the entire text is in French,
 * etc. It does not mean that 85% of the input is in English and 15% is in French.
 */
public final class TextLanguage implements android.os.Parcelable {
    @android.annotation.NonNull
    public static final android.view.textclassifier.Creator<android.view.textclassifier.TextLanguage> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextLanguage>() {
        @java.lang.Override
        public android.view.textclassifier.TextLanguage createFromParcel(android.os.Parcel in) {
            return android.view.textclassifier.TextLanguage.readFromParcel(in);
        }

        @java.lang.Override
        public android.view.textclassifier.TextLanguage[] newArray(int size) {
            return new android.view.textclassifier.TextLanguage[size];
        }
    };

    static final android.view.textclassifier.TextLanguage EMPTY = new android.view.textclassifier.TextLanguage.Builder().build();

    @android.annotation.Nullable
    private final java.lang.String mId;

    private final android.view.textclassifier.EntityConfidence mEntityConfidence;

    private final android.os.Bundle mBundle;

    private TextLanguage(@android.annotation.Nullable
    java.lang.String id, android.view.textclassifier.EntityConfidence entityConfidence, android.os.Bundle bundle) {
        mId = id;
        mEntityConfidence = entityConfidence;
        mBundle = bundle;
    }

    /**
     * Returns the id, if one exists, for this object.
     */
    @android.annotation.Nullable
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Returns the number of possible locales for the processed text.
     */
    @android.annotation.IntRange(from = 0)
    public int getLocaleHypothesisCount() {
        return mEntityConfidence.getEntities().size();
    }

    /**
     * Returns the language locale at the specified index. Locales are ordered from high
     * confidence to low confidence.
     * <p>
     * See {@link #getLocaleHypothesisCount()} for the number of locales available.
     *
     * @throws IndexOutOfBoundsException
     * 		if the specified index is out of range.
     */
    @android.annotation.NonNull
    public android.icu.util.ULocale getLocale(int index) {
        return android.icu.util.ULocale.forLanguageTag(mEntityConfidence.getEntities().get(index));
    }

    /**
     * Returns the confidence score for the specified language locale. The value ranges from
     * 0 (low confidence) to 1 (high confidence). 0 indicates that the locale was not found for
     * the processed text.
     */
    @android.annotation.FloatRange(from = 0.0, to = 1.0)
    public float getConfidenceScore(@android.annotation.NonNull
    android.icu.util.ULocale locale) {
        return mEntityConfidence.getConfidenceScore(locale.toLanguageTag());
    }

    /**
     * Returns a bundle containing non-structured extra information about this result. What is
     * returned in the extras is specific to the {@link TextClassifier} implementation.
     *
     * <p><b>NOTE: </b>Do not modify this bundle.
     */
    @android.annotation.NonNull
    public android.os.Bundle getExtras() {
        return mBundle;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format(java.util.Locale.US, "TextLanguage {id=%s, locales=%s, bundle=%s}", mId, mEntityConfidence, mBundle);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mId);
        mEntityConfidence.writeToParcel(dest, flags);
        dest.writeBundle(mBundle);
    }

    private static android.view.textclassifier.TextLanguage readFromParcel(android.os.Parcel in) {
        return new android.view.textclassifier.TextLanguage(in.readString(), this.CREATOR.createFromParcel(in), in.readBundle());
    }

    /**
     * Builder used to build TextLanguage objects.
     */
    public static final class Builder {
        @android.annotation.Nullable
        private java.lang.String mId;

        private final java.util.Map<java.lang.String, java.lang.Float> mEntityConfidenceMap = new android.util.ArrayMap();

        @android.annotation.Nullable
        private android.os.Bundle mBundle;

        /**
         * Sets a language locale for the processed text and assigns a confidence score. If the
         * locale has already been set, this updates it.
         *
         * @param confidenceScore
         * 		a value from 0 (low confidence) to 1 (high confidence).
         * 		0 implies the locale does not exist for the processed text.
         * 		Values greater than 1 are clamped to 1.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLanguage.Builder putLocale(@android.annotation.NonNull
        android.icu.util.ULocale locale, @android.annotation.FloatRange(from = 0.0, to = 1.0)
        float confidenceScore) {
            com.android.internal.util.Preconditions.checkNotNull(locale);
            mEntityConfidenceMap.put(locale.toLanguageTag(), confidenceScore);
            return this;
        }

        /**
         * Sets an optional id for the TextLanguage object.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLanguage.Builder setId(@android.annotation.Nullable
        java.lang.String id) {
            mId = id;
            return this;
        }

        /**
         * Sets a bundle containing non-structured extra information about the TextLanguage object.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLanguage.Builder setExtras(@android.annotation.NonNull
        android.os.Bundle bundle) {
            mBundle = com.android.internal.util.Preconditions.checkNotNull(bundle);
            return this;
        }

        /**
         * Builds and returns a new TextLanguage object.
         * <p>
         * If necessary, this method will verify fields, clamp them, and make them immutable.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLanguage build() {
            mBundle = (mBundle == null) ? android.os.Bundle.EMPTY : mBundle;
            return new android.view.textclassifier.TextLanguage(mId, new android.view.textclassifier.EntityConfidence(mEntityConfidenceMap), mBundle);
        }
    }

    /**
     * A request object for detecting the language of a piece of text.
     */
    public static final class Request implements android.os.Parcelable {
        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.TextLanguage.Request> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextLanguage.Request>() {
            @java.lang.Override
            public android.view.textclassifier.TextLanguage.Request createFromParcel(android.os.Parcel in) {
                return android.view.textclassifier.TextLanguage.Request.readFromParcel(in);
            }

            @java.lang.Override
            public android.view.textclassifier.TextLanguage.Request[] newArray(int size) {
                return new android.view.textclassifier.TextLanguage.Request[size];
            }
        };

        private final java.lang.CharSequence mText;

        private final android.os.Bundle mExtra;

        @android.annotation.Nullable
        private java.lang.String mCallingPackageName;

        private Request(java.lang.CharSequence text, android.os.Bundle bundle) {
            mText = text;
            mExtra = bundle;
        }

        /**
         * Returns the text to process.
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getText() {
            return mText;
        }

        /**
         * Sets the name of the package that is sending this request.
         * Package-private for SystemTextClassifier's use.
         *
         * @unknown 
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public void setCallingPackageName(@android.annotation.Nullable
        java.lang.String callingPackageName) {
            mCallingPackageName = callingPackageName;
        }

        /**
         * Returns the name of the package that sent this request.
         * This returns null if no calling package name is set.
         */
        @android.annotation.Nullable
        public java.lang.String getCallingPackageName() {
            return mCallingPackageName;
        }

        /**
         * Returns a bundle containing non-structured extra information about this request.
         *
         * <p><b>NOTE: </b>Do not modify this bundle.
         */
        @android.annotation.NonNull
        public android.os.Bundle getExtras() {
            return mExtra;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeCharSequence(mText);
            dest.writeString(mCallingPackageName);
            dest.writeBundle(mExtra);
        }

        private static android.view.textclassifier.TextLanguage.Request readFromParcel(android.os.Parcel in) {
            final java.lang.CharSequence text = in.readCharSequence();
            final java.lang.String callingPackageName = in.readString();
            final android.os.Bundle extra = in.readBundle();
            final android.view.textclassifier.TextLanguage.Request request = new android.view.textclassifier.TextLanguage.Request(text, extra);
            request.setCallingPackageName(callingPackageName);
            return request;
        }

        /**
         * A builder for building TextLanguage requests.
         */
        public static final class Builder {
            private final java.lang.CharSequence mText;

            @android.annotation.Nullable
            private android.os.Bundle mBundle;

            /**
             * Creates a builder to build TextLanguage requests.
             *
             * @param text
             * 		the text to process.
             */
            public Builder(@android.annotation.NonNull
            java.lang.CharSequence text) {
                mText = com.android.internal.util.Preconditions.checkNotNull(text);
            }

            /**
             * Sets a bundle containing non-structured extra information about the request.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextLanguage.Request.Builder setExtras(@android.annotation.NonNull
            android.os.Bundle bundle) {
                mBundle = com.android.internal.util.Preconditions.checkNotNull(bundle);
                return this;
            }

            /**
             * Builds and returns a new TextLanguage request object.
             * <p>
             * If necessary, this method will verify fields, clamp them, and make them immutable.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextLanguage.Request build() {
                return new android.view.textclassifier.TextLanguage.Request(mText.toString(), mBundle == null ? android.os.Bundle.EMPTY : mBundle);
            }
        }
    }
}

