/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * Information about where text selection should be.
 */
public final class TextSelection implements android.os.Parcelable {
    private final int mStartIndex;

    private final int mEndIndex;

    private final android.view.textclassifier.EntityConfidence mEntityConfidence;

    @android.annotation.Nullable
    private final java.lang.String mId;

    private final android.os.Bundle mExtras;

    private TextSelection(int startIndex, int endIndex, java.util.Map<java.lang.String, java.lang.Float> entityConfidence, java.lang.String id, android.os.Bundle extras) {
        mStartIndex = startIndex;
        mEndIndex = endIndex;
        mEntityConfidence = new android.view.textclassifier.EntityConfidence(entityConfidence);
        mId = id;
        mExtras = extras;
    }

    /**
     * Returns the start index of the text selection.
     */
    public int getSelectionStartIndex() {
        return mStartIndex;
    }

    /**
     * Returns the end index of the text selection.
     */
    public int getSelectionEndIndex() {
        return mEndIndex;
    }

    /**
     * Returns the number of entities found in the classified text.
     */
    @android.annotation.IntRange(from = 0)
    public int getEntityCount() {
        return mEntityConfidence.getEntities().size();
    }

    /**
     * Returns the entity at the specified index. Entities are ordered from high confidence
     * to low confidence.
     *
     * @throws IndexOutOfBoundsException
     * 		if the specified index is out of range.
     * @see #getEntityCount() for the number of entities available.
     */
    @android.annotation.NonNull
    @android.view.textclassifier.TextClassifier.EntityType
    public java.lang.String getEntity(int index) {
        return mEntityConfidence.getEntities().get(index);
    }

    /**
     * Returns the confidence score for the specified entity. The value ranges from
     * 0 (low confidence) to 1 (high confidence). 0 indicates that the entity was not found for the
     * classified text.
     */
    @android.annotation.FloatRange(from = 0.0, to = 1.0)
    public float getConfidenceScore(@android.view.textclassifier.TextClassifier.EntityType
    java.lang.String entity) {
        return mEntityConfidence.getConfidenceScore(entity);
    }

