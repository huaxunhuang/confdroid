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
 * Information for generating a widget to handle classified text.
 *
 * <p>A TextClassification object contains icons, labels, onClickListeners and intents that may
 * be used to build a widget that can be used to act on classified text. There is the concept of a
 * <i>primary action</i> and other <i>secondary actions</i>.
 *
 * <p>e.g. building a view that, when clicked, shares the classified text with the preferred app:
 *
 * <pre>{@code // Called preferably outside the UiThread.
 *   TextClassification classification = textClassifier.classifyText(allText, 10, 25);
 *
 *   // Called on the UiThread.
 *   Button button = new Button(context);
 *   button.setCompoundDrawablesWithIntrinsicBounds(classification.getIcon(), null, null, null);
 *   button.setText(classification.getLabel());
 *   button.setOnClickListener(v -> classification.getActions().get(0).getActionIntent().send());}</pre>
 *
 * <p>e.g. starting an action mode with menu items that can handle the classified text:
 *
 * <pre>{@code // Called preferably outside the UiThread.
 *   final TextClassification classification = textClassifier.classifyText(allText, 10, 25);
 *
 *   // Called on the UiThread.
 *   view.startActionMode(new ActionMode.Callback() {
 *
 *       public boolean onCreateActionMode(ActionMode mode, Menu menu) {
 *           for (int i = 0; i < classification.getActions().size(); ++i) {
 *              RemoteAction action = classification.getActions().get(i);
 *              menu.add(Menu.NONE, i, 20, action.getTitle())
 *                 .setIcon(action.getIcon());}
 *           return true;
 *       }
 *
 *       public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
 *           classification.getActions().get(item.getItemId()).getActionIntent().send();
 *           return true;
 *       }
 *
 *       ...
 *   });
 * }</pre>
 */
