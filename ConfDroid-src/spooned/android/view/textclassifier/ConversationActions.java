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
 * Represents a list of actions suggested by a {@link TextClassifier} on a given conversation.
 *
 * @see TextClassifier#suggestConversationActions(Request)
 */
public final class ConversationActions implements android.os.Parcelable {
    @android.annotation.NonNull
    public static final android.view.textclassifier.Creator<android.view.textclassifier.ConversationActions> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.ConversationActions>() {
        @java.lang.Override
        public android.view.textclassifier.ConversationActions createFromParcel(android.os.Parcel in) {
            return new android.view.textclassifier.ConversationActions(in);
        }

        @java.lang.Override
        public android.view.textclassifier.ConversationActions[] newArray(int size) {
            return new android.view.textclassifier.ConversationActions[size];
        }
    };

    private final java.util.List<android.view.textclassifier.ConversationAction> mConversationActions;

    private final java.lang.String mId;

    /**
     * Constructs a {@link ConversationActions} object.
     */
    public ConversationActions(@android.annotation.NonNull
    java.util.List<android.view.textclassifier.ConversationAction> conversationActions, @android.annotation.Nullable
    java.lang.String id) {
        mConversationActions = java.util.Collections.unmodifiableList(com.android.internal.util.Preconditions.checkNotNull(conversationActions));
        mId = id;
    }

    private ConversationActions(android.os.Parcel in) {
        mConversationActions = java.util.Collections.unmodifiableList(in.createTypedArrayList(android.view.textclassifier.ConversationAction.CREATOR));
        mId = in.readString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeTypedList(mConversationActions);
        parcel.writeString(mId);
    }

    /**
     * Returns an immutable list of {@link ConversationAction} objects, which are ordered from high
     * confidence to low confidence.
     */
    @android.annotation.NonNull
    public java.util.List<android.view.textclassifier.ConversationAction> getConversationActions() {
        return mConversationActions;
    }

    /**
     * Returns the id, if one exists, for this object.
     */
    @android.annotation.Nullable
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Represents a message in the conversation.
     */
    public static final class Message implements android.os.Parcelable {
        /**
         * Represents the local user.
         *
         * @see Builder#Builder(Person)
         */
        @android.annotation.NonNull
        public static final android.app.Person PERSON_USER_SELF = new android.app.Person.Builder().setKey("text-classifier-conversation-actions-user-self").build();

        /**
         * Represents the remote user.
         * <p>
         * If possible, you are suggested to create a {@link Person} object that can identify
         * the remote user better, so that the underlying model could differentiate between
         * different remote users.
         *
         * @see Builder#Builder(Person)
         */
        @android.annotation.NonNull
        public static final android.app.Person PERSON_USER_OTHERS = new android.app.Person.Builder().setKey("text-classifier-conversation-actions-user-others").build();

        @android.annotation.Nullable
        private final android.app.Person mAuthor;

        @android.annotation.Nullable
        private final java.time.ZonedDateTime mReferenceTime;

        @android.annotation.Nullable
        private final java.lang.CharSequence mText;

        @android.annotation.NonNull
        private final android.os.Bundle mExtras;

        private Message(@android.annotation.Nullable
        android.app.Person author, @android.annotation.Nullable
        java.time.ZonedDateTime referenceTime, @android.annotation.Nullable
        java.lang.CharSequence text, @android.annotation.NonNull
        android.os.Bundle bundle) {
            mAuthor = author;
            mReferenceTime = referenceTime;
            mText = text;
            mExtras = com.android.internal.util.Preconditions.checkNotNull(bundle);
        }

