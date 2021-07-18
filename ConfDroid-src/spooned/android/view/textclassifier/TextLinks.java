/**
 * Copyright 2017 The Android Open Source Project
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
 * A collection of links, representing subsequences of text and the entity types (phone number,
 * address, url, etc) they may be.
 */
public final class TextLinks implements android.os.Parcelable {
    /**
     * Return status of an attempt to apply TextLinks to text.
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.TextLinks.STATUS_LINKS_APPLIED, android.view.textclassifier.TextLinks.STATUS_NO_LINKS_FOUND, android.view.textclassifier.TextLinks.STATUS_NO_LINKS_APPLIED, android.view.textclassifier.TextLinks.STATUS_DIFFERENT_TEXT, android.view.textclassifier.TextLinks.STATUS_UNSUPPORTED_CHARACTER })
    public @interface Status {}

    /**
     * Links were successfully applied to the text.
     */
    public static final int STATUS_LINKS_APPLIED = 0;

    /**
     * No links exist to apply to text. Links count is zero.
     */
    public static final int STATUS_NO_LINKS_FOUND = 1;

    /**
     * No links applied to text. The links were filtered out.
     */
    public static final int STATUS_NO_LINKS_APPLIED = 2;

    /**
     * The specified text does not match the text used to generate the links.
     */
    public static final int STATUS_DIFFERENT_TEXT = 3;