public final class TextClassification implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static final android.view.textclassifier.TextClassification EMPTY = new android.view.textclassifier.TextClassification.Builder().build();

    private static final java.lang.String LOG_TAG = "TextClassification";

    // TODO(toki): investigate a way to derive this based on device properties.
    private static final int MAX_LEGACY_ICON_SIZE = 192;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.TextClassification.IntentType.UNSUPPORTED, android.view.textclassifier.TextClassification.IntentType.ACTIVITY, android.view.textclassifier.TextClassification.IntentType.SERVICE })
    private @interface IntentType {
        int UNSUPPORTED = -1;

        int ACTIVITY = 0;

        int SERVICE = 1;
    }

    @android.annotation.NonNull
    private final java.lang.String mText;

    @android.annotation.Nullable
    private final android.graphics.drawable.Drawable mLegacyIcon;

    @android.annotation.Nullable
    private final java.lang.String mLegacyLabel;

    @android.annotation.Nullable
    private final android.content.Intent mLegacyIntent;

    @android.annotation.Nullable
    private final android.view.View.OnClickListener mLegacyOnClickListener;

    @android.annotation.NonNull
    private final java.util.List<android.app.RemoteAction> mActions;

    @android.annotation.NonNull
    private final android.view.textclassifier.EntityConfidence mEntityConfidence;

    @android.annotation.Nullable
    private final java.lang.String mId;

    @android.annotation.NonNull
    private final android.os.Bundle mExtras;

    private TextClassification(@android.annotation.Nullable
    java.lang.String text, @android.annotation.Nullable
    android.graphics.drawable.Drawable legacyIcon, @android.annotation.Nullable
    java.lang.String legacyLabel, @android.annotation.Nullable
    android.content.Intent legacyIntent, @android.annotation.Nullable
    android.view.View.OnClickListener legacyOnClickListener, @android.annotation.NonNull
    java.util.List<android.app.RemoteAction> actions, @android.annotation.NonNull
    android.view.textclassifier.EntityConfidence entityConfidence, @android.annotation.Nullable
    java.lang.String id, @android.annotation.NonNull
    android.os.Bundle extras) {
        mText = text;
        mLegacyIcon = legacyIcon;
        mLegacyLabel = legacyLabel;
        mLegacyIntent = legacyIntent;
        mLegacyOnClickListener = legacyOnClickListener;
        mActions = java.util.Collections.unmodifiableList(actions);
        mEntityConfidence = com.android.internal.util.Preconditions.checkNotNull(entityConfidence);
        mId = id;
        mExtras = extras;
    }

    /**
     * Gets the classified text.
     */
    @android.annotation.Nullable
    public java.lang.String getText() {
        return mText;
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
     * Returns a list of actions that may be performed on the text. The list is ordered based on
     * the likelihood that a user will use the action, with the most likely action appearing first.
     */
    public java.util.List<android.app.RemoteAction> getActions() {
        return mActions;
    }

    /**
     * Returns an icon that may be rendered on a widget used to act on the classified text.
     *
     * <p><strong>NOTE: </strong>This field is not parcelable and only represents the icon of the
     * first {@link RemoteAction} (if one exists) when this object is read from a parcel.
     *
     * @deprecated Use {@link #getActions()} instead.
     */
    @java.lang.Deprecated
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getIcon() {
        return mLegacyIcon;
    }

    /**
     * Returns a label that may be rendered on a widget used to act on the classified text.
     *
     * <p><strong>NOTE: </strong>This field is not parcelable and only represents the label of the
     * first {@link RemoteAction} (if one exists) when this object is read from a parcel.
     *
     * @deprecated Use {@link #getActions()} instead.
     */
    @java.lang.Deprecated
    @android.annotation.Nullable
    public java.lang.CharSequence getLabel() {
        return mLegacyLabel;
    }

    /**
     * Returns an intent that may be fired to act on the classified text.
     *
     * <p><strong>NOTE: </strong>This field is not parcelled and will always return null when this
     * object is read from a parcel.
     *
     * @deprecated Use {@link #getActions()} instead.
     */
    @java.lang.Deprecated
    @android.annotation.Nullable
    public android.content.Intent getIntent() {
        return mLegacyIntent;
    }

    /**
     * Returns the OnClickListener that may be triggered to act on the classified text.
     *
     * <p><strong>NOTE: </strong>This field is not parcelable and only represents the first
     * {@link RemoteAction} (if one exists) when this object is read from a parcel.
     *
     * @deprecated Use {@link #getActions()} instead.
     */
    @android.annotation.Nullable
    public android.view.View.OnClickListener getOnClickListener() {
        return mLegacyOnClickListener;
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
        return java.lang.String.format(java.util.Locale.US, "TextClassification {text=%s, entities=%s, actions=%s, id=%s, extras=%s}", mText, mEntityConfidence, mActions, mId, mExtras);
    }

    /**
     * Creates an OnClickListener that triggers the specified PendingIntent.
     *
     * @unknown 
     */
    public static android.view.View.OnClickListener createIntentOnClickListener(@android.annotation.NonNull
    final android.app.PendingIntent intent) {
        com.android.internal.util.Preconditions.checkNotNull(intent);
        return ( v) -> {
            try {
                intent.send();
            } catch (android.app.PendingIntent e) {
                android.view.textclassifier.Log.e(android.view.textclassifier.TextClassification.LOG_TAG, "Error sending PendingIntent", e);
            }
        };
    }

    /**
     * Creates a PendingIntent for the specified intent.
     * Returns null if the intent is not supported for the specified context.
     *
     * @throws IllegalArgumentException
     * 		if context or intent is null
     * @unknown 
     */
    public static android.app.PendingIntent createPendingIntent(@android.annotation.NonNull
    final android.content.Context context, @android.annotation.NonNull
    final android.content.Intent intent, int requestCode) {
        return android.app.PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Builder for building {@link TextClassification} objects.
     *
     * <p>e.g.
     *
     * <pre>{@code TextClassification classification = new TextClassification.Builder()
     *          .setText(classifiedText)
     *          .setEntityType(TextClassifier.TYPE_EMAIL, 0.9)
     *          .setEntityType(TextClassifier.TYPE_OTHER, 0.1)
     *          .addAction(remoteAction1)
     *          .addAction(remoteAction2)
     *          .build();}</pre>
     */
    public static final class Builder {
        @android.annotation.NonNull
        private java.util.List<android.app.RemoteAction> mActions = new java.util.ArrayList<>();

        @android.annotation.NonNull
        private final java.util.Map<java.lang.String, java.lang.Float> mTypeScoreMap = new android.util.ArrayMap();

        @android.annotation.NonNull
        private final java.util.Map<java.lang.String, com.google.android.textclassifier.AnnotatorModel.ClassificationResult> mClassificationResults = new android.util.ArrayMap();

        @android.annotation.Nullable
        private java.lang.String mText;

        @android.annotation.Nullable
        private android.graphics.drawable.Drawable mLegacyIcon;

        @android.annotation.Nullable
        private java.lang.String mLegacyLabel;

        @android.annotation.Nullable
        private android.content.Intent mLegacyIntent;

        @android.annotation.Nullable
        private android.view.View.OnClickListener mLegacyOnClickListener;

        @android.annotation.Nullable
        private java.lang.String mId;

        @android.annotation.Nullable
        private android.os.Bundle mExtras;

        @android.annotation.NonNull
        private final java.util.ArrayList<android.content.Intent> mActionIntents = new java.util.ArrayList<>();

        @android.annotation.Nullable
        private android.os.Bundle mForeignLanguageExtra;

        /**
         * Sets the classified text.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setText(@android.annotation.Nullable
        java.lang.String text) {
            mText = text;
            return this;
        }

        /**
         * Sets an entity type for the classification result and assigns a confidence score.
         * If a confidence score had already been set for the specified entity type, this will
         * override that score.
         *
         * @param confidenceScore
         * 		a value from 0 (low confidence) to 1 (high confidence).
         * 		0 implies the entity does not exist for the classified text.
         * 		Values greater than 1 are clamped to 1.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setEntityType(@android.annotation.NonNull
        @android.view.textclassifier.TextClassifier.EntityType
        java.lang.String type, @android.annotation.FloatRange(from = 0.0, to = 1.0)
        float confidenceScore) {
            setEntityType(type, confidenceScore, null);
            return this;
        }

        /**
         *
         *
         * @see #setEntityType(String, float)
         * @unknown 
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setEntityType(com.google.android.textclassifier.AnnotatorModel.ClassificationResult classificationResult) {
            setEntityType(classificationResult.getCollection(), classificationResult.getScore(), classificationResult);
            return this;
        }

        /**
         *
         *
         * @see #setEntityType(String, float)
         * @unknown 
         */
        @android.annotation.NonNull
        private android.view.textclassifier.TextClassification.Builder setEntityType(@android.annotation.NonNull
        @android.view.textclassifier.TextClassifier.EntityType
        java.lang.String type, @android.annotation.FloatRange(from = 0.0, to = 1.0)
        float confidenceScore, @android.annotation.Nullable
        com.google.android.textclassifier.AnnotatorModel.ClassificationResult classificationResult) {
            mTypeScoreMap.put(type, confidenceScore);
            mClassificationResults.put(type, classificationResult);
            return this;
        }

        /**
         * Adds an action that may be performed on the classified text. Actions should be added in
         * order of likelihood that the user will use them, with the most likely action being added
         * first.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder addAction(@android.annotation.NonNull
        android.app.RemoteAction action) {
            return addAction(action, null);
        }

        /**
         *
         *
         * @param intent
         * 		the intent in the remote action.
         * @see #addAction(RemoteAction)
         * @unknown 
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public android.view.textclassifier.TextClassification.Builder addAction(android.app.RemoteAction action, @android.annotation.Nullable
        android.content.Intent intent) {
            com.android.internal.util.Preconditions.checkArgument(action != null);
            mActions.add(action);
            mActionIntents.add(intent);
            return this;
        }

        /**
         * Sets the icon for the <i>primary</i> action that may be rendered on a widget used to act
         * on the classified text.
         *
         * <p><strong>NOTE: </strong>This field is not parcelled. If read from a parcel, the
         * returned icon represents the icon of the first {@link RemoteAction} (if one exists).
         *
         * @deprecated Use {@link #addAction(RemoteAction)} instead.
         */
        @java.lang.Deprecated
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setIcon(@android.annotation.Nullable
        android.graphics.drawable.Drawable icon) {
            mLegacyIcon = icon;
            return this;
        }

        /**
         * Sets the label for the <i>primary</i> action that may be rendered on a widget used to
         * act on the classified text.
         *
         * <p><strong>NOTE: </strong>This field is not parcelled. If read from a parcel, the
         * returned label represents the label of the first {@link RemoteAction} (if one exists).
         *
         * @deprecated Use {@link #addAction(RemoteAction)} instead.
         */
        @java.lang.Deprecated
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setLabel(@android.annotation.Nullable
        java.lang.String label) {
            mLegacyLabel = label;
            return this;
        }

        /**
         * Sets the intent for the <i>primary</i> action that may be fired to act on the classified
         * text.
         *
         * <p><strong>NOTE: </strong>This field is not parcelled.
         *
         * @deprecated Use {@link #addAction(RemoteAction)} instead.
         */
        @java.lang.Deprecated
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setIntent(@android.annotation.Nullable
        android.content.Intent intent) {
            mLegacyIntent = intent;
            return this;
        }

        /**
         * Sets the OnClickListener for the <i>primary</i> action that may be triggered to act on
         * the classified text.
         *
         * <p><strong>NOTE: </strong>This field is not parcelable. If read from a parcel, the
         * returned OnClickListener represents the first {@link RemoteAction} (if one exists).
         *
         * @deprecated Use {@link #addAction(RemoteAction)} instead.
         */
        @java.lang.Deprecated
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setOnClickListener(@android.annotation.Nullable
        android.view.View.OnClickListener onClickListener) {
            mLegacyOnClickListener = onClickListener;
            return this;
        }

        /**
         * Sets an id for the TextClassification object.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setId(@android.annotation.Nullable
        java.lang.String id) {
            mId = id;
            return this;
        }

        /**
         * Sets the extended data.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification.Builder setExtras(@android.annotation.Nullable
        android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         *
         *
         * @see #setExtras(Bundle)
         * @unknown 
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public android.view.textclassifier.TextClassification.Builder setForeignLanguageExtra(@android.annotation.Nullable
        android.os.Bundle extra) {
            mForeignLanguageExtra = extra;
            return this;
        }

        /**
         * Builds and returns a {@link TextClassification} object.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassification build() {
            android.view.textclassifier.EntityConfidence entityConfidence = new android.view.textclassifier.EntityConfidence(mTypeScoreMap);
            return new android.view.textclassifier.TextClassification(mText, mLegacyIcon, mLegacyLabel, mLegacyIntent, mLegacyOnClickListener, mActions, entityConfidence, mId, buildExtras(entityConfidence));
        }

        private android.os.Bundle buildExtras(android.view.textclassifier.EntityConfidence entityConfidence) {
            final android.os.Bundle extras = (mExtras == null) ? new android.os.Bundle() : mExtras;
            if (mActionIntents.stream().anyMatch(java.util.Objects::nonNull)) {
                android.view.textclassifier.ExtrasUtils.putActionsIntents(extras, mActionIntents);
            }
            if (mForeignLanguageExtra != null) {
                android.view.textclassifier.ExtrasUtils.putForeignLanguageExtra(extras, mForeignLanguageExtra);
            }
            java.util.List<java.lang.String> sortedTypes = entityConfidence.getEntities();
            java.util.ArrayList<com.google.android.textclassifier.AnnotatorModel.ClassificationResult> sortedEntities = new java.util.ArrayList<>();
            for (java.lang.String type : sortedTypes) {
                sortedEntities.add(mClassificationResults.get(type));
            }
            android.view.textclassifier.ExtrasUtils.putEntities(extras, sortedEntities.toArray(new com.google.android.textclassifier.AnnotatorModel.ClassificationResult[0]));
            return extras.isEmpty() ? android.os.Bundle.EMPTY : extras;
        }
    }

    /**
     * A request object for generating TextClassification.
     */
    public static final class Request implements android.os.Parcelable {
        private final java.lang.CharSequence mText;

        private final int mStartIndex;

        private final int mEndIndex;

        @android.annotation.Nullable
        private final android.os.LocaleList mDefaultLocales;

        @android.annotation.Nullable
        private final java.time.ZonedDateTime mReferenceTime;

        @android.annotation.NonNull
        private final android.os.Bundle mExtras;

        @android.annotation.Nullable
        private java.lang.String mCallingPackageName;

        private Request(java.lang.CharSequence text, int startIndex, int endIndex, android.os.LocaleList defaultLocales, java.time.ZonedDateTime referenceTime, android.os.Bundle extras) {
            mText = text;
            mStartIndex = startIndex;
            mEndIndex = endIndex;
            mDefaultLocales = defaultLocales;
            mReferenceTime = referenceTime;
            mExtras = extras;
        }

        /**
         * Returns the text providing context for the text to classify (which is specified
         *      by the sub sequence starting at startIndex and ending at endIndex)
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getText() {
            return mText;
        }

        /**
         * Returns start index of the text to classify.
         */
        @android.annotation.IntRange(from = 0)
        public int getStartIndex() {
            return mStartIndex;
        }

        /**
         * Returns end index of the text to classify.
         */
        @android.annotation.IntRange(from = 0)
        public int getEndIndex() {
            return mEndIndex;
        }

        /**
         *
         *
         * @return ordered list of locale preferences that can be used to disambiguate
        the provided text.
         */
        @android.annotation.Nullable
        public android.os.LocaleList getDefaultLocales() {
            return mDefaultLocales;
        }

        /**
         *
         *
         * @return reference time based on which relative dates (e.g. "tomorrow") should be
        interpreted.
         */
        @android.annotation.Nullable
        public java.time.ZonedDateTime getReferenceTime() {
            return mReferenceTime;
        }

        /**
         * Sets the name of the package that is sending this request.
         * <p>
         * For SystemTextClassifier's use.
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
         * A builder for building TextClassification requests.
         */
        public static final class Builder {
            private final java.lang.CharSequence mText;

            private final int mStartIndex;

            private final int mEndIndex;

            private android.os.Bundle mExtras;

            @android.annotation.Nullable
            private android.os.LocaleList mDefaultLocales;

            @android.annotation.Nullable
            private java.time.ZonedDateTime mReferenceTime;

            /**
             *
             *
             * @param text
             * 		text providing context for the text to classify (which is specified
             * 		by the sub sequence starting at startIndex and ending at endIndex)
             * @param startIndex
             * 		start index of the text to classify
             * @param endIndex
             * 		end index of the text to classify
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
             * @return this builder
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassification.Request.Builder setDefaultLocales(@android.annotation.Nullable
            android.os.LocaleList defaultLocales) {
                mDefaultLocales = defaultLocales;
                return this;
            }

            /**
             *
             *
             * @param referenceTime
             * 		reference time based on which relative dates (e.g. "tomorrow"
             * 		should be interpreted. This should usually be the time when the text was
             * 		originally composed. If no reference time is set, now is used.
             * @return this builder
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassification.Request.Builder setReferenceTime(@android.annotation.Nullable
            java.time.ZonedDateTime referenceTime) {
                mReferenceTime = referenceTime;
                return this;
            }

            /**
             * Sets the extended data.
             *
             * @return this builder
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassification.Request.Builder setExtras(@android.annotation.Nullable
            android.os.Bundle extras) {
                mExtras = extras;
                return this;
            }

            /**
             * Builds and returns the request object.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassification.Request build() {
                return new android.view.textclassifier.TextClassification.Request(new android.text.SpannedString(mText), mStartIndex, mEndIndex, mDefaultLocales, mReferenceTime, mExtras == null ? android.os.Bundle.EMPTY : mExtras);
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
            dest.writeString(mReferenceTime == null ? null : mReferenceTime.toString());
            dest.writeString(mCallingPackageName);
            dest.writeBundle(mExtras);
        }

        private static android.view.textclassifier.TextClassification.Request readFromParcel(android.os.Parcel in) {
            final java.lang.CharSequence text = in.readCharSequence();
            final int startIndex = in.readInt();
            final int endIndex = in.readInt();
            final android.os.LocaleList defaultLocales = in.readParcelable(null);
            final java.lang.String referenceTimeString = in.readString();
            final java.time.ZonedDateTime referenceTime = (referenceTimeString == null) ? null : java.time.ZonedDateTime.parse(referenceTimeString);
            final java.lang.String callingPackageName = in.readString();
            final android.os.Bundle extras = in.readBundle();
            final android.view.textclassifier.TextClassification.Request request = new android.view.textclassifier.TextClassification.Request(text, startIndex, endIndex, defaultLocales, referenceTime, extras);
            request.setCallingPackageName(callingPackageName);
            return request;
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.textclassifier.TextClassification.Request> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextClassification.Request>() {
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
        dest.writeString(mText);
        // NOTE: legacy fields are not parcelled.
        dest.writeTypedList(mActions);
        mEntityConfidence.writeToParcel(dest, flags);
        dest.writeString(mId);
        dest.writeBundle(mExtras);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textclassifier.TextClassification> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.TextClassification>() {
        @java.lang.Override
        public android.view.textclassifier.TextClassification createFromParcel(android.os.Parcel in) {
            return new android.view.textclassifier.TextClassification(in);
        }

        @java.lang.Override
        public android.view.textclassifier.TextClassification[] newArray(int size) {
            return new android.view.textclassifier.TextClassification[size];
        }
    };

    private TextClassification(android.os.Parcel in) {
        mText = in.readString();
        mActions = in.createTypedArrayList(RemoteAction.CREATOR);
        if (!mActions.isEmpty()) {
            final android.app.RemoteAction action = mActions.get(0);
            mLegacyIcon = android.view.textclassifier.TextClassification.maybeLoadDrawable(action.getIcon());
            mLegacyLabel = toString();
            mLegacyOnClickListener = android.view.textclassifier.TextClassification.createIntentOnClickListener(mActions.get(0).getActionIntent());
        } else {
            mLegacyIcon = null;
            mLegacyLabel = null;
            mLegacyOnClickListener = null;
        }
        mLegacyIntent = null;// mLegacyIntent is not parcelled.

        mEntityConfidence = this.CREATOR.createFromParcel(in);
        mId = in.readString();
        mExtras = in.readBundle();
    }

    // Best effort attempt to try to load a drawable from the provided icon.
    @android.annotation.Nullable
    private static android.graphics.drawable.Drawable maybeLoadDrawable(android.graphics.drawable.Icon icon) {
        if (icon == null) {
            return null;
        }
        switch (icon.getType()) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
                return new android.graphics.drawable.BitmapDrawable(android.content.res.Resources.getSystem(), icon.getBitmap());
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                return new android.graphics.drawable.AdaptiveIconDrawable(null, new android.graphics.drawable.BitmapDrawable(android.content.res.Resources.getSystem(), icon.getBitmap()));
            case android.graphics.drawable.Icon.TYPE_DATA :
                return new android.graphics.drawable.BitmapDrawable(android.content.res.Resources.getSystem(), android.graphics.BitmapFactory.decodeByteArray(icon.getDataBytes(), icon.getDataOffset(), icon.getDataLength()));
        }
        return null;
    }
}