        private Message(android.os.Parcel in) {
            mAuthor = in.readParcelable(null);
            mReferenceTime = (in.readInt() == 0) ? null : java.time.ZonedDateTime.parse(in.readString(), java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME);
            mText = in.readCharSequence();
            mExtras = in.readBundle();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel parcel, int flags) {
            parcel.writeParcelable(mAuthor, flags);
            parcel.writeInt(mReferenceTime != null ? 1 : 0);
            if (mReferenceTime != null) {
                parcel.writeString(mReferenceTime.format(java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME));
            }
            parcel.writeCharSequence(mText);
            parcel.writeBundle(mExtras);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.ConversationActions.Message> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.ConversationActions.Message>() {
            @java.lang.Override
            public android.view.textclassifier.ConversationActions.Message createFromParcel(android.os.Parcel in) {
                return new android.view.textclassifier.ConversationActions.Message(in);
            }

            @java.lang.Override
            public android.view.textclassifier.ConversationActions.Message[] newArray(int size) {
                return new android.view.textclassifier.ConversationActions.Message[size];
            }
        };

        /**
         * Returns the person that composed the message.
         */
        @android.annotation.NonNull
        public android.app.Person getAuthor() {
            return mAuthor;
        }

        /**
         * Returns the reference time of the message, for example it could be the compose or send
         * time of this message.
         */
        @android.annotation.Nullable
        public java.time.ZonedDateTime getReferenceTime() {
            return mReferenceTime;
        }

        /**
         * Returns the text of the message.
         */
        @android.annotation.Nullable
        public java.lang.CharSequence getText() {
            return mText;
        }

        /**
         * Returns the extended data related to this conversation action.
         *
         * <p><b>NOTE: </b>Do not modify this bundle.
         */
        @android.annotation.NonNull
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Builder class to construct a {@link Message}
         */
        public static final class Builder {
            @android.annotation.Nullable
            private android.app.Person mAuthor;

            @android.annotation.Nullable
            private java.time.ZonedDateTime mReferenceTime;

            @android.annotation.Nullable
            private java.lang.CharSequence mText;

            @android.annotation.Nullable
            private android.os.Bundle mExtras;

            /**
             * Constructs a builder.
             *
             * @param author
             * 		the person that composed the message, use {@link #PERSON_USER_SELF}
             * 		to represent the local user. If it is not possible to identify the
             * 		remote user that the local user is conversing with, use
             * 		{@link #PERSON_USER_OTHERS} to represent a remote user.
             */
            public Builder(@android.annotation.NonNull
            android.app.Person author) {
                mAuthor = com.android.internal.util.Preconditions.checkNotNull(author);
            }

            /**
             * Sets the text of this message.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Message.Builder setText(@android.annotation.Nullable
            java.lang.CharSequence text) {
                mText = text;
                return this;
            }

            /**
             * Sets the reference time of this message, for example it could be the compose or send
             * time of this message.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Message.Builder setReferenceTime(@android.annotation.Nullable
            java.time.ZonedDateTime referenceTime) {
                mReferenceTime = referenceTime;
                return this;
            }

            /**
             * Sets a set of extended data to the message.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Message.Builder setExtras(@android.annotation.Nullable
            android.os.Bundle bundle) {
                this.mExtras = bundle;
                return this;
            }

            /**
             * Builds the {@link Message} object.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Message build() {
                return new android.view.textclassifier.ConversationActions.Message(mAuthor, mReferenceTime, mText == null ? null : new android.text.SpannedString(mText), mExtras == null ? android.os.Bundle.EMPTY : mExtras);
            }
        }
    }

    /**
     * A request object for generating conversation action suggestions.
     *
     * @see TextClassifier#suggestConversationActions(Request)
     */
    public static final class Request implements android.os.Parcelable {
        /**
         *
         *
         * @unknown 
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.StringDef(value = { android.view.textclassifier.ConversationActions.Request.HINT_FOR_NOTIFICATION, android.view.textclassifier.ConversationActions.Request.HINT_FOR_IN_APP }, prefix = "HINT_")
        public @interface Hint {}

        /**
         * To indicate the generated actions will be used within the app.
         */
        public static final java.lang.String HINT_FOR_IN_APP = "in_app";

        /**
         * To indicate the generated actions will be used for notification.
         */
        public static final java.lang.String HINT_FOR_NOTIFICATION = "notification";

        @android.annotation.NonNull
        private final java.util.List<android.view.textclassifier.ConversationActions.Message> mConversation;

        @android.annotation.NonNull
        private final android.view.textclassifier.TextClassifier.EntityConfig mTypeConfig;

        private final int mMaxSuggestions;

        @android.annotation.NonNull
        @android.view.textclassifier.ConversationActions.Request.Hint
        private final java.util.List<java.lang.String> mHints;

        @android.annotation.Nullable
        private java.lang.String mCallingPackageName;

        @android.annotation.NonNull
        private android.os.Bundle mExtras;

        private Request(@android.annotation.NonNull
        java.util.List<android.view.textclassifier.ConversationActions.Message> conversation, @android.annotation.NonNull
        android.view.textclassifier.TextClassifier.EntityConfig typeConfig, int maxSuggestions, @android.annotation.Nullable
        @android.view.textclassifier.ConversationActions.Request.Hint
        java.util.List<java.lang.String> hints, @android.annotation.NonNull
        android.os.Bundle extras) {
            mConversation = com.android.internal.util.Preconditions.checkNotNull(conversation);
            mTypeConfig = com.android.internal.util.Preconditions.checkNotNull(typeConfig);
            mMaxSuggestions = maxSuggestions;
            mHints = hints;
            mExtras = extras;
        }

        private static android.view.textclassifier.ConversationActions.Request readFromParcel(android.os.Parcel in) {
            java.util.List<android.view.textclassifier.ConversationActions.Message> conversation = new java.util.ArrayList<>();
            in.readParcelableList(conversation, null);
            android.view.textclassifier.TextClassifier.EntityConfig typeConfig = in.readParcelable(null);
            int maxSuggestions = in.readInt();
            java.util.List<java.lang.String> hints = new java.util.ArrayList<>();
            in.readStringList(hints);
            java.lang.String callingPackageName = in.readString();
            android.os.Bundle extras = in.readBundle();
            android.view.textclassifier.ConversationActions.Request request = new android.view.textclassifier.ConversationActions.Request(conversation, typeConfig, maxSuggestions, hints, extras);
            request.setCallingPackageName(callingPackageName);
            return request;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel parcel, int flags) {
            parcel.writeParcelableList(mConversation, flags);
            parcel.writeParcelable(mTypeConfig, flags);
            parcel.writeInt(mMaxSuggestions);
            parcel.writeStringList(mHints);
            parcel.writeString(mCallingPackageName);
            parcel.writeBundle(mExtras);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.ConversationActions.Request> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.ConversationActions.Request>() {
            @java.lang.Override
            public android.view.textclassifier.ConversationActions.Request createFromParcel(android.os.Parcel in) {
                return android.view.textclassifier.ConversationActions.Request.readFromParcel(in);
            }

            @java.lang.Override
            public android.view.textclassifier.ConversationActions.Request[] newArray(int size) {
                return new android.view.textclassifier.ConversationActions.Request[size];
            }
        };

        /**
         * Returns the type config.
         */
        @android.annotation.NonNull
        public android.view.textclassifier.TextClassifier.EntityConfig getTypeConfig() {
            return mTypeConfig;
        }

        /**
         * Returns an immutable list of messages that make up the conversation.
         */
        @android.annotation.NonNull
        public java.util.List<android.view.textclassifier.ConversationActions.Message> getConversation() {
            return mConversation;
        }

        /**
         * Return the maximal number of suggestions the caller wants, value -1 means no restriction
         * and this is the default.
         */
        @android.annotation.IntRange(from = -1)
        public int getMaxSuggestions() {
            return mMaxSuggestions;
        }

        /**
         * Returns an immutable list of hints
         */
        @android.annotation.NonNull
        @android.view.textclassifier.ConversationActions.Request.Hint
        public java.util.List<java.lang.String> getHints() {
            return mHints;
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
         * Returns the extended data related to this request.
         *
         * <p><b>NOTE: </b>Do not modify this bundle.
         */
        @android.annotation.NonNull
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Builder object to construct the {@link Request} object.
         */
        public static final class Builder {
            @android.annotation.NonNull
            private java.util.List<android.view.textclassifier.ConversationActions.Message> mConversation;

            @android.annotation.Nullable
            private android.view.textclassifier.TextClassifier.EntityConfig mTypeConfig;

            private int mMaxSuggestions = -1;

            @android.annotation.Nullable
            @android.view.textclassifier.ConversationActions.Request.Hint
            private java.util.List<java.lang.String> mHints;

            @android.annotation.Nullable
            private android.os.Bundle mExtras;

            /**
             * Constructs a builder.
             *
             * @param conversation
             * 		the conversation that the text classifier is going to generate
             * 		actions for.
             */
            public Builder(@android.annotation.NonNull
            java.util.List<android.view.textclassifier.ConversationActions.Message> conversation) {
                mConversation = com.android.internal.util.Preconditions.checkNotNull(conversation);
            }

            /**
             * Sets the hints to help text classifier to generate actions. It could be used to help
             * text classifier to infer what types of actions the caller may be interested in.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Request.Builder setHints(@android.annotation.Nullable
            @android.view.textclassifier.ConversationActions.Request.Hint
            java.util.List<java.lang.String> hints) {
                mHints = hints;
                return this;
            }

            /**
             * Sets the type config.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Request.Builder setTypeConfig(@android.annotation.Nullable
            android.view.textclassifier.TextClassifier.EntityConfig typeConfig) {
                mTypeConfig = typeConfig;
                return this;
            }

            /**
             * Sets the maximum number of suggestions you want. Value -1 means no restriction and
             * this is the default.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Request.Builder setMaxSuggestions(@android.annotation.IntRange(from = -1)
            int maxSuggestions) {
                mMaxSuggestions = com.android.internal.util.Preconditions.checkArgumentNonnegative(maxSuggestions);
                return this;
            }

            /**
             * Sets a set of extended data to the request.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Request.Builder setExtras(@android.annotation.Nullable
            android.os.Bundle bundle) {
                mExtras = bundle;
                return this;
            }

            /**
             * Builds the {@link Request} object.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.ConversationActions.Request build() {
                return new android.view.textclassifier.ConversationActions.Request(java.util.Collections.unmodifiableList(mConversation), mTypeConfig == null ? new android.view.textclassifier.TextClassifier.EntityConfig.Builder().build() : mTypeConfig, mMaxSuggestions, mHints == null ? java.util.Collections.emptyList() : java.util.Collections.unmodifiableList(mHints), mExtras == null ? android.os.Bundle.EMPTY : mExtras);
            }
        }
    }
}