    /**
     * Returns the id, if one exists, for this object.
     */
    @android.annotation.Nullable
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Returns the extended data.
     *
     * <p><b>NOTE: </b>Do not modify this bundle.
     */
    @android.annotation.NonNull
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format(java.util.Locale.US, "TextSelection {id=%s, startIndex=%d, endIndex=%d, entities=%s}", mId, mStartIndex, mEndIndex, mEntityConfidence);
    }

    /**
     * Builder used to build {@link TextSelection} objects.
     */
    public static final class Builder {
        private final int mStartIndex;

        private final int mEndIndex;

        private final java.util.Map<java.lang.String, java.lang.Float> mEntityConfidence = new android.util.ArrayMap();

        @android.annotation.Nullable
        private java.lang.String mId;

        @android.annotation.Nullable
        private android.os.Bundle mExtras;

        /**
         * Creates a builder used to build {@link TextSelection} objects.
         *
         * @param startIndex
         * 		the start index of the text selection.
         * @param endIndex
         * 		the end index of the text selection. Must be greater than startIndex
         */
        public Builder(@android.annotation.IntRange(from = 0)
        int startIndex, @android.annotation.IntRange(from = 0)
        int endIndex) {
            com.android.internal.util.Preconditions.checkArgument(startIndex >= 0);
            com.android.internal.util.Preconditions.checkArgument(endIndex > startIndex);
            mStartIndex = startIndex;
            mEndIndex = endIndex;
        }

        /**
         * Sets an entity type for the classified text and assigns a confidence score.
         *
         * @param confidenceScore
         * 		a value from 0 (low confidence) to 1 (high confidence).
         * 		0 implies the entity does not exist for the classified text.
         * 		Values greater than 1 are clamped to 1.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextSelection.Builder setEntityType(@android.annotation.NonNull
        @android.view.textclassifier.TextClassifier.EntityType
        java.lang.String type, @android.annotation.FloatRange(from = 0.0, to = 1.0)
        float confidenceScore) {
            com.android.internal.util.Preconditions.checkNotNull(type);
            mEntityConfidence.put(type, confidenceScore);
            return this;
        }

        /**
         * Sets an id for the TextSelection object.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextSelection.Builder setId(@android.annotation.Nullable
        java.lang.String id) {
            mId = id;
            return this;
        }

        /**
         * Sets the extended data.
         *
         * @return this builder
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextSelection.Builder setExtras(@android.annotation.Nullable
        android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Builds and returns {@link TextSelection} object.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextSelection build() {
            return new android.view.textclassifier.TextSelection(mStartIndex, mEndIndex, mEntityConfidence, mId, mExtras == null ? android.os.Bundle.EMPTY : mExtras);
        }
    }

    /**
     * A request object for generating TextSelection.
     */
    public static final class Request implements android.os.Parcelable {
        private final java.lang.CharSequence mText;

        private final int mStartIndex;

        private final int mEndIndex;

        @android.annotation.Nullable
        private final android.os.LocaleList mDefaultLocales;

        private final boolean mDarkLaunchAllowed;

        private final android.os.Bundle mExtras;

        @android.annotation.Nullable
        private java.lang.String mCallingPackageName;

        private Request(java.lang.CharSequence text, int startIndex, int endIndex, android.os.LocaleList defaultLocales, boolean darkLaunchAllowed, android.os.Bundle extras) {
            mText = text;
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mDefaultLocales = defaultLocales;
            mDarkLaunchAllowed = darkLaunchAllowed;
            mExtras = extras;
        }

        /**
         * Returns the text providing context for the selected text (which is specified by the
         * sub sequence starting at startIndex and ending at endIndex).
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getText() {
            return mText;
        }

        /**
         * Returns start index of the selected part of text.
         */
        @android.annotation.IntRange(from = 0)
        public int getStartIndex() {
            return mStartIndex;
        }

        /**
         * Returns end index of the selected part of text.
         */
        @android.annotation.IntRange(from = 0)
        public int getEndIndex() {
            return mEndIndex;
        }

        /**
         * Returns true if the TextClassifier should return selection suggestions when "dark
         * launched". Otherwise, returns false.
         *
         * @unknown 
         */
        public boolean isDarkLaunchAllowed() {
            return mDarkLaunchAllowed;
        }

        /**
         *
         *
         * @return ordered list of locale preferences that can be used to disambiguate the
        provided text.
         */
        @android.annotation.Nullable
        public android.os.LocaleList getDefaultLocales() {
            return mDefaultLocales;
        }

        /**
         * Sets the name of the package that is sending this request.
         * <p>
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
         * This returns {@code null} if no calling package name is set.
         */
        @android.annotation.Nullable
        public java.lang.String getCallingPackageName() {
            return mCallingPackageName;
        }

        /**
         * Returns the extended data.
         *
         * <p><b>NOTE: </b>Do not modify this bundle.
         */
        @android.annotation.NonNull
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * A builder for building TextSelection requests.
         */
        public static final class Builder {
            private final java.lang.CharSequence mText;

            private final int mStartIndex;

            private final int mEndIndex;

            @android.annotation.Nullable
            private android.os.LocaleList mDefaultLocales;

            private boolean mDarkLaunchAllowed;

            private android.os.Bundle mExtras;

            /**
             *
             *
             * @param text
             * 		text providing context for the selected text (which is specified by the
             * 		sub sequence starting at selectionStartIndex and ending at selectionEndIndex)
             * @param startIndex
             * 		start index of the selected part of text
             * @param endIndex
             * 		end index of the selected part of text
             */
            public Builder(@android.annotation.NonNull
            java.lang.CharSequence text, @android.annotation.IntRange(from = 0)
            int startIndex, @android.annotation.IntRange(from = 0)
            int endIndex) {
                android.view.textclassifier.TextClassifier.Utils.checkArgument(text, startIndex, endIndex);
                mText = text;
                mStartIndex = startIndex;
                mEndIndex = endIndex;
            }

            /**
             *
             *
             * @param defaultLocales
             * 		ordered list of locale preferences that may be used to
             * 		disambiguate the provided text. If no locale preferences exist, set this to null
             * 		or an empty locale list.
             * @return this builder.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextSelection.Request.Builder setDefaultLocales(@android.annotation.Nullable
            android.os.LocaleList defaultLocales) {
                mDefaultLocales = defaultLocales;
                return this;
            }

            /**
             *
             *
             * @param allowed
             * 		whether or not the TextClassifier should return selection suggestions
             * 		when "dark launched". When a TextClassifier is dark launched, it can suggest
             * 		selection changes that should not be used to actually change the user's
             * 		selection. Instead, the suggested selection is logged, compared with the user's
             * 		selection interaction, and used to generate quality metrics for the
             * 		TextClassifier. Not parceled.
             * @return this builder.
             * @unknown 
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextSelection.Request.Builder setDarkLaunchAllowed(boolean allowed) {
                mDarkLaunchAllowed = allowed;
                return this;
            }

            /**
             * Sets the extended data.
             *
             * @return this builder
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextSelection.Request.Builder setExtras(@android.annotation.Nullable
            android.os.Bundle extras) {
                mExtras = extras;
                return this;
            }

            /**
             * Builds and returns the request object.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextSelection.Request build() {
                return new android.view.textclassifier.TextSelection.Request(new android.text.SpannedString(mText), mStartIndex, mEndIndex, mDefaultLocales, mDarkLaunchAllowed, mExtras == null ? android.os.Bundle.EMPTY : mExtras);
            }
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeCharSequence(mText);
            dest.writeInt(mStartIndex);
            dest.writeInt(mEndIndex);
            dest.writeParcelable(mDefaultLocales, flags);
            dest.writeString(mCallingPackageName);
            dest.writeBundle(mExtras);
        }

        private static android.view.textclassifier.TextSelection.Request readFromParcel(android.os.Parcel in) {
            final java.lang.CharSequence text = in.readCharSequence();
            final int startIndex = in.readInt();
            final int endIndex = in.readInt();
            final android.os.LocaleList defaultLocales = in.readParcelable(null);
            final java.lang.String callingPackageName = in.readString();
            final android.os.Bundle extras = in.readBundle();
            final android.view.textclassifier.TextSelection.Request request = /* darkLaunchAllowed= */
            new android.view.textclassifier.TextSelection.Request(text, startIndex, endIndex, defaultLocales, false, extras);
            request.setCallingPackageName(callingPackageName);
            return request;
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.textclassifier.TextSelection.Request> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextSelection.Request>() {
            @java.lang.Override
            public android.view.textclassifier.Request createFromParcel(android.os.Parcel in) {
                return readFromParcel(in);
            }

            @java.lang.Override
            public android.view.textclassifier.Request[] newArray(int size) {
                return new android.view.textclassifier.Request[size];
            }
        };
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mStartIndex);
        dest.writeInt(mEndIndex);
        mEntityConfidence.writeToParcel(dest, flags);
        dest.writeString(mId);
        dest.writeBundle(mExtras);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textclassifier.TextSelection> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextSelection>() {
        @java.lang.Override
        public android.view.textclassifier.TextSelection createFromParcel(android.os.Parcel in) {
            return new android.view.textclassifier.TextSelection(in);
        }

        @java.lang.Override
        public android.view.textclassifier.TextSelection[] newArray(int size) {
            return new android.view.textclassifier.TextSelection[size];
        }
    };

    private TextSelection(android.os.Parcel in) {
        mStartIndex = in.readInt();
        mEndIndex = in.readInt();
        mEntityConfidence = this.CREATOR.createFromParcel(in);
        mId = in.readString();
        mExtras = in.readBundle();
    }
}

