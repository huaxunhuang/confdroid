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
 * Interface for providing text classification related features.
 * <p>
 * The TextClassifier may be used to understand the meaning of text, as well as generating predicted
 * next actions based on the text.
 *
 * <p><strong>NOTE: </strong>Unless otherwise stated, methods of this interface are blocking
 * operations. Call on a worker thread.
 */
public interface TextClassifier {
    /**
     *
     *
     * @unknown 
     */
    java.lang.String DEFAULT_LOG_TAG = "androidtc";

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.TextClassifier.LOCAL, android.view.textclassifier.TextClassifier.SYSTEM })
    @interface TextClassifierType {}

    /**
     * Specifies a TextClassifier that runs locally in the app's process. @hide
     */
    int LOCAL = 0;

    /**
     * Specifies a TextClassifier that runs in the system process and serves all apps. @hide
     */
    int SYSTEM = 1;

    /**
     * The TextClassifier failed to run.
     */
    java.lang.String TYPE_UNKNOWN = "";

    /**
     * The classifier ran, but didn't recognize a known entity.
     */
    java.lang.String TYPE_OTHER = "other";

    /**
     * E-mail address (e.g. "noreply@android.com").
     */
    java.lang.String TYPE_EMAIL = "email";

    /**
     * Phone number (e.g. "555-123 456").
     */
    java.lang.String TYPE_PHONE = "phone";

    /**
     * Physical address.
     */
    java.lang.String TYPE_ADDRESS = "address";

    /**
     * Web URL.
     */
    java.lang.String TYPE_URL = "url";

    /**
     * Time reference that is no more specific than a date. May be absolute such as "01/01/2000" or
     * relative like "tomorrow". *
     */
    java.lang.String TYPE_DATE = "date";

    /**
     * Time reference that includes a specific time. May be absolute such as "01/01/2000 5:30pm" or
     * relative like "tomorrow at 5:30pm". *
     */
    java.lang.String TYPE_DATE_TIME = "datetime";

    /**
     * Flight number in IATA format.
     */
    java.lang.String TYPE_FLIGHT_NUMBER = "flight";

    /**
     * Word that users may be interested to look up for meaning.
     *
     * @unknown 
     */
    java.lang.String TYPE_DICTIONARY = "dictionary";

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef(prefix = { "TYPE_" }, value = { android.view.textclassifier.TextClassifier.TYPE_UNKNOWN, android.view.textclassifier.TextClassifier.TYPE_OTHER, android.view.textclassifier.TextClassifier.TYPE_EMAIL, android.view.textclassifier.TextClassifier.TYPE_PHONE, android.view.textclassifier.TextClassifier.TYPE_ADDRESS, android.view.textclassifier.TextClassifier.TYPE_URL, android.view.textclassifier.TextClassifier.TYPE_DATE, android.view.textclassifier.TextClassifier.TYPE_DATE_TIME, android.view.textclassifier.TextClassifier.TYPE_FLIGHT_NUMBER, android.view.textclassifier.TextClassifier.TYPE_DICTIONARY })
    @interface EntityType {}

    /**
     * Designates that the text in question is editable. *
     */
    java.lang.String HINT_TEXT_IS_EDITABLE = "android.text_is_editable";

    /**
     * Designates that the text in question is not editable. *
     */
    java.lang.String HINT_TEXT_IS_NOT_EDITABLE = "android.text_is_not_editable";

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef(prefix = { "HINT_" }, value = { android.view.textclassifier.TextClassifier.HINT_TEXT_IS_EDITABLE, android.view.textclassifier.TextClassifier.HINT_TEXT_IS_NOT_EDITABLE })
    @interface Hints {}

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef({ android.view.textclassifier.TextClassifier.WIDGET_TYPE_TEXTVIEW, android.view.textclassifier.TextClassifier.WIDGET_TYPE_EDITTEXT, android.view.textclassifier.TextClassifier.WIDGET_TYPE_UNSELECTABLE_TEXTVIEW, android.view.textclassifier.TextClassifier.WIDGET_TYPE_WEBVIEW, android.view.textclassifier.TextClassifier.WIDGET_TYPE_EDIT_WEBVIEW, android.view.textclassifier.TextClassifier.WIDGET_TYPE_CUSTOM_TEXTVIEW, android.view.textclassifier.TextClassifier.WIDGET_TYPE_CUSTOM_EDITTEXT, android.view.textclassifier.TextClassifier.WIDGET_TYPE_CUSTOM_UNSELECTABLE_TEXTVIEW, android.view.textclassifier.TextClassifier.WIDGET_TYPE_NOTIFICATION, android.view.textclassifier.TextClassifier.WIDGET_TYPE_UNKNOWN })
    @interface WidgetType {}

    /**
     * The widget involved in the text classification context is a standard
     * {@link android.widget.TextView}.
     */
    java.lang.String WIDGET_TYPE_TEXTVIEW = "textview";

    /**
     * The widget involved in the text classification context is a standard
     * {@link android.widget.EditText}.
     */
    java.lang.String WIDGET_TYPE_EDITTEXT = "edittext";

    /**
     * The widget involved in the text classification context is a standard non-selectable
     * {@link android.widget.TextView}.
     */
    java.lang.String WIDGET_TYPE_UNSELECTABLE_TEXTVIEW = "nosel-textview";

    /**
     * The widget involved in the text classification context is a standard
     * {@link android.webkit.WebView}.
     */
    java.lang.String WIDGET_TYPE_WEBVIEW = "webview";

    /**
     * The widget involved in the text classification context is a standard editable
     * {@link android.webkit.WebView}.
     */
    java.lang.String WIDGET_TYPE_EDIT_WEBVIEW = "edit-webview";

    /**
     * The widget involved in the text classification context is a custom text widget.
     */
    java.lang.String WIDGET_TYPE_CUSTOM_TEXTVIEW = "customview";

    /**
     * The widget involved in the text classification context is a custom editable text widget.
     */
    java.lang.String WIDGET_TYPE_CUSTOM_EDITTEXT = "customedit";

    /**
     * The widget involved in the text classification context is a custom non-selectable text
     * widget.
     */
    java.lang.String WIDGET_TYPE_CUSTOM_UNSELECTABLE_TEXTVIEW = "nosel-customview";

    /**
     * The widget involved in the text classification context is a notification
     */
    java.lang.String WIDGET_TYPE_NOTIFICATION = "notification";

    /**
     * The widget involved in the text classification context is of an unknown/unspecified type.
     */
    java.lang.String WIDGET_TYPE_UNKNOWN = "unknown";

    /**
     * No-op TextClassifier.
     * This may be used to turn off TextClassifier features.
     */
    android.view.textclassifier.TextClassifier NO_OP = new android.view.textclassifier.TextClassifier() {
        @java.lang.Override
        public java.lang.String toString() {
            return "TextClassifier.NO_OP";
        }
    };

    /**
     * Extra that is included on activity intents coming from a TextClassifier when
     * it suggests actions to its caller.
     * <p>
     * All {@link TextClassifier} implementations should make sure this extra exists in their
     * generated intents.
     */
    java.lang.String EXTRA_FROM_TEXT_CLASSIFIER = "android.view.textclassifier.extra.FROM_TEXT_CLASSIFIER";

    /**
     * Returns suggested text selection start and end indices, recognized entity types, and their
     * associated confidence scores. The entity types are ordered from highest to lowest scoring.
     *
     * <p><strong>NOTE: </strong>Call on a worker thread.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * @param request
     * 		the text selection request
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.TextSelection suggestSelection(@android.annotation.NonNull
    android.view.textclassifier.TextSelection.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        return new android.view.textclassifier.TextSelection.Builder(request.getStartIndex(), request.getEndIndex()).build();
    }

    /**
     * Returns suggested text selection start and end indices, recognized entity types, and their
     * associated confidence scores. The entity types are ordered from highest to lowest scoring.
     *
     * <p><strong>NOTE: </strong>Call on a worker thread.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * <p><b>NOTE:</b> Do not implement. The default implementation of this method calls
     * {@link #suggestSelection(TextSelection.Request)}. If that method calls this method,
     * a stack overflow error will happen.
     *
     * @param text
     * 		text providing context for the selected text (which is specified
     * 		by the sub sequence starting at selectionStartIndex and ending at selectionEndIndex)
     * @param selectionStartIndex
     * 		start index of the selected part of text
     * @param selectionEndIndex
     * 		end index of the selected part of text
     * @param defaultLocales
     * 		ordered list of locale preferences that may be used to
     * 		disambiguate the provided text. If no locale preferences exist, set this to null
     * 		or an empty locale list.
     * @throws IllegalArgumentException
     * 		if text is null; selectionStartIndex is negative;
     * 		selectionEndIndex is greater than text.length() or not greater than selectionStartIndex
     * @see #suggestSelection(TextSelection.Request)
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.TextSelection suggestSelection(@android.annotation.NonNull
    java.lang.CharSequence text, @android.annotation.IntRange(from = 0)
    int selectionStartIndex, @android.annotation.IntRange(from = 0)
    int selectionEndIndex, @android.annotation.Nullable
    android.os.LocaleList defaultLocales) {
        final android.view.textclassifier.TextSelection.Request request = new android.view.textclassifier.TextSelection.Request.Builder(text, selectionStartIndex, selectionEndIndex).setDefaultLocales(defaultLocales).build();
        return suggestSelection(request);
    }

    /**
     * Classifies the specified text and returns a {@link TextClassification} object that can be
     * used to generate a widget for handling the classified text.
     *
     * <p><strong>NOTE: </strong>Call on a worker thread.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * @param request
     * 		the text classification request
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.TextClassification classifyText(@android.annotation.NonNull
    android.view.textclassifier.TextClassification.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        return android.view.textclassifier.TextClassification.EMPTY;
    }

    /**
     * Classifies the specified text and returns a {@link TextClassification} object that can be
     * used to generate a widget for handling the classified text.
     *
     * <p><strong>NOTE: </strong>Call on a worker thread.
     *
     * <p><b>NOTE:</b> Do not implement. The default implementation of this method calls
     * {@link #classifyText(TextClassification.Request)}. If that method calls this method,
     * a stack overflow error will happen.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * @param text
     * 		text providing context for the text to classify (which is specified
     * 		by the sub sequence starting at startIndex and ending at endIndex)
     * @param startIndex
     * 		start index of the text to classify
     * @param endIndex
     * 		end index of the text to classify
     * @param defaultLocales
     * 		ordered list of locale preferences that may be used to
     * 		disambiguate the provided text. If no locale preferences exist, set this to null
     * 		or an empty locale list.
     * @throws IllegalArgumentException
     * 		if text is null; startIndex is negative;
     * 		endIndex is greater than text.length() or not greater than startIndex
     * @see #classifyText(TextClassification.Request)
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.TextClassification classifyText(@android.annotation.NonNull
    java.lang.CharSequence text, @android.annotation.IntRange(from = 0)
    int startIndex, @android.annotation.IntRange(from = 0)
    int endIndex, @android.annotation.Nullable
    android.os.LocaleList defaultLocales) {
        final android.view.textclassifier.TextClassification.Request request = new android.view.textclassifier.TextClassification.Request.Builder(text, startIndex, endIndex).setDefaultLocales(defaultLocales).build();
        return classifyText(request);
    }

    /**
     * Generates and returns a {@link TextLinks} that may be applied to the text to annotate it with
     * links information.
     *
     * <p><strong>NOTE: </strong>Call on a worker thread.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * @param request
     * 		the text links request
     * @see #getMaxGenerateLinksTextLength()
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.TextLinks generateLinks(@android.annotation.NonNull
    android.view.textclassifier.TextLinks.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        return new android.view.textclassifier.TextLinks.Builder(request.getText().toString()).build();
    }

    /**
     * Returns the maximal length of text that can be processed by generateLinks.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * @see #generateLinks(TextLinks.Request)
     */
    @android.annotation.WorkerThread
    default int getMaxGenerateLinksTextLength() {
        return java.lang.Integer.MAX_VALUE;
    }

    /**
     * Detects the language of the text in the given request.
     *
     * <p><strong>NOTE: </strong>Call on a worker thread.
     *
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * @param request
     * 		the {@link TextLanguage} request.
     * @return the {@link TextLanguage} result.
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.TextLanguage detectLanguage(@android.annotation.NonNull
    android.view.textclassifier.TextLanguage.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        return android.view.textclassifier.TextLanguage.EMPTY;
    }

    /**
     * Suggests and returns a list of actions according to the given conversation.
     */
    @android.annotation.WorkerThread
    @android.annotation.NonNull
    default android.view.textclassifier.ConversationActions suggestConversationActions(@android.annotation.NonNull
    android.view.textclassifier.ConversationActions.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        return new android.view.textclassifier.ConversationActions(java.util.Collections.emptyList(), null);
    }

    /**
     * <strong>NOTE: </strong>Use {@link #onTextClassifierEvent(TextClassifierEvent)} instead.
     * <p>
     * Reports a selection event.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to this method should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     */
    default void onSelectionEvent(@android.annotation.NonNull
    android.view.textclassifier.SelectionEvent event) {
        // TODO: Consider rerouting to onTextClassifierEvent()
    }

    /**
     * Reports a text classifier event.
     * <p>
     * <strong>NOTE: </strong>Call on a worker thread.
     *
     * @throws IllegalStateException
     * 		if this TextClassifier has been destroyed.
     * @see #isDestroyed()
     */
    default void onTextClassifierEvent(@android.annotation.NonNull
    android.view.textclassifier.TextClassifierEvent event) {
    }

    /**
     * Destroys this TextClassifier.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, calls to its methods should
     * throw an {@link IllegalStateException}. See {@link #isDestroyed()}.
     *
     * <p>Subsequent calls to this method are no-ops.
     */
    default void destroy() {
    }

    /**
     * Returns whether or not this TextClassifier has been destroyed.
     *
     * <p><strong>NOTE: </strong>If a TextClassifier has been destroyed, caller should not interact
     * with the classifier and an attempt to do so would throw an {@link IllegalStateException}.
     * However, this method should never throw an {@link IllegalStateException}.
     *
     * @see #destroy()
     */
    default boolean isDestroyed() {
        return false;
    }

    /**
     *
     *
     * @unknown *
     */
    default void dump(@android.annotation.NonNull
    com.android.internal.util.IndentingPrintWriter printWriter) {
    }

    /**
     * Configuration object for specifying what entity types to identify.
     *
     * Configs are initially based on a predefined preset, and can be modified from there.
     */
    final class EntityConfig implements android.os.Parcelable {
        private final java.util.List<java.lang.String> mIncludedTypes;

        private final java.util.List<java.lang.String> mExcludedTypes;

        private final java.util.List<java.lang.String> mHints;

        private final boolean mIncludeTypesFromTextClassifier;

        private EntityConfig(java.util.List<java.lang.String> includedEntityTypes, java.util.List<java.lang.String> excludedEntityTypes, java.util.List<java.lang.String> hints, boolean includeTypesFromTextClassifier) {
            mIncludedTypes = com.android.internal.util.Preconditions.checkNotNull(includedEntityTypes);
            mExcludedTypes = com.android.internal.util.Preconditions.checkNotNull(excludedEntityTypes);
            mHints = com.android.internal.util.Preconditions.checkNotNull(hints);
            mIncludeTypesFromTextClassifier = includeTypesFromTextClassifier;
        }

        private EntityConfig(android.os.Parcel in) {
            mIncludedTypes = new java.util.ArrayList<>();
            in.readStringList(mIncludedTypes);
            mExcludedTypes = new java.util.ArrayList<>();
            in.readStringList(mExcludedTypes);
            java.util.List<java.lang.String> tmpHints = new java.util.ArrayList<>();
            in.readStringList(tmpHints);
            mHints = java.util.Collections.unmodifiableList(tmpHints);
            mIncludeTypesFromTextClassifier = in.readByte() != 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel parcel, int flags) {
            parcel.writeStringList(mIncludedTypes);
            parcel.writeStringList(mExcludedTypes);
            parcel.writeStringList(mHints);
            parcel.writeByte(((byte) (mIncludeTypesFromTextClassifier ? 1 : 0)));
        }

        /**
         * Creates an EntityConfig.
         *
         * @param hints
         * 		Hints for the TextClassifier to determine what types of entities to find.
         * @deprecated Use {@link Builder} instead.
         */
        @java.lang.Deprecated
        public static android.view.textclassifier.TextClassifier.EntityConfig createWithHints(@android.annotation.Nullable
        java.util.Collection<java.lang.String> hints) {
            return new android.view.textclassifier.TextClassifier.EntityConfig.Builder().includeTypesFromTextClassifier(true).setHints(hints).build();
        }

        /**
         * Creates an EntityConfig.
         *
         * @param hints
         * 		Hints for the TextClassifier to determine what types of entities to find
         * @param includedEntityTypes
         * 		Entity types, e.g. {@link #TYPE_EMAIL}, to explicitly include
         * @param excludedEntityTypes
         * 		Entity types, e.g. {@link #TYPE_PHONE}, to explicitly exclude
         * 		
         * 		
         * 		Note that if an entity has been excluded, the exclusion will take precedence.
         * @deprecated Use {@link Builder} instead.
         */
        @java.lang.Deprecated
        public static android.view.textclassifier.TextClassifier.EntityConfig create(@android.annotation.Nullable
        java.util.Collection<java.lang.String> hints, @android.annotation.Nullable
        java.util.Collection<java.lang.String> includedEntityTypes, @android.annotation.Nullable
        java.util.Collection<java.lang.String> excludedEntityTypes) {
            return new android.view.textclassifier.TextClassifier.EntityConfig.Builder().setIncludedTypes(includedEntityTypes).setExcludedTypes(excludedEntityTypes).setHints(hints).includeTypesFromTextClassifier(true).build();
        }

        /**
         * Creates an EntityConfig with an explicit entity list.
         *
         * @param entityTypes
         * 		Complete set of entities, e.g. {@link #TYPE_URL} to find.
         * @deprecated Use {@link Builder} instead.
         */
        @java.lang.Deprecated
        public static android.view.textclassifier.TextClassifier.EntityConfig createWithExplicitEntityList(@android.annotation.Nullable
        java.util.Collection<java.lang.String> entityTypes) {
            return new android.view.textclassifier.TextClassifier.EntityConfig.Builder().setIncludedTypes(entityTypes).includeTypesFromTextClassifier(false).build();
        }

        /**
         * Returns a final list of entity types to find.
         *
         * @param entityTypes
         * 		Entity types we think should be found before factoring in
         * 		includes/excludes
         * 		
         * 		This method is intended for use by TextClassifier implementations.
         */
        public java.util.Collection<java.lang.String> resolveEntityListModifications(@android.annotation.NonNull
        java.util.Collection<java.lang.String> entityTypes) {
            final java.util.Set<java.lang.String> finalSet = new java.util.HashSet<>();
            if (mIncludeTypesFromTextClassifier) {
                finalSet.addAll(entityTypes);
            }
            finalSet.addAll(mIncludedTypes);
            finalSet.removeAll(mExcludedTypes);
            return finalSet;
        }

        /**
         * Retrieves the list of hints.
         *
         * @return An unmodifiable collection of the hints.
         */
        public java.util.Collection<java.lang.String> getHints() {
            return mHints;
        }

        /**
         * Return whether the client allows the text classifier to include its own list of
         * default types. If this function returns {@code true}, a default list of types suggested
         * from a text classifier will be taking into account.
         *
         * <p>NOTE: This method is intended for use by a text classifier.
         *
         * @see #resolveEntityListModifications(Collection)
         */
        public boolean shouldIncludeTypesFromTextClassifier() {
            return mIncludeTypesFromTextClassifier;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.textclassifier.TextClassifier.EntityConfig> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextClassifier.EntityConfig>() {
            @java.lang.Override
            public android.view.textclassifier.EntityConfig createFromParcel(android.os.Parcel in) {
                return new android.view.textclassifier.EntityConfig(in);
            }

            @java.lang.Override
            public android.view.textclassifier.EntityConfig[] newArray(int size) {
                return new android.view.textclassifier.EntityConfig[size];
            }
        };

        /**
         * Builder class to construct the {@link EntityConfig} object.
         */
        public static final class Builder {
            @android.annotation.Nullable
            private java.util.Collection<java.lang.String> mIncludedTypes;

            @android.annotation.Nullable
            private java.util.Collection<java.lang.String> mExcludedTypes;

            @android.annotation.Nullable
            private java.util.Collection<java.lang.String> mHints;

            private boolean mIncludeTypesFromTextClassifier = true;

            /**
             * Sets a collection of types that are explicitly included.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifier.EntityConfig.Builder setIncludedTypes(@android.annotation.Nullable
            java.util.Collection<java.lang.String> includedTypes) {
                mIncludedTypes = includedTypes;
                return this;
            }

            /**
             * Sets a collection of types that are explicitly excluded.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifier.EntityConfig.Builder setExcludedTypes(@android.annotation.Nullable
            java.util.Collection<java.lang.String> excludedTypes) {
                mExcludedTypes = excludedTypes;
                return this;
            }

            /**
             * Specifies whether or not to include the types suggested by the text classifier. By
             * default, it is included.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifier.EntityConfig.Builder includeTypesFromTextClassifier(boolean includeTypesFromTextClassifier) {
                mIncludeTypesFromTextClassifier = includeTypesFromTextClassifier;
                return this;
            }

            /**
             * Sets the hints for the TextClassifier to determine what types of entities to find.
             * These hints will only be used if {@link #includeTypesFromTextClassifier} is
             * set to be true.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifier.EntityConfig.Builder setHints(@android.annotation.Nullable
            java.util.Collection<java.lang.String> hints) {
                mHints = hints;
                return this;
            }

            /**
             * Combines all of the options that have been set and returns a new {@link EntityConfig}
             * object.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifier.EntityConfig build() {
                return new android.view.textclassifier.TextClassifier.EntityConfig(mIncludedTypes == null ? java.util.Collections.emptyList() : new java.util.ArrayList<>(mIncludedTypes), mExcludedTypes == null ? java.util.Collections.emptyList() : new java.util.ArrayList<>(mExcludedTypes), mHints == null ? java.util.Collections.emptyList() : java.util.Collections.unmodifiableList(new java.util.ArrayList<>(mHints)), mIncludeTypesFromTextClassifier);
            }
        }
    }

    /**
     * Utility functions for TextClassifier methods.
     *
     * <ul>
     *  <li>Provides validation of input parameters to TextClassifier methods
     * </ul>
     *
     * Intended to be used only for TextClassifier purposes.
     *
     * @unknown 
     */
    final class Utils {
        @com.android.internal.annotations.GuardedBy("WORD_ITERATOR")
        private static final java.text.BreakIterator WORD_ITERATOR = java.text.BreakIterator.getWordInstance();

        /**
         *
         *
         * @throws IllegalArgumentException
         * 		if text is null; startIndex is negative;
         * 		endIndex is greater than text.length() or is not greater than startIndex;
         * 		options is null
         */
        static void checkArgument(@android.annotation.NonNull
        java.lang.CharSequence text, int startIndex, int endIndex) {
            com.android.internal.util.Preconditions.checkArgument(text != null);
            com.android.internal.util.Preconditions.checkArgument(startIndex >= 0);
            com.android.internal.util.Preconditions.checkArgument(endIndex <= text.length());
            com.android.internal.util.Preconditions.checkArgument(endIndex > startIndex);
        }

        static void checkTextLength(java.lang.CharSequence text, int maxLength) {
            com.android.internal.util.Preconditions.checkArgumentInRange(text.length(), 0, maxLength, "text.length()");
        }

        /**
         * Returns the substring of {@code text} that contains at least text from index
         * {@code start} <i>(inclusive)</i> to index {@code end} <i><(exclusive)/i> with the goal of
         * returning text that is at least {@code minimumLength}. If {@code text} is not long
         * enough, this will return {@code text}. This method returns text at word boundaries.
         *
         * @param text
         * 		the source text
         * @param start
         * 		the start index of text that must be included
         * @param end
         * 		the end index of text that must be included
         * @param minimumLength
         * 		minimum length of text to return if {@code text} is long enough
         */
        public static java.lang.String getSubString(java.lang.String text, int start, int end, int minimumLength) {
            com.android.internal.util.Preconditions.checkArgument(start >= 0);
            com.android.internal.util.Preconditions.checkArgument(end <= text.length());
            com.android.internal.util.Preconditions.checkArgument(start <= end);
            if (text.length() < minimumLength) {
                return text;
            }
            final int length = end - start;
            if (length >= minimumLength) {
                return text.substring(start, end);
            }
            final int offset = (minimumLength - length) / 2;
            int iterStart = java.lang.Math.max(0, java.lang.Math.min(start - offset, text.length() - minimumLength));
            int iterEnd = java.lang.Math.min(text.length(), iterStart + minimumLength);
            synchronized(android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR) {
                android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR.setText(text);
                iterStart = (android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR.isBoundary(iterStart)) ? iterStart : java.lang.Math.max(0, android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR.preceding(iterStart));
                iterEnd = (android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR.isBoundary(iterEnd)) ? iterEnd : java.lang.Math.max(iterEnd, android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR.following(iterEnd));
                android.view.textclassifier.TextClassifier.Utils.WORD_ITERATOR.setText("");
                return text.substring(iterStart, iterEnd);
            }
        }

        /**
         * Generates links using legacy {@link Linkify}.
         */
        public static android.view.textclassifier.TextLinks generateLegacyLinks(@android.annotation.NonNull
        android.view.textclassifier.TextLinks.Request request) {
            final java.lang.String string = request.getText().toString();
            final android.view.textclassifier.TextLinks.Builder links = new android.view.textclassifier.TextLinks.Builder(string);
            final java.util.Collection<java.lang.String> entities = request.getEntityConfig().resolveEntityListModifications(java.util.Collections.emptyList());
            if (entities.contains(android.view.textclassifier.TextClassifier.TYPE_URL)) {
                android.view.textclassifier.TextClassifier.Utils.addLinks(links, string, android.view.textclassifier.TextClassifier.TYPE_URL);
            }
            if (entities.contains(android.view.textclassifier.TextClassifier.TYPE_PHONE)) {
                android.view.textclassifier.TextClassifier.Utils.addLinks(links, string, android.view.textclassifier.TextClassifier.TYPE_PHONE);
            }
            if (entities.contains(android.view.textclassifier.TextClassifier.TYPE_EMAIL)) {
                android.view.textclassifier.TextClassifier.Utils.addLinks(links, string, android.view.textclassifier.TextClassifier.TYPE_EMAIL);
            }
            // NOTE: Do not support MAP_ADDRESSES. Legacy version does not work well.
            return links.build();
        }

        private static void addLinks(android.view.textclassifier.TextLinks.Builder links, java.lang.String string, @android.view.textclassifier.TextClassifier.EntityType
        java.lang.String entityType) {
            final android.text.Spannable spannable = new android.text.SpannableString(string);
            if (android.text.util.Linkify.addLinks(spannable, android.view.textclassifier.TextClassifier.Utils.linkMask(entityType))) {
                final android.text.style.URLSpan[] spans = spannable.getSpans(0, spannable.length(), android.text.style.URLSpan.class);
                for (android.text.style.URLSpan urlSpan : spans) {
                    links.addLink(spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), android.view.textclassifier.TextClassifier.Utils.entityScores(entityType), urlSpan);
                }
            }
        }

        @android.text.util.Linkify.LinkifyMask
        private static int linkMask(@android.view.textclassifier.TextClassifier.EntityType
        java.lang.String entityType) {
            switch (entityType) {
                case android.view.textclassifier.TextClassifier.TYPE_URL :
                    return android.text.util.Linkify.WEB_URLS;
                case android.view.textclassifier.TextClassifier.TYPE_PHONE :
                    return android.text.util.Linkify.PHONE_NUMBERS;
                case android.view.textclassifier.TextClassifier.TYPE_EMAIL :
                    return android.text.util.Linkify.EMAIL_ADDRESSES;
                default :
                    // NOTE: Do not support MAP_ADDRESSES. Legacy version does not work well.
                    return 0;
            }
        }

        private static java.util.Map<java.lang.String, java.lang.Float> entityScores(@android.view.textclassifier.TextClassifier.EntityType
        java.lang.String entityType) {
            final java.util.Map<java.lang.String, java.lang.Float> scores = new android.util.ArrayMap();
            scores.put(entityType, 1.0F);
            return scores;
        }

        static void checkMainThread() {
            if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
                android.view.textclassifier.Log.w(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "TextClassifier called on main thread");
            }
        }
    }
}

