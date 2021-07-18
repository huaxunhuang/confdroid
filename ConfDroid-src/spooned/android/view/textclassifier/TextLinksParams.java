/**
 * Copyright 2018 The Android Open Source Project
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
 * Parameters for generating and applying links.
 *
 * @unknown 
 */
public final class TextLinksParams {
    /**
     * A function to create spans from TextLinks.
     */
    private static final java.util.function.Function<android.view.textclassifier.TextLinks.TextLink, android.view.textclassifier.TextLinks.TextLinkSpan> DEFAULT_SPAN_FACTORY = ( textLink) -> new android.view.textclassifier.TextLinks.TextLinkSpan(textLink);

    @android.view.textclassifier.TextLinks.ApplyStrategy
    private final int mApplyStrategy;

    private final java.util.function.Function<android.view.textclassifier.TextLinks.TextLink, android.view.textclassifier.TextLinks.TextLinkSpan> mSpanFactory;

    private final android.view.textclassifier.TextClassifier.EntityConfig mEntityConfig;

    private TextLinksParams(@android.view.textclassifier.TextLinks.ApplyStrategy
    int applyStrategy, java.util.function.Function<android.view.textclassifier.TextLinks.TextLink, android.view.textclassifier.TextLinks.TextLinkSpan> spanFactory) {
        mApplyStrategy = applyStrategy;
        mSpanFactory = spanFactory;
        mEntityConfig = android.view.textclassifier.TextClassifier.EntityConfig.createWithHints(null);
    }

    /**
     * Returns a new TextLinksParams object based on the specified link mask.
     *
     * @param mask
     * 		the link mask
     * 		e.g. {@link LinkifyMask#PHONE_NUMBERS} | {@link LinkifyMask#EMAIL_ADDRESSES}
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.view.textclassifier.TextLinksParams fromLinkMask(@android.text.util.Linkify.LinkifyMask
    int mask) {
        final java.util.List<java.lang.String> entitiesToFind = new java.util.ArrayList<>();
        if ((mask & android.text.util.Linkify.WEB_URLS) != 0) {
            entitiesToFind.add(android.view.textclassifier.TextClassifier.TYPE_URL);
        }
        if ((mask & android.text.util.Linkify.EMAIL_ADDRESSES) != 0) {
            entitiesToFind.add(android.view.textclassifier.TextClassifier.TYPE_EMAIL);
        }
        if ((mask & android.text.util.Linkify.PHONE_NUMBERS) != 0) {
            entitiesToFind.add(android.view.textclassifier.TextClassifier.TYPE_PHONE);
        }
        if ((mask & android.text.util.Linkify.MAP_ADDRESSES) != 0) {
            entitiesToFind.add(android.view.textclassifier.TextClassifier.TYPE_ADDRESS);
        }
        return new android.view.textclassifier.TextLinksParams.Builder().setEntityConfig(android.view.textclassifier.TextClassifier.EntityConfig.createWithExplicitEntityList(entitiesToFind)).build();
    }

    /**
     * Returns the entity config used to determine what entity types to generate.
     */
    @android.annotation.NonNull
    public android.view.textclassifier.TextClassifier.EntityConfig getEntityConfig() {
        return mEntityConfig;
    }

