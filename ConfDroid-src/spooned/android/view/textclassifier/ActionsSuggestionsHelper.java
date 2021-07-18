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
 * Helper class for action suggestions.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class ActionsSuggestionsHelper {
    private static final java.lang.String TAG = "ActionsSuggestions";

    private static final int USER_LOCAL = 0;

    private static final int FIRST_NON_LOCAL_USER = 1;

    private ActionsSuggestionsHelper() {
    }

    /**
     * Converts the messages to a list of native messages object that the model can understand.
     * <p>
     * User id encoding - local user is represented as 0, Other users are numbered according to
     * how far before they spoke last time in the conversation. For example, considering this
     * conversation:
     * <ul>
     * <li> User A: xxx
     * <li> Local user: yyy
     * <li> User B: zzz
     * </ul>
     * User A will be encoded as 2, user B will be encoded as 1 and local user will be encoded as 0.
     */
    public static ActionsSuggestionsModel.ConversationMessage[] toNativeMessages(java.util.List<android.view.textclassifier.ConversationActions.Message> messages, java.util.function.Function<java.lang.CharSequence, java.lang.String> languageDetector) {
        java.util.List<android.view.textclassifier.ConversationActions.Message> messagesWithText = messages.stream().filter(( message) -> !android.text.TextUtils.isEmpty(message.getText())).collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
        if (messagesWithText.isEmpty()) {
            return new com.google.android.textclassifier.ActionsSuggestionsModel.ConversationMessage[0];
        }
        java.util.Deque<com.google.android.textclassifier.ActionsSuggestionsModel.ConversationMessage> nativeMessages = new java.util.ArrayDeque<>();
        android.view.textclassifier.ActionsSuggestionsHelper.PersonEncoder personEncoder = new android.view.textclassifier.ActionsSuggestionsHelper.PersonEncoder();
        int size = messagesWithText.size();
        for (int i = size - 1; i >= 0; i--) {
            android.view.textclassifier.ConversationActions.Message message = messagesWithText.get(i);
            long referenceTime = (message.getReferenceTime() == null) ? 0 : message.getReferenceTime().toInstant().toEpochMilli();
            java.lang.String timeZone = (message.getReferenceTime() == null) ? null : message.getReferenceTime().getZone().getId();
            nativeMessages.push(new com.google.android.textclassifier.ActionsSuggestionsModel.ConversationMessage(personEncoder.encode(message.getAuthor()), message.getText().toString(), referenceTime, timeZone, languageDetector.apply(message.getText())));
        }
        return nativeMessages.toArray(new com.google.android.textclassifier.ActionsSuggestionsModel.ConversationMessage[nativeMessages.size()]);
    }

    /**
     * Returns the result id for logging.
     */
    public static java.lang.String createResultId(android.content.Context context, java.util.List<android.view.textclassifier.ConversationActions.Message> messages, int modelVersion, java.util.List<java.util.Locale> modelLocales) {
        final java.util.StringJoiner localesJoiner = new java.util.StringJoiner(",");
        for (java.util.Locale locale : modelLocales) {
            localesJoiner.add(locale.toLanguageTag());
        }
        final java.lang.String modelName = java.lang.String.format(java.util.Locale.US, "%s_v%d", localesJoiner.toString(), modelVersion);
        final int hash = java.util.Objects.hash(messages.stream().mapToInt(android.view.textclassifier.ActionsSuggestionsHelper::hashMessage), context.getPackageName(), java.lang.System.currentTimeMillis());
        return android.view.textclassifier.SelectionSessionLogger.SignatureParser.createSignature(android.view.textclassifier.SelectionSessionLogger.CLASSIFIER_ID, modelName, hash);
    }

    /**
     * Generated labeled intent from an action suggestion and return the resolved result.
     */
    @android.annotation.Nullable
    public static android.view.textclassifier.intent.LabeledIntent.Result createLabeledIntentResult(android.content.Context context, android.view.textclassifier.intent.TemplateIntentFactory templateIntentFactory, com.google.android.textclassifier.ActionsSuggestionsModel.ActionSuggestion nativeSuggestion) {
        com.google.android.textclassifier.RemoteActionTemplate[] remoteActionTemplates = nativeSuggestion.getRemoteActionTemplates();
        if (remoteActionTemplates == null) {
            android.view.textclassifier.Log.w(android.view.textclassifier.ActionsSuggestionsHelper.TAG, "createRemoteAction: Missing template for type " + nativeSuggestion.getActionType());
            return null;
        }
        java.util.List<android.view.textclassifier.intent.LabeledIntent> labeledIntents = templateIntentFactory.create(remoteActionTemplates);
        if (labeledIntents.isEmpty()) {
            return null;
        }
        // Given that we only support implicit intent here, we should expect there is just one
        // intent for each action type.
        android.view.textclassifier.intent.LabeledIntent.TitleChooser titleChooser = android.view.textclassifier.ActionsSuggestionsHelper.createTitleChooser(nativeSuggestion.getActionType());
        return labeledIntents.get(0).resolve(context, titleChooser, null);
    }

    /**
     * Returns a {@link LabeledIntent.TitleChooser} for conversation actions use case.
     */
    @android.annotation.Nullable
    public static android.view.textclassifier.intent.LabeledIntent.TitleChooser createTitleChooser(java.lang.String actionType) {
        if (android.view.textclassifier.ConversationAction.TYPE_OPEN_URL.equals(actionType)) {
            return ( labeledIntent, resolveInfo) -> {
                if (resolveInfo.handleAllWebDataURI) {
                    return labeledIntent.titleWithEntity;
                }
                if ("android".equals(resolveInfo.activityInfo.packageName)) {
                    return labeledIntent.titleWithEntity;
                }
                return labeledIntent.titleWithoutEntity;
            };
        }
        return null;
    }

    /**
     * Returns a list of {@link ConversationAction}s that have 0 duplicates. Two actions are
     * duplicates if they may look the same to users. This function assumes every
     * ConversationActions with a non-null RemoteAction also have a non-null intent in the extras.
     */
    public static java.util.List<android.view.textclassifier.ConversationAction> removeActionsWithDuplicates(java.util.List<android.view.textclassifier.ConversationAction> conversationActions) {
        // Ideally, we should compare title and icon here, but comparing icon is expensive and thus
        // we use the component name of the target handler as the heuristic.
        java.util.Map<android.util.Pair<java.lang.String, java.lang.String>, java.lang.Integer> counter = new android.util.ArrayMap();
        for (android.view.textclassifier.ConversationAction conversationAction : conversationActions) {
            android.util.Pair<java.lang.String, java.lang.String> representation = android.view.textclassifier.ActionsSuggestionsHelper.getRepresentation(conversationAction);
            if (representation == null) {
                continue;
            }
            java.lang.Integer existingCount = counter.getOrDefault(representation, 0);
            counter.put(representation, existingCount + 1);
        }
        java.util.List<android.view.textclassifier.ConversationAction> result = new java.util.ArrayList<>();
        for (android.view.textclassifier.ConversationAction conversationAction : conversationActions) {
            android.util.Pair<java.lang.String, java.lang.String> representation = android.view.textclassifier.ActionsSuggestionsHelper.getRepresentation(conversationAction);
            if ((representation == null) || (counter.getOrDefault(representation, 0) == 1)) {
                result.add(conversationAction);
            }
        }
        return result;
    }

    @android.annotation.Nullable
    private static android.util.Pair<java.lang.String, java.lang.String> getRepresentation(android.view.textclassifier.ConversationAction conversationAction) {
        android.app.RemoteAction remoteAction = conversationAction.getAction();
        if (remoteAction == null) {
            return null;
        }
        android.content.Intent actionIntent = android.view.textclassifier.ExtrasUtils.getActionIntent(conversationAction.getExtras());
        android.content.ComponentName componentName = actionIntent.getComponent();
        // Action without a component name will be considered as from the same app.
        java.lang.String packageName = (componentName == null) ? null : componentName.getPackageName();
        return new android.util.Pair(conversationAction.getAction().getTitle().toString(), packageName);
    }

    private static final class PersonEncoder {
        private final java.util.Map<android.app.Person, java.lang.Integer> mMapping = new android.util.ArrayMap();

        private int mNextUserId = android.view.textclassifier.ActionsSuggestionsHelper.FIRST_NON_LOCAL_USER;

        private int encode(android.app.Person person) {
            if (android.view.textclassifier.ConversationActions.Message.PERSON_USER_SELF.equals(person)) {
                return android.view.textclassifier.ActionsSuggestionsHelper.USER_LOCAL;
            }
            java.lang.Integer result = mMapping.get(person);
            if (result == null) {
                mMapping.put(person, mNextUserId);
                result = mNextUserId;
                mNextUserId++;
            }
            return result;
        }
    }

    private static int hashMessage(android.view.textclassifier.ConversationActions.Message message) {
        return java.util.Objects.hash(message.getAuthor(), message.getText(), message.getReferenceTime());
    }
}