    /**
     * The specified text contains unsupported characters.
     */
    public static final int STATUS_UNSUPPORTED_CHARACTER = 4;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.TextLinks.APPLY_STRATEGY_IGNORE, android.view.textclassifier.TextLinks.APPLY_STRATEGY_REPLACE })
    public @interface ApplyStrategy {}

    /**
     * Do not replace {@link ClickableSpan}s that exist where the {@link TextLinkSpan} needs to
     * be applied to. Do not apply the TextLinkSpan.
     */
    public static final int APPLY_STRATEGY_IGNORE = 0;

    /**
     * Replace any {@link ClickableSpan}s that exist where the {@link TextLinkSpan} needs to be
     * applied to.
     */
    public static final int APPLY_STRATEGY_REPLACE = 1;

    private final java.lang.String mFullText;

    private final java.util.List<android.view.textclassifier.TextLinks.TextLink> mLinks;

    private final android.os.Bundle mExtras;

    private TextLinks(java.lang.String fullText, java.util.ArrayList<android.view.textclassifier.TextLinks.TextLink> links, android.os.Bundle extras) {
        mFullText = fullText;
        mLinks = java.util.Collections.unmodifiableList(links);
        mExtras = extras;
    }

    /**
     * Returns the text that was used to generate these links.
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public java.lang.String getText() {
        return mFullText;
    }

    /**
     * Returns an unmodifiable Collection of the links.
     */
    @android.annotation.NonNull
    public java.util.Collection<android.view.textclassifier.TextLinks.TextLink> getLinks() {
        return mLinks;
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
     * Annotates the given text with the generated links. It will fail if the provided text doesn't
     * match the original text used to create the TextLinks.
     *
     * <p><strong>NOTE: </strong>It may be necessary to set a LinkMovementMethod on the TextView
     * widget to properly handle links. See {@link TextView#setMovementMethod(MovementMethod)}
     *
     * @param text
     * 		the text to apply the links to. Must match the original text
     * @param applyStrategy
     * 		the apply strategy used to determine how to apply links to text.
     * 		e.g {@link TextLinks#APPLY_STRATEGY_IGNORE}
     * @param spanFactory
     * 		a custom span factory for converting TextLinks to TextLinkSpans.
     * 		Set to {@code null} to use the default span factory.
     * @return a status code indicating whether or not the links were successfully applied
    e.g. {@link #STATUS_LINKS_APPLIED}
     */
    @android.view.textclassifier.TextLinks.Status
    public int apply(@android.annotation.NonNull
    android.text.Spannable text, @android.view.textclassifier.TextLinks.ApplyStrategy
    int applyStrategy, @android.annotation.Nullable
    java.util.function.Function<android.view.textclassifier.TextLinks.TextLink, android.view.textclassifier.TextLinks.TextLinkSpan> spanFactory) {
        com.android.internal.util.Preconditions.checkNotNull(text);
        return new android.view.textclassifier.TextLinksParams.Builder().setApplyStrategy(applyStrategy).setSpanFactory(spanFactory).build().apply(text, this);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format(java.util.Locale.US, "TextLinks{fullText=%s, links=%s}", mFullText, mLinks);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mFullText);
        dest.writeTypedList(mLinks);
        dest.writeBundle(mExtras);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textclassifier.TextLinks> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextLinks>() {
        @java.lang.Override
        public android.view.textclassifier.TextLinks createFromParcel(android.os.Parcel in) {
            return new android.view.textclassifier.TextLinks(in);
        }

        @java.lang.Override
        public android.view.textclassifier.TextLinks[] newArray(int size) {
            return new android.view.textclassifier.TextLinks[size];
        }
    };

    private TextLinks(android.os.Parcel in) {
        mFullText = in.readString();
        mLinks = in.createTypedArrayList(this.CREATOR);
        mExtras = in.readBundle();
    }

    /**
     * A link, identifying a substring of text and possible entity types for it.
     */
    public static final class TextLink implements android.os.Parcelable {
        private final android.view.textclassifier.EntityConfidence mEntityScores;

        private final int mStart;

        private final int mEnd;

        private final android.os.Bundle mExtras;

        @android.annotation.Nullable
        private final android.text.style.URLSpan mUrlSpan;

        /**
         * Create a new TextLink.
         *
         * @param start
         * 		The start index of the identified subsequence
         * @param end
         * 		The end index of the identified subsequence
         * @param entityConfidence
         * 		A mapping of entity type to confidence score
         * @param extras
         * 		A bundle containing custom data related to this TextLink
         * @param urlSpan
         * 		An optional URLSpan to delegate to. NOTE: Not parcelled
         * @throws IllegalArgumentException
         * 		if {@code entityConfidence} is null or empty
         * @throws IllegalArgumentException
         * 		if {@code start} is greater than {@code end}
         */
        private TextLink(int start, int end, @android.annotation.NonNull
        android.view.textclassifier.EntityConfidence entityConfidence, @android.annotation.NonNull
        android.os.Bundle extras, @android.annotation.Nullable
        android.text.style.URLSpan urlSpan) {
            com.android.internal.util.Preconditions.checkNotNull(entityConfidence);
            com.android.internal.util.Preconditions.checkArgument(!entityConfidence.getEntities().isEmpty());
            com.android.internal.util.Preconditions.checkArgument(start <= end);
            com.android.internal.util.Preconditions.checkNotNull(extras);
            mStart = start;
            mEnd = end;
            mEntityScores = entityConfidence;
            mUrlSpan = urlSpan;
            mExtras = extras;
        }

        /**
         * Returns the start index of this link in the original text.
         *
         * @return the start index
         */
        public int getStart() {
            return mStart;
        }

        /**
         * Returns the end index of this link in the original text.
         *
         * @return the end index
         */
        public int getEnd() {
            return mEnd;
        }

        /**
         * Returns the number of entity types that have confidence scores.
         *
         * @return the entity count
         */
        public int getEntityCount() {
            return mEntityScores.getEntities().size();
        }

        /**
         * Returns the entity type at a given index. Entity types are sorted by confidence.
         *
         * @return the entity type at the provided index
         */
        @android.annotation.NonNull
        @android.view.textclassifier.TextClassifier.EntityType
        public java.lang.String getEntity(int index) {
            return mEntityScores.getEntities().get(index);
        }

        /**
         * Returns the confidence score for a particular entity type.
         *
         * @param entityType
         * 		the entity type
         */
        @android.annotation.FloatRange(from = 0.0, to = 1.0)
        public float getConfidenceScore(@android.view.textclassifier.TextClassifier.EntityType
        java.lang.String entityType) {
            return mEntityScores.getConfidenceScore(entityType);
        }

        /**
         * Returns a bundle containing custom data related to this TextLink.
         */
        @android.annotation.NonNull
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return java.lang.String.format(java.util.Locale.US, "TextLink{start=%s, end=%s, entityScores=%s, urlSpan=%s}", mStart, mEnd, mEntityScores, mUrlSpan);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            mEntityScores.writeToParcel(dest, flags);
            dest.writeInt(mStart);
            dest.writeInt(mEnd);
            dest.writeBundle(mExtras);
        }

        private static android.view.textclassifier.TextLinks.TextLink readFromParcel(android.os.Parcel in) {
            final android.view.textclassifier.EntityConfidence entityConfidence = android.view.textclassifier.EntityConfidence.this.CREATOR.createFromParcel(in);
            final int start = in.readInt();
            final int end = in.readInt();
            final android.os.Bundle extras = in.readBundle();
            return /* urlSpan */
            new android.view.textclassifier.TextLinks.TextLink(start, end, entityConfidence, extras, null);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.textclassifier.TextLinks.TextLink> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextLinks.TextLink>() {
            @java.lang.Override
            public android.view.textclassifier.TextLink createFromParcel(android.os.Parcel in) {
                return readFromParcel(in);
            }

            @java.lang.Override
            public android.view.textclassifier.TextLink[] newArray(int size) {
                return new android.view.textclassifier.TextLink[size];
            }
        };
    }

    /**
     * A request object for generating TextLinks.
     */
    public static final class Request implements android.os.Parcelable {
        private final java.lang.CharSequence mText;

        @android.annotation.Nullable
        private final android.os.LocaleList mDefaultLocales;

        @android.annotation.Nullable
        private final android.view.textclassifier.TextClassifier.EntityConfig mEntityConfig;

        private final boolean mLegacyFallback;

        @android.annotation.Nullable
        private java.lang.String mCallingPackageName;

        private final android.os.Bundle mExtras;

        private Request(java.lang.CharSequence text, android.os.LocaleList defaultLocales, android.view.textclassifier.TextClassifier.EntityConfig entityConfig, boolean legacyFallback, android.os.Bundle extras) {
            mText = text;
            mDefaultLocales = defaultLocales;
            mEntityConfig = entityConfig;
            mLegacyFallback = legacyFallback;
            mExtras = extras;
        }

        /**
         * Returns the text to generate links for.
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getText() {
            return mText;
        }

        /**
         *
         *
         * @return ordered list of locale preferences that can be used to disambiguate
        the provided text
         */
        @android.annotation.Nullable
        public android.os.LocaleList getDefaultLocales() {
            return mDefaultLocales;
        }

        /**
         *
         *
         * @return The config representing the set of entities to look for
         * @see Builder#setEntityConfig(EntityConfig)
         */
        @android.annotation.Nullable
        public android.view.textclassifier.TextClassifier.EntityConfig getEntityConfig() {
            return mEntityConfig;
        }

        /**
         * Returns whether the TextClassifier can fallback to legacy links if smart linkify is
         * disabled.
         * <strong>Note: </strong>This is not parcelled.
         *
         * @unknown 
         */
        public boolean isLegacyFallback() {
            return mLegacyFallback;
        }

        /**
         * Sets the name of the package that is sending this request.
         * <p>
         * Package-private for SystemTextClassifier's use.
         *
         * @unknown 
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
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
         * A builder for building TextLinks requests.
         */
        public static final class Builder {
            private final java.lang.CharSequence mText;

            @android.annotation.Nullable
            private android.os.LocaleList mDefaultLocales;

            @android.annotation.Nullable
            private android.view.textclassifier.TextClassifier.EntityConfig mEntityConfig;

            private boolean mLegacyFallback = true;// Use legacy fall back by default.


            @android.annotation.Nullable
            private android.os.Bundle mExtras;

            public Builder(@android.annotation.NonNull
            java.lang.CharSequence text) {
                mText = com.android.internal.util.Preconditions.checkNotNull(text);
            }

            /**
             *
             *
             * @param defaultLocales
             * 		ordered list of locale preferences that may be used to
             * 		disambiguate the provided text. If no locale preferences exist,
             * 		set this to null or an empty locale list.
             * @return this builder
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextLinks.Request.Builder setDefaultLocales(@android.annotation.Nullable
            android.os.LocaleList defaultLocales) {
                mDefaultLocales = defaultLocales;
                return this;
            }

            /**
             * Sets the entity configuration to use. This determines what types of entities the
             * TextClassifier will look for.
             * Set to {@code null} for the default entity config and teh TextClassifier will
             * automatically determine what links to generate.
             *
             * @return this builder
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextLinks.Request.Builder setEntityConfig(@android.annotation.Nullable
            android.view.textclassifier.TextClassifier.EntityConfig entityConfig) {
                mEntityConfig = entityConfig;
                return this;
            }

            /**
             * Sets whether the TextClassifier can fallback to legacy links if smart linkify is
             * disabled.
             *
             * <p><strong>Note: </strong>This is not parcelled.
             *
             * @return this builder
             * @unknown 
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextLinks.Request.Builder setLegacyFallback(boolean legacyFallback) {
                mLegacyFallback = legacyFallback;
                return this;
            }

            /**
             * Sets the extended data.
             *
             * @return this builder
             */
            public android.view.textclassifier.TextLinks.Request.Builder setExtras(@android.annotation.Nullable
            android.os.Bundle extras) {
                mExtras = extras;
                return this;
            }

            /**
             * Builds and returns the request object.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextLinks.Request build() {
                return new android.view.textclassifier.TextLinks.Request(mText, mDefaultLocales, mEntityConfig, mLegacyFallback, mExtras == null ? android.os.Bundle.EMPTY : mExtras);
            }
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(mText.toString());
            dest.writeParcelable(mDefaultLocales, flags);
            dest.writeParcelable(mEntityConfig, flags);
            dest.writeString(mCallingPackageName);
            dest.writeBundle(mExtras);
        }

        private static android.view.textclassifier.TextLinks.Request readFromParcel(android.os.Parcel in) {
            final java.lang.String text = in.readString();
            final android.os.LocaleList defaultLocales = in.readParcelable(null);
            final android.view.textclassifier.TextClassifier.EntityConfig entityConfig = in.readParcelable(null);
            final java.lang.String callingPackageName = in.readString();
            final android.os.Bundle extras = in.readBundle();
            final android.view.textclassifier.TextLinks.Request request = /* legacyFallback= */
            new android.view.textclassifier.TextLinks.Request(text, defaultLocales, entityConfig, true, extras);
            request.setCallingPackageName(callingPackageName);
            return request;
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.textclassifier.TextLinks.Request> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextLinks.Request>() {
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

    /**
     * A ClickableSpan for a TextLink.
     *
     * <p>Applies only to TextViews.
     */
    public static class TextLinkSpan extends android.text.style.ClickableSpan {
        /**
         * How the clickspan is triggered.
         *
         * @unknown 
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_UNSPECIFIED, android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_TOUCH, android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_KEYBOARD })
        public @interface InvocationMethod {}

        /**
         *
         *
         * @unknown 
         */
        public static final int INVOCATION_METHOD_UNSPECIFIED = -1;

        /**
         *
         *
         * @unknown 
         */
        public static final int INVOCATION_METHOD_TOUCH = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int INVOCATION_METHOD_KEYBOARD = 1;

        private final android.view.textclassifier.TextLinks.TextLink mTextLink;

        public TextLinkSpan(@android.annotation.NonNull
        android.view.textclassifier.TextLinks.TextLink textLink) {
            mTextLink = textLink;
        }

        @java.lang.Override
        public void onClick(android.view.View widget) {
            onClick(widget, android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_UNSPECIFIED);
        }

        /**
         *
         *
         * @unknown 
         */
        public final void onClick(android.view.View widget, @android.view.textclassifier.TextLinks.TextLinkSpan.InvocationMethod
        int invocationMethod) {
            if (widget instanceof android.widget.TextView) {
                final android.widget.TextView textView = ((android.widget.TextView) (widget));
                final android.content.Context context = textView.getContext();
                if (android.view.textclassifier.TextClassificationManager.getSettings(context).isSmartLinkifyEnabled()) {
                    switch (invocationMethod) {
                        case android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_TOUCH :
                            textView.requestActionMode(this);
                            break;
                        case android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_KEYBOARD :
                            // fall though
                        case android.view.textclassifier.TextLinks.TextLinkSpan.INVOCATION_METHOD_UNSPECIFIED :
                            // fall through
                        default :
                            textView.handleClick(this);
                            break;
                    }
                } else {
                    if (mTextLink.mUrlSpan != null) {
                        mTextLink.mUrlSpan.onClick(textView);
                    } else {
                        textView.handleClick(this);
                    }
                }
            }
        }

        public final android.view.textclassifier.TextLinks.TextLink getTextLink() {
            return mTextLink;
        }

        /**
         *
         *
         * @unknown 
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PRIVATE)
        @android.annotation.Nullable
        public final java.lang.String getUrl() {
            if (mTextLink.mUrlSpan != null) {
                return mTextLink.mUrlSpan.getURL();
            }
            return null;
        }
    }

    /**
     * A builder to construct a TextLinks instance.
     */
    public static final class Builder {
        private final java.lang.String mFullText;

        private final java.util.ArrayList<android.view.textclassifier.TextLinks.TextLink> mLinks;

        private android.os.Bundle mExtras;

        /**
         * Create a new TextLinks.Builder.
         *
         * @param fullText
         * 		The full text to annotate with links
         */
        public Builder(@android.annotation.NonNull
        java.lang.String fullText) {
            mFullText = com.android.internal.util.Preconditions.checkNotNull(fullText);
            mLinks = new java.util.ArrayList<>();
        }

        /**
         * Adds a TextLink.
         *
         * @param start
         * 		The start index of the identified subsequence
         * @param end
         * 		The end index of the identified subsequence
         * @param entityScores
         * 		A mapping of entity type to confidence score
         * @throws IllegalArgumentException
         * 		if entityScores is null or empty.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLinks.Builder addLink(int start, int end, @android.annotation.NonNull
        java.util.Map<java.lang.String, java.lang.Float> entityScores) {
            return addLink(start, end, entityScores, Bundle.EMPTY, null);
        }

        /**
         * Adds a TextLink.
         *
         * @see #addLink(int, int, Map)
         * @param extras
         * 		An optional bundle containing custom data related to this TextLink
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLinks.Builder addLink(int start, int end, @android.annotation.NonNull
        java.util.Map<java.lang.String, java.lang.Float> entityScores, @android.annotation.NonNull
        android.os.Bundle extras) {
            return addLink(start, end, entityScores, extras, null);
        }

        /**
         *
         *
         * @see #addLink(int, int, Map)
         * @param urlSpan
         * 		An optional URLSpan to delegate to. NOTE: Not parcelled.
         */
        @android.annotation.NonNull
        android.view.textclassifier.TextLinks.Builder addLink(int start, int end, @android.annotation.NonNull
        java.util.Map<java.lang.String, java.lang.Float> entityScores, @android.annotation.Nullable
        android.text.style.URLSpan urlSpan) {
            return addLink(start, end, entityScores, Bundle.EMPTY, urlSpan);
        }

        private android.view.textclassifier.TextLinks.Builder addLink(int start, int end, @android.annotation.NonNull
        java.util.Map<java.lang.String, java.lang.Float> entityScores, @android.annotation.NonNull
        android.os.Bundle extras, @android.annotation.Nullable
        android.text.style.URLSpan urlSpan) {
            mLinks.add(new android.view.textclassifier.TextLinks.TextLink(start, end, new android.view.textclassifier.EntityConfidence(entityScores), extras, urlSpan));
            return this;
        }

        /**
         * Removes all {@link TextLink}s.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLinks.Builder clearTextLinks() {
            mLinks.clear();
            return this;
        }

        /**
         * Sets the extended data.
         *
         * @return this builder
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLinks.Builder setExtras(@android.annotation.Nullable
        android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Constructs a TextLinks instance.
         *
         * @return the constructed TextLinks
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextLinks build() {
            return new android.view.textclassifier.TextLinks(mFullText, mLinks, mExtras == null ? android.os.Bundle.EMPTY : mExtras);
        }
    }
}