    /**
     * Annotates the given text with the generated links. It will fail if the provided text doesn't
     * match the original text used to crete the TextLinks.
     *
     * @param text
     * 		the text to apply the links to. Must match the original text
     * @param textLinks
     * 		the links to apply to the text
     * @return a status code indicating whether or not the links were successfully applied
     * @unknown 
     */
    @android.view.textclassifier.TextLinks.Status
    public int apply(@android.annotation.NonNull
    android.text.Spannable text, @android.annotation.NonNull
    android.view.textclassifier.TextLinks textLinks) {
        com.android.internal.util.Preconditions.checkNotNull(text);
        com.android.internal.util.Preconditions.checkNotNull(textLinks);
        final java.lang.String textString = text.toString();
        if (android.text.util.Linkify.containsUnsupportedCharacters(textString)) {
            // Do not apply links to text containing unsupported characters.
            android.view.textclassifier.android.util.EventLog.writeEvent(0x534e4554, "116321860", -1, "");
            return android.view.textclassifier.TextLinks.STATUS_UNSUPPORTED_CHARACTER;
        }
        if (!textString.startsWith(textLinks.getText())) {
            return android.view.textclassifier.TextLinks.STATUS_DIFFERENT_TEXT;
        }
        if (textLinks.getLinks().isEmpty()) {
            return android.view.textclassifier.TextLinks.STATUS_NO_LINKS_FOUND;
        }
        int applyCount = 0;
        for (android.view.textclassifier.TextLinks.TextLink link : textLinks.getLinks()) {
            final android.view.textclassifier.TextLinks.TextLinkSpan span = mSpanFactory.apply(link);
            if (span != null) {
                final android.text.style.ClickableSpan[] existingSpans = text.getSpans(link.getStart(), link.getEnd(), android.text.style.ClickableSpan.class);
                if (existingSpans.length > 0) {
                    if (mApplyStrategy == android.view.textclassifier.TextLinks.APPLY_STRATEGY_REPLACE) {
                        for (android.text.style.ClickableSpan existingSpan : existingSpans) {
                            text.removeSpan(existingSpan);
                        }
                        text.setSpan(span, link.getStart(), link.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        applyCount++;
                    }
                } else {
                    text.setSpan(span, link.getStart(), link.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    applyCount++;
                }
            }
        }
        if (applyCount == 0) {
            return android.view.textclassifier.TextLinks.STATUS_NO_LINKS_APPLIED;
        }
        return android.view.textclassifier.TextLinks.STATUS_LINKS_APPLIED;
    }

    /**
     * A builder for building TextLinksParams.
     */
    public static final class Builder {
        @android.view.textclassifier.TextLinks.ApplyStrategy
        private int mApplyStrategy = android.view.textclassifier.TextLinks.APPLY_STRATEGY_IGNORE;

        private java.util.function.Function<android.view.textclassifier.TextLinks.TextLink, android.view.textclassifier.TextLinks.TextLinkSpan> mSpanFactory = android.view.textclassifier.TextLinksParams.DEFAULT_SPAN_FACTORY;

        /**
         * Sets the apply strategy used to determine how to apply links to text.
         *      e.g {@link TextLinks#APPLY_STRATEGY_IGNORE}
         *
         * @return this builder
         */
        public android.view.textclassifier.TextLinksParams.Builder setApplyStrategy(@android.view.textclassifier.TextLinks.ApplyStrategy
        int applyStrategy) {
            mApplyStrategy = android.view.textclassifier.TextLinksParams.checkApplyStrategy(applyStrategy);
            return this;
        }

        /**
         * Sets a custom span factory for converting TextLinks to TextLinkSpans.
         * Set to {@code null} to use the default span factory.
         *
         * @return this builder
         */
        public android.view.textclassifier.TextLinksParams.Builder setSpanFactory(@android.annotation.Nullable
        java.util.function.Function<android.view.textclassifier.TextLinks.TextLink, android.view.textclassifier.TextLinks.TextLinkSpan> spanFactory) {
            mSpanFactory = (spanFactory == null) ? android.view.textclassifier.TextLinksParams.DEFAULT_SPAN_FACTORY : spanFactory;
            return this;
        }

        /**
         * Sets the entity configuration used to determine what entity types to generate.
         * Set to {@code null} for the default entity config which will automatically determine
         * what links to generate.
         *
         * @return this builder
         */
        public android.view.textclassifier.TextLinksParams.Builder setEntityConfig(@android.annotation.Nullable
        android.view.textclassifier.TextClassifier.EntityConfig entityConfig) {
            return this;
        }

        /**
         * Builds and returns a TextLinksParams object.
         */
        public android.view.textclassifier.TextLinksParams build() {
            return new android.view.textclassifier.TextLinksParams(mApplyStrategy, mSpanFactory);
        }
    }

    /**
     *
     *
     * @throws IllegalArgumentException
     * 		if the value is invalid
     */
    @android.view.textclassifier.TextLinks.ApplyStrategy
    private static int checkApplyStrategy(int applyStrategy) {
        if ((applyStrategy != android.view.textclassifier.TextLinks.APPLY_STRATEGY_IGNORE) && (applyStrategy != android.view.textclassifier.TextLinks.APPLY_STRATEGY_REPLACE)) {
            throw new java.lang.IllegalArgumentException("Invalid apply strategy. See TextLinksParams.ApplyStrategy for options.");
        }
        return applyStrategy;
    }
}

