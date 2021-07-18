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
 * Default implementation of the {@link TextClassifier} interface.
 *
 * <p>This class uses machine learning to recognize entities in text.
 * Unless otherwise stated, methods of this class are blocking operations and should most
 * likely not be called on the UI thread.
 *
 * @unknown 
 */
public final class TextClassifierImpl implements android.view.textclassifier.TextClassifier {
    private static final java.lang.String LOG_TAG = android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG;

    private static final boolean DEBUG = false;

    private static final java.io.File FACTORY_MODEL_DIR = new java.io.File("/etc/textclassifier/");

    // Annotator
    private static final java.lang.String ANNOTATOR_FACTORY_MODEL_FILENAME_REGEX = "textclassifier\\.(.*)\\.model";

    private static final java.io.File ANNOTATOR_UPDATED_MODEL_FILE = new java.io.File("/data/misc/textclassifier/textclassifier.model");

    // LangID
    private static final java.lang.String LANG_ID_FACTORY_MODEL_FILENAME_REGEX = "lang_id.model";

    private static final java.io.File UPDATED_LANG_ID_MODEL_FILE = new java.io.File("/data/misc/textclassifier/lang_id.model");

    // Actions
    private static final java.lang.String ACTIONS_FACTORY_MODEL_FILENAME_REGEX = "actions_suggestions\\.(.*)\\.model";

    private static final java.io.File UPDATED_ACTIONS_MODEL = new java.io.File("/data/misc/textclassifier/actions_suggestions.model");

    private final android.content.Context mContext;

    private final android.view.textclassifier.TextClassifier mFallback;

    private final android.view.textclassifier.GenerateLinksLogger mGenerateLinksLogger;

    private final java.lang.Object mLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.view.textclassifier.ModelFileManager.ModelFile mAnnotatorModelInUse;

    @com.android.internal.annotations.GuardedBy("mLock")
    private com.google.android.textclassifier.AnnotatorModel mAnnotatorImpl;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.view.textclassifier.ModelFileManager.ModelFile mLangIdModelInUse;

    @com.android.internal.annotations.GuardedBy("mLock")
    private com.google.android.textclassifier.LangIdModel mLangIdImpl;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.view.textclassifier.ModelFileManager.ModelFile mActionModelInUse;

    @com.android.internal.annotations.GuardedBy("mLock")
    private com.google.android.textclassifier.ActionsSuggestionsModel mActionsImpl;

    private final android.view.textclassifier.SelectionSessionLogger mSessionLogger = new android.view.textclassifier.SelectionSessionLogger();

    private final android.view.textclassifier.TextClassifierEventTronLogger mTextClassifierEventTronLogger = new android.view.textclassifier.TextClassifierEventTronLogger();

    private final android.view.textclassifier.TextClassificationConstants mSettings;

    private final android.view.textclassifier.ModelFileManager mAnnotatorModelFileManager;

    private final android.view.textclassifier.ModelFileManager mLangIdModelFileManager;

    private final android.view.textclassifier.ModelFileManager mActionsModelFileManager;

    private final android.view.textclassifier.intent.ClassificationIntentFactory mClassificationIntentFactory;

    private final android.view.textclassifier.intent.TemplateIntentFactory mTemplateIntentFactory;

    private final java.util.function.Supplier<android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams> mActionsModelParamsSupplier;

    public TextClassifierImpl(android.content.Context context, android.view.textclassifier.TextClassificationConstants settings, android.view.textclassifier.TextClassifier fallback) {
        mContext = com.android.internal.util.Preconditions.checkNotNull(context);
        mFallback = com.android.internal.util.Preconditions.checkNotNull(fallback);
        mSettings = com.android.internal.util.Preconditions.checkNotNull(settings);
        mGenerateLinksLogger = new android.view.textclassifier.GenerateLinksLogger(mSettings.getGenerateLinksLogSampleRate());
        mAnnotatorModelFileManager = new android.view.textclassifier.ModelFileManager(new android.view.textclassifier.ModelFileManager.ModelFileSupplierImpl(android.view.textclassifier.TextClassifierImpl.FACTORY_MODEL_DIR, android.view.textclassifier.TextClassifierImpl.ANNOTATOR_FACTORY_MODEL_FILENAME_REGEX, android.view.textclassifier.TextClassifierImpl.ANNOTATOR_UPDATED_MODEL_FILE, AnnotatorModel::getVersion, AnnotatorModel::getLocales));
        mLangIdModelFileManager = new android.view.textclassifier.ModelFileManager(new android.view.textclassifier.ModelFileManager.ModelFileSupplierImpl(android.view.textclassifier.TextClassifierImpl.FACTORY_MODEL_DIR, android.view.textclassifier.TextClassifierImpl.LANG_ID_FACTORY_MODEL_FILENAME_REGEX, android.view.textclassifier.TextClassifierImpl.UPDATED_LANG_ID_MODEL_FILE, LangIdModel::getVersion, ( fd) -> ModelFileManager.ModelFile.LANGUAGE_INDEPENDENT));
        mActionsModelFileManager = new android.view.textclassifier.ModelFileManager(new android.view.textclassifier.ModelFileManager.ModelFileSupplierImpl(android.view.textclassifier.TextClassifierImpl.FACTORY_MODEL_DIR, android.view.textclassifier.TextClassifierImpl.ACTIONS_FACTORY_MODEL_FILENAME_REGEX, android.view.textclassifier.TextClassifierImpl.UPDATED_ACTIONS_MODEL, ActionsSuggestionsModel::getVersion, ActionsSuggestionsModel::getLocales));
        mTemplateIntentFactory = new android.view.textclassifier.intent.TemplateIntentFactory();
        mClassificationIntentFactory = (mSettings.isTemplateIntentFactoryEnabled()) ? new android.view.textclassifier.intent.TemplateClassificationIntentFactory(mTemplateIntentFactory, new android.view.textclassifier.intent.LegacyClassificationIntentFactory()) : new android.view.textclassifier.intent.LegacyClassificationIntentFactory();
        mActionsModelParamsSupplier = new android.view.textclassifier.ActionsModelParamsSupplier(mContext, () -> {
            synchronized(mLock) {
                // Clear mActionsImpl here, so that we will create a new
                // ActionsSuggestionsModel object with the new flag in the next request.
                mActionsImpl = null;
                mActionModelInUse = null;
            }
        });
    }

    public TextClassifierImpl(android.content.Context context, android.view.textclassifier.TextClassificationConstants settings) {
        this(context, settings, android.view.textclassifier.TextClassifier.NO_OP);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.WorkerThread
    public android.view.textclassifier.TextSelection suggestSelection(android.view.textclassifier.TextSelection.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            final int rangeLength = request.getEndIndex() - request.getStartIndex();
            final java.lang.String string = request.getText().toString();
            if ((string.length() > 0) && (rangeLength <= mSettings.getSuggestSelectionMaxRangeLength())) {
                final java.lang.String localesString = android.view.textclassifier.TextClassifierImpl.concatenateLocales(request.getDefaultLocales());
                final java.lang.String detectLanguageTags = detectLanguageTagsFromText(request.getText());
                final java.time.ZonedDateTime refTime = java.time.ZonedDateTime.now();
                final com.google.android.textclassifier.AnnotatorModel annotatorImpl = getAnnotatorImpl(request.getDefaultLocales());
                final int start;
                final int end;
                if (mSettings.isModelDarkLaunchEnabled() && (!request.isDarkLaunchAllowed())) {
                    start = request.getStartIndex();
                    end = request.getEndIndex();
                } else {
                    final int[] startEnd = annotatorImpl.suggestSelection(string, request.getStartIndex(), request.getEndIndex(), new com.google.android.textclassifier.AnnotatorModel.SelectionOptions(localesString, detectLanguageTags));
                    start = startEnd[0];
                    end = startEnd[1];
                }
                if (((((start < end) && (start >= 0)) && (end <= string.length())) && (start <= request.getStartIndex())) && (end >= request.getEndIndex())) {
                    final android.view.textclassifier.TextSelection.Builder tsBuilder = new android.view.textclassifier.TextSelection.Builder(start, end);
                    final com.google.android.textclassifier.AnnotatorModel[] results = // Passing null here to suppress intent generation
                    // TODO: Use an explicit flag to suppress it.
                    /* appContext */
                    /* deviceLocales */
                    annotatorImpl.classifyText(string, start, end, new com.google.android.textclassifier.AnnotatorModel.ClassificationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString, detectLanguageTags), null, null);
                    final int size = results.length;
                    for (int i = 0; i < size; i++) {
                        tsBuilder.setEntityType(results[i].getCollection(), results[i].getScore());
                    }
                    return tsBuilder.setId(createId(string, request.getStartIndex(), request.getEndIndex())).build();
                } else {
                    // We can not trust the result. Log the issue and ignore the result.
                    android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Got bad indices for input text. Ignoring result.");
                }
            }
        } catch (java.lang.Throwable t) {
            // Avoid throwing from this method. Log the error.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error suggesting selection for text. No changes to selection suggested.", t);
        }
        // Getting here means something went wrong, return a NO_OP result.
        return mFallback.suggestSelection(request);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.WorkerThread
    public android.view.textclassifier.TextClassification classifyText(android.view.textclassifier.TextClassification.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            final int rangeLength = request.getEndIndex() - request.getStartIndex();
            final java.lang.String string = request.getText().toString();
            if ((string.length() > 0) && (rangeLength <= mSettings.getClassifyTextMaxRangeLength())) {
                final java.lang.String localesString = android.view.textclassifier.TextClassifierImpl.concatenateLocales(request.getDefaultLocales());
                final java.lang.String detectLanguageTags = detectLanguageTagsFromText(request.getText());
                final java.time.ZonedDateTime refTime = (request.getReferenceTime() != null) ? request.getReferenceTime() : java.time.ZonedDateTime.now();
                final com.google.android.textclassifier.AnnotatorModel[] results = classifyText(string, request.getStartIndex(), request.getEndIndex(), new com.google.android.textclassifier.AnnotatorModel.ClassificationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString, detectLanguageTags), mContext, getResourceLocalesString());
                if (results.length > 0) {
                    return createClassificationResult(results, string, request.getStartIndex(), request.getEndIndex(), refTime.toInstant());
                }
            }
        } catch (java.lang.Throwable t) {
            // Avoid throwing from this method. Log the error.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error getting text classification info.", t);
        }
        // Getting here means something went wrong, return a NO_OP result.
        return mFallback.classifyText(request);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.WorkerThread
    public android.view.textclassifier.TextLinks generateLinks(@android.annotation.NonNull
    android.view.textclassifier.TextLinks.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkTextLength(request.getText(), getMaxGenerateLinksTextLength());
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        if ((!mSettings.isSmartLinkifyEnabled()) && request.isLegacyFallback()) {
            return android.view.textclassifier.TextClassifier.Utils.generateLegacyLinks(request);
        }
        final java.lang.String textString = request.getText().toString();
        final android.view.textclassifier.TextLinks.Builder builder = new android.view.textclassifier.TextLinks.Builder(textString);
        try {
            final long startTimeMs = java.lang.System.currentTimeMillis();
            final java.time.ZonedDateTime refTime = java.time.ZonedDateTime.now();
            final java.util.Collection<java.lang.String> entitiesToIdentify = (request.getEntityConfig() != null) ? request.getEntityConfig().resolveEntityListModifications(getEntitiesForHints(request.getEntityConfig().getHints())) : mSettings.getEntityListDefault();
            final java.lang.String localesString = android.view.textclassifier.TextClassifierImpl.concatenateLocales(request.getDefaultLocales());
            final java.lang.String detectLanguageTags = detectLanguageTagsFromText(request.getText());
            final com.google.android.textclassifier.AnnotatorModel annotatorImpl = getAnnotatorImpl(request.getDefaultLocales());
            final boolean isSerializedEntityDataEnabled = android.view.textclassifier.ExtrasUtils.isSerializedEntityDataEnabled(request);
            final com.google.android.textclassifier.AnnotatorModel[] annotations = annotatorImpl.annotate(textString, new com.google.android.textclassifier.AnnotatorModel.AnnotationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString, detectLanguageTags, entitiesToIdentify, AnnotatorModel.AnnotationUsecase.SMART.getValue(), isSerializedEntityDataEnabled));
            for (com.google.android.textclassifier.AnnotatorModel.AnnotatedSpan span : annotations) {
                final com.google.android.textclassifier.AnnotatorModel[] results = span.getClassification();
                if ((results.length == 0) || (!entitiesToIdentify.contains(results[0].getCollection()))) {
                    continue;
                }
                final java.util.Map<java.lang.String, java.lang.Float> entityScores = new android.util.ArrayMap();
                for (int i = 0; i < results.length; i++) {
                    entityScores.put(results[i].getCollection(), results[i].getScore());
                }
                android.os.Bundle extras = new android.os.Bundle();
                if (isSerializedEntityDataEnabled) {
                    android.view.textclassifier.ExtrasUtils.putEntities(extras, results);
                }
                builder.addLink(span.getStartIndex(), span.getEndIndex(), entityScores, extras);
            }
            final android.view.textclassifier.TextLinks links = builder.build();
            final long endTimeMs = java.lang.System.currentTimeMillis();
            final java.lang.String callingPackageName = (request.getCallingPackageName() == null) ? mContext.getPackageName()// local (in process) TC.
             : request.getCallingPackageName();
            mGenerateLinksLogger.logGenerateLinks(request.getText(), links, callingPackageName, endTimeMs - startTimeMs);
            return links;
        } catch (java.lang.Throwable t) {
            // Avoid throwing from this method. Log the error.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error getting links info.", t);
        }
        return mFallback.generateLinks(request);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getMaxGenerateLinksTextLength() {
        return mSettings.getGenerateLinksMaxTextLength();
    }

    private java.util.Collection<java.lang.String> getEntitiesForHints(java.util.Collection<java.lang.String> hints) {
        final boolean editable = hints.contains(android.view.textclassifier.TextClassifier.HINT_TEXT_IS_EDITABLE);
        final boolean notEditable = hints.contains(android.view.textclassifier.TextClassifier.HINT_TEXT_IS_NOT_EDITABLE);
        // Use the default if there is no hint, or conflicting ones.
        final boolean useDefault = editable == notEditable;
        if (useDefault) {
            return mSettings.getEntityListDefault();
        } else
            if (editable) {
                return mSettings.getEntityListEditable();
            } else {
                // notEditable
                return mSettings.getEntityListNotEditable();
            }

    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onSelectionEvent(android.view.textclassifier.SelectionEvent event) {
        mSessionLogger.writeEvent(event);
    }

    @java.lang.Override
    public void onTextClassifierEvent(android.view.textclassifier.TextClassifierEvent event) {
        if (android.view.textclassifier.TextClassifierImpl.DEBUG) {
            android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, ("onTextClassifierEvent() called with: event = [" + event) + "]");
        }
        try {
            final android.view.textclassifier.SelectionEvent selEvent = event.toSelectionEvent();
            if (selEvent != null) {
                mSessionLogger.writeEvent(selEvent);
            } else {
                mTextClassifierEventTronLogger.writeEvent(event);
            }
        } catch (java.lang.Exception e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error writing event", e);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.textclassifier.TextLanguage detectLanguage(@android.annotation.NonNull
    android.view.textclassifier.TextLanguage.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            final android.view.textclassifier.TextLanguage.Builder builder = new android.view.textclassifier.TextLanguage.Builder();
            final com.google.android.textclassifier.LangIdModel[] langResults = getLangIdImpl().detectLanguages(request.getText().toString());
            for (int i = 0; i < langResults.length; i++) {
                builder.putLocale(android.icu.util.ULocale.forLanguageTag(langResults[i].getLanguage()), langResults[i].getScore());
            }
            return builder.build();
        } catch (java.lang.Throwable t) {
            // Avoid throwing from this method. Log the error.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error detecting text language.", t);
        }
        return mFallback.detectLanguage(request);
    }

    @java.lang.Override
    public android.view.textclassifier.ConversationActions suggestConversationActions(android.view.textclassifier.ConversationActions.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            com.google.android.textclassifier.ActionsSuggestionsModel actionsImpl = getActionsImpl();
            if (actionsImpl == null) {
                // Actions model is optional, fallback if it is not available.
                return mFallback.suggestConversationActions(request);
            }
            com.google.android.textclassifier.ActionsSuggestionsModel[] nativeMessages = android.view.textclassifier.ActionsSuggestionsHelper.toNativeMessages(request.getConversation(), this::detectLanguageTagsFromText);
            if (nativeMessages.length == 0) {
                return mFallback.suggestConversationActions(request);
            }
            com.google.android.textclassifier.ActionsSuggestionsModel.Conversation nativeConversation = new com.google.android.textclassifier.ActionsSuggestionsModel.Conversation(nativeMessages);
            com.google.android.textclassifier.ActionsSuggestionsModel[] nativeSuggestions = actionsImpl.suggestActionsWithIntents(nativeConversation, null, mContext, getResourceLocalesString(), getAnnotatorImpl(android.os.LocaleList.getDefault()));
            return createConversationActionResult(request, nativeSuggestions);
        } catch (java.lang.Throwable t) {
            // Avoid throwing from this method. Log the error.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error suggesting conversation actions.", t);
        }
        return mFallback.suggestConversationActions(request);
    }

    /**
     * Returns the {@link ConversationAction} result, with a non-null extras.
     * <p>
     * Whenever the RemoteAction is non-null, you can expect its corresponding intent
     * with a non-null component name is in the extras.
     */
    private android.view.textclassifier.ConversationActions createConversationActionResult(android.view.textclassifier.ConversationActions.Request request, com.google.android.textclassifier.ActionsSuggestionsModel[] nativeSuggestions) {
        java.util.Collection<java.lang.String> expectedTypes = resolveActionTypesFromRequest(request);
        java.util.List<android.view.textclassifier.ConversationAction> conversationActions = new java.util.ArrayList<>();
        for (com.google.android.textclassifier.ActionsSuggestionsModel.ActionSuggestion nativeSuggestion : nativeSuggestions) {
            java.lang.String actionType = nativeSuggestion.getActionType();
            if (!expectedTypes.contains(actionType)) {
                continue;
            }
            android.view.textclassifier.intent.LabeledIntent.Result labeledIntentResult = android.view.textclassifier.ActionsSuggestionsHelper.createLabeledIntentResult(mContext, mTemplateIntentFactory, nativeSuggestion);
            android.app.RemoteAction remoteAction = null;
            android.os.Bundle extras = new android.os.Bundle();
            if (labeledIntentResult != null) {
                remoteAction = labeledIntentResult.remoteAction;
                android.view.textclassifier.ExtrasUtils.putActionIntent(extras, labeledIntentResult.resolvedIntent);
            }
            android.view.textclassifier.ExtrasUtils.putSerializedEntityData(extras, nativeSuggestion.getSerializedEntityData());
            android.view.textclassifier.ExtrasUtils.putEntitiesExtras(extras, android.view.textclassifier.intent.TemplateIntentFactory.nameVariantsToBundle(nativeSuggestion.getEntityData()));
            conversationActions.add(new android.view.textclassifier.ConversationAction.Builder(actionType).setConfidenceScore(nativeSuggestion.getScore()).setTextReply(nativeSuggestion.getResponseText()).setAction(remoteAction).setExtras(extras).build());
        }
        conversationActions = android.view.textclassifier.ActionsSuggestionsHelper.removeActionsWithDuplicates(conversationActions);
        if ((request.getMaxSuggestions() >= 0) && (conversationActions.size() > request.getMaxSuggestions())) {
            conversationActions = conversationActions.subList(0, request.getMaxSuggestions());
        }
        java.lang.String resultId = android.view.textclassifier.ActionsSuggestionsHelper.createResultId(mContext, request.getConversation(), mActionModelInUse.getVersion(), mActionModelInUse.getSupportedLocales());
        return new android.view.textclassifier.ConversationActions(conversationActions, resultId);
    }

    @android.annotation.Nullable
    private java.lang.String detectLanguageTagsFromText(java.lang.CharSequence text) {
        if (!mSettings.isDetectLanguagesFromTextEnabled()) {
            return null;
        }
        final float threshold = getLangIdThreshold();
        if ((threshold < 0) || (threshold > 1)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "[detectLanguageTagsFromText] unexpected threshold is found: " + threshold);
            return null;
        }
        android.view.textclassifier.TextLanguage.Request request = new android.view.textclassifier.TextLanguage.Request.Builder(text).build();
        android.view.textclassifier.TextLanguage textLanguage = detectLanguage(request);
        int localeHypothesisCount = textLanguage.getLocaleHypothesisCount();
        java.util.List<java.lang.String> languageTags = new java.util.ArrayList<>();
        for (int i = 0; i < localeHypothesisCount; i++) {
            android.icu.util.ULocale locale = textLanguage.getLocale(i);
            if (textLanguage.getConfidenceScore(locale) < threshold) {
                break;
            }
            languageTags.add(locale.toLanguageTag());
        }
        if (languageTags.isEmpty()) {
            return null;
        }
        return java.lang.String.join(",", languageTags);
    }

    private java.util.Collection<java.lang.String> resolveActionTypesFromRequest(android.view.textclassifier.ConversationActions.Request request) {
        java.util.List<java.lang.String> defaultActionTypes = (request.getHints().contains(android.view.textclassifier.ConversationActions.Request.HINT_FOR_NOTIFICATION)) ? mSettings.getNotificationConversationActionTypes() : mSettings.getInAppConversationActionTypes();
        return request.getTypeConfig().resolveEntityListModifications(defaultActionTypes);
    }

    private com.google.android.textclassifier.AnnotatorModel getAnnotatorImpl(android.os.LocaleList localeList) throws java.io.FileNotFoundException {
        synchronized(mLock) {
            localeList = (localeList == null) ? android.os.LocaleList.getDefault() : localeList;
            final android.view.textclassifier.ModelFileManager.ModelFile bestModel = mAnnotatorModelFileManager.findBestModelFile(localeList);
            if (bestModel == null) {
                throw new java.io.FileNotFoundException("No annotator model for " + localeList.toLanguageTags());
            }
            if ((mAnnotatorImpl == null) || (!java.util.Objects.equals(mAnnotatorModelInUse, bestModel))) {
                android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "Loading " + bestModel);
                final android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(bestModel.getPath()), ParcelFileDescriptor.MODE_READ_ONLY);
                try {
                    if (pfd != null) {
                        // The current annotator model may be still used by another thread / model.
                        // Do not call close() here, and let the GC to clean it up when no one else
                        // is using it.
                        mAnnotatorImpl = new com.google.android.textclassifier.AnnotatorModel(pfd.getFd());
                        mAnnotatorModelInUse = bestModel;
                    }
                } finally {
                    android.view.textclassifier.TextClassifierImpl.maybeCloseAndLogError(pfd);
                }
            }
            return mAnnotatorImpl;
        }
    }

    private com.google.android.textclassifier.LangIdModel getLangIdImpl() throws java.io.FileNotFoundException {
        synchronized(mLock) {
            final android.view.textclassifier.ModelFileManager.ModelFile bestModel = mLangIdModelFileManager.findBestModelFile(null);
            if (bestModel == null) {
                throw new java.io.FileNotFoundException("No LangID model is found");
            }
            if ((mLangIdImpl == null) || (!java.util.Objects.equals(mLangIdModelInUse, bestModel))) {
                android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "Loading " + bestModel);
                final android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(bestModel.getPath()), ParcelFileDescriptor.MODE_READ_ONLY);
                try {
                    if (pfd != null) {
                        mLangIdImpl = new com.google.android.textclassifier.LangIdModel(pfd.getFd());
                        mLangIdModelInUse = bestModel;
                    }
                } finally {
                    android.view.textclassifier.TextClassifierImpl.maybeCloseAndLogError(pfd);
                }
            }
            return mLangIdImpl;
        }
    }

    @android.annotation.Nullable
    private com.google.android.textclassifier.ActionsSuggestionsModel getActionsImpl() throws java.io.FileNotFoundException {
        synchronized(mLock) {
            // TODO: Use LangID to determine the locale we should use here?
            final android.view.textclassifier.ModelFileManager.ModelFile bestModel = mActionsModelFileManager.findBestModelFile(android.os.LocaleList.getDefault());
            if (bestModel == null) {
                return null;
            }
            if ((mActionsImpl == null) || (!java.util.Objects.equals(mActionModelInUse, bestModel))) {
                android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG, "Loading " + bestModel);
                final android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(bestModel.getPath()), ParcelFileDescriptor.MODE_READ_ONLY);
                try {
                    if (pfd == null) {
                        android.view.textclassifier.Log.d(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Failed to read the model file: " + bestModel.getPath());
                        return null;
                    }
                    android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams params = mActionsModelParamsSupplier.get();
                    mActionsImpl = new com.google.android.textclassifier.ActionsSuggestionsModel(pfd.getFd(), params.getSerializedPreconditions(bestModel));
                    mActionModelInUse = bestModel;
                } finally {
                    android.view.textclassifier.TextClassifierImpl.maybeCloseAndLogError(pfd);
                }
            }
            return mActionsImpl;
        }
    }

    private java.lang.String createId(java.lang.String text, int start, int end) {
        synchronized(mLock) {
            return android.view.textclassifier.SelectionSessionLogger.createId(text, start, end, mContext, mAnnotatorModelInUse.getVersion(), mAnnotatorModelInUse.getSupportedLocales());
        }
    }

    private static java.lang.String concatenateLocales(@android.annotation.Nullable
    android.os.LocaleList locales) {
        return locales == null ? "" : locales.toLanguageTags();
    }

    private android.view.textclassifier.TextClassification createClassificationResult(com.google.android.textclassifier.AnnotatorModel[] classifications, java.lang.String text, int start, int end, @android.annotation.Nullable
    java.time.Instant referenceTime) {
        final java.lang.String classifiedText = text.substring(start, end);
        final android.view.textclassifier.TextClassification.Builder builder = new android.view.textclassifier.TextClassification.Builder().setText(classifiedText);
        final int typeCount = classifications.length;
        com.google.android.textclassifier.AnnotatorModel.ClassificationResult highestScoringResult = (typeCount > 0) ? classifications[0] : null;
        for (int i = 0; i < typeCount; i++) {
            builder.setEntityType(classifications[i]);
            if (classifications[i].getScore() > highestScoringResult.getScore()) {
                highestScoringResult = classifications[i];
            }
        }
        final android.util.Pair<android.os.Bundle, android.os.Bundle> languagesBundles = generateLanguageBundles(text, start, end);
        final android.os.Bundle textLanguagesBundle = languagesBundles.first;
        final android.os.Bundle foreignLanguageBundle = languagesBundles.second;
        builder.setForeignLanguageExtra(foreignLanguageBundle);
        boolean isPrimaryAction = true;
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> labeledIntents = mClassificationIntentFactory.create(mContext, classifiedText, foreignLanguageBundle != null, referenceTime, highestScoringResult);
        final android.view.textclassifier.intent.LabeledIntent.TitleChooser titleChooser = ( labeledIntent, resolveInfo) -> labeledIntent.titleWithoutEntity;
        for (android.view.textclassifier.intent.LabeledIntent labeledIntent : labeledIntents) {
            final android.view.textclassifier.intent.LabeledIntent.Result result = labeledIntent.resolve(mContext, titleChooser, textLanguagesBundle);
            if (result == null) {
                continue;
            }
            final android.content.Intent intent = result.resolvedIntent;
            final android.app.RemoteAction action = result.remoteAction;
            if (isPrimaryAction) {
                // For O backwards compatibility, the first RemoteAction is also written to the
                // legacy API fields.
                builder.setIcon(action.getIcon().loadDrawable(mContext));
                builder.setLabel(action.getTitle().toString());
                builder.setIntent(intent);
                builder.setOnClickListener(android.view.textclassifier.TextClassification.createIntentOnClickListener(android.view.textclassifier.TextClassification.createPendingIntent(mContext, intent, labeledIntent.requestCode)));
                isPrimaryAction = false;
            }
            builder.addAction(action, intent);
        }
        return builder.setId(createId(text, start, end)).build();
    }

    /**
     * Returns a bundle pair with language detection information for extras.
     * <p>
     * Pair.first = textLanguagesBundle - A bundle containing information about all detected
     * languages in the text. May be null if language detection fails or is disabled. This is
     * typically expected to be added to a textClassifier generated remote action intent.
     * See {@link ExtrasUtils#putTextLanguagesExtra(Bundle, Bundle)}.
     * See {@link ExtrasUtils#getTopLanguage(Intent)}.
     * <p>
     * Pair.second = foreignLanguageBundle - A bundle with the language and confidence score if the
     * system finds the text to be in a foreign language. Otherwise is null.
     * See {@link TextClassification.Builder#setForeignLanguageExtra(Bundle)}.
     *
     * @param context
     * 		the context of the text to detect languages for
     * @param start
     * 		the start index of the text
     * @param end
     * 		the end index of the text
     */
    // TODO: Revisit this algorithm.
    // TODO: Consider making this public API.
    private android.util.Pair<android.os.Bundle, android.os.Bundle> generateLanguageBundles(java.lang.String context, int start, int end) {
        if (!mSettings.isTranslateInClassificationEnabled()) {
            return null;
        }
        try {
            final float threshold = getLangIdThreshold();
            if ((threshold < 0) || (threshold > 1)) {
                android.view.textclassifier.Log.w(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "[detectForeignLanguage] unexpected threshold is found: " + threshold);
                return android.util.Pair.create(null, null);
            }
            final android.view.textclassifier.EntityConfidence languageScores = detectLanguages(context, start, end);
            if (languageScores.getEntities().isEmpty()) {
                return android.util.Pair.create(null, null);
            }
            final android.os.Bundle textLanguagesBundle = new android.os.Bundle();
            android.view.textclassifier.ExtrasUtils.putTopLanguageScores(textLanguagesBundle, languageScores);
            final java.lang.String language = languageScores.getEntities().get(0);
            final float score = languageScores.getConfidenceScore(language);
            if (score < threshold) {
                return android.util.Pair.create(textLanguagesBundle, null);
            }
            android.view.textclassifier.Log.v(android.view.textclassifier.TextClassifierImpl.LOG_TAG, java.lang.String.format(java.util.Locale.US, "Language detected: <%s:%.2f>", language, score));
            final java.util.Locale detected = new java.util.Locale(language);
            final android.os.LocaleList deviceLocales = android.os.LocaleList.getDefault();
            final int size = deviceLocales.size();
            for (int i = 0; i < size; i++) {
                if (deviceLocales.get(i).getLanguage().equals(detected.getLanguage())) {
                    return android.util.Pair.create(textLanguagesBundle, null);
                }
            }
            final android.os.Bundle foreignLanguageBundle = android.view.textclassifier.ExtrasUtils.createForeignLanguageExtra(detected.getLanguage(), score, getLangIdImpl().getVersion());
            return android.util.Pair.create(textLanguagesBundle, foreignLanguageBundle);
        } catch (java.lang.Throwable t) {
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error generating language bundles.", t);
        }
        return android.util.Pair.create(null, null);
    }

    /**
     * Detect the language of a piece of text by taking surrounding text into consideration.
     *
     * @param text
     * 		text providing context for the text for which its language is to be detected
     * @param start
     * 		the start index of the text to detect its language
     * @param end
     * 		the end index of the text to detect its language
     */
    // TODO: Revisit this algorithm.
    private android.view.textclassifier.EntityConfidence detectLanguages(java.lang.String text, int start, int end) throws java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkArgument(start >= 0);
        com.android.internal.util.Preconditions.checkArgument(end <= text.length());
        com.android.internal.util.Preconditions.checkArgument(start <= end);
        final float[] langIdContextSettings = mSettings.getLangIdContextSettings();
        // The minimum size of text to prefer for detection.
        final int minimumTextSize = ((int) (langIdContextSettings[0]));
        // For reducing the score when text is less than the preferred size.
        final float penalizeRatio = langIdContextSettings[1];
        // Original detection score to surrounding text detection score ratios.
        final float subjectTextScoreRatio = langIdContextSettings[2];
        final float moreTextScoreRatio = 1.0F - subjectTextScoreRatio;
        android.view.textclassifier.Log.v(android.view.textclassifier.TextClassifierImpl.LOG_TAG, java.lang.String.format(java.util.Locale.US, "LangIdContextSettings: " + ("minimumTextSize=%d, penalizeRatio=%.2f, " + "subjectTextScoreRatio=%.2f, moreTextScoreRatio=%.2f"), minimumTextSize, penalizeRatio, subjectTextScoreRatio, moreTextScoreRatio));
        if (((end - start) < minimumTextSize) && (penalizeRatio <= 0)) {
            return new android.view.textclassifier.EntityConfidence(java.util.Collections.emptyMap());
        }
        final java.lang.String subject = text.substring(start, end);
        final android.view.textclassifier.EntityConfidence scores = detectLanguages(subject);
        if (((subject.length() >= minimumTextSize) || (subject.length() == text.length())) || ((subjectTextScoreRatio * penalizeRatio) >= 1)) {
            return scores;
        }
        final android.view.textclassifier.EntityConfidence moreTextScores;
        if (moreTextScoreRatio >= 0) {
            // Attempt to grow the detection text to be at least minimumTextSize long.
            final java.lang.String moreText = android.view.textclassifier.TextClassifier.Utils.getSubString(text, start, end, minimumTextSize);
            moreTextScores = detectLanguages(moreText);
        } else {
            moreTextScores = new android.view.textclassifier.EntityConfidence(java.util.Collections.emptyMap());
        }
        // Combine the original detection scores with the those returned after including more text.
        final java.util.Map<java.lang.String, java.lang.Float> newScores = new android.util.ArrayMap();
        final java.util.Set<java.lang.String> languages = new android.util.ArraySet();
        languages.addAll(scores.getEntities());
        languages.addAll(moreTextScores.getEntities());
        for (java.lang.String language : languages) {
            final float score = ((subjectTextScoreRatio * scores.getConfidenceScore(language)) + (moreTextScoreRatio * moreTextScores.getConfidenceScore(language))) * penalizeRatio;
            newScores.put(language, score);
        }
        return new android.view.textclassifier.EntityConfidence(newScores);
    }

    /**
     * Detect languages for the specified text.
     */
    private android.view.textclassifier.EntityConfidence detectLanguages(java.lang.String text) throws java.io.FileNotFoundException {
        final com.google.android.textclassifier.LangIdModel langId = getLangIdImpl();
        final com.google.android.textclassifier.LangIdModel[] langResults = langId.detectLanguages(text);
        final java.util.Map<java.lang.String, java.lang.Float> languagesMap = new android.util.ArrayMap();
        for (com.google.android.textclassifier.LangIdModel.LanguageResult langResult : langResults) {
            languagesMap.put(langResult.getLanguage(), langResult.getScore());
        }
        return new android.view.textclassifier.EntityConfidence(languagesMap);
    }

    private float getLangIdThreshold() {
        try {
            return mSettings.getLangIdThresholdOverride() >= 0 ? mSettings.getLangIdThresholdOverride() : getLangIdImpl().getLangIdThreshold();
        } catch (java.io.FileNotFoundException e) {
            final float defaultThreshold = 0.5F;
            android.view.textclassifier.Log.v(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Using default foreign language threshold: " + defaultThreshold);
            return defaultThreshold;
        }
    }

    @java.lang.Override
    public void dump(@android.annotation.NonNull
    com.android.internal.util.IndentingPrintWriter printWriter) {
        synchronized(mLock) {
            printWriter.println("TextClassifierImpl:");
            printWriter.increaseIndent();
            printWriter.println("Annotator model file(s):");
            printWriter.increaseIndent();
            for (android.view.textclassifier.ModelFileManager.ModelFile modelFile : mAnnotatorModelFileManager.listModelFiles()) {
                printWriter.println(modelFile.toString());
            }
            printWriter.decreaseIndent();
            printWriter.println("LangID model file(s):");
            printWriter.increaseIndent();
            for (android.view.textclassifier.ModelFileManager.ModelFile modelFile : mLangIdModelFileManager.listModelFiles()) {
                printWriter.println(modelFile.toString());
            }
            printWriter.decreaseIndent();
            printWriter.println("Actions model file(s):");
            printWriter.increaseIndent();
            for (android.view.textclassifier.ModelFileManager.ModelFile modelFile : mActionsModelFileManager.listModelFiles()) {
                printWriter.println(modelFile.toString());
            }
            printWriter.decreaseIndent();
            printWriter.printPair("mFallback", mFallback);
            printWriter.decreaseIndent();
            printWriter.println();
        }
    }

    /**
     * Closes the ParcelFileDescriptor, if non-null, and logs any errors that occur.
     */
    private static void maybeCloseAndLogError(@android.annotation.Nullable
    android.os.ParcelFileDescriptor fd) {
        if (fd == null) {
            return;
        }
        try {
            fd.close();
        } catch (java.io.IOException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassifierImpl.LOG_TAG, "Error closing file.", e);
        }
    }

    /**
     * Returns the locales string for the current resources configuration.
     */
    private java.lang.String getResourceLocalesString() {
        try {
            return mContext.getResources().getConfiguration().getLocales().toLanguageTags();
        } catch (java.lang.NullPointerException e) {
            // NPE is unexpected. Erring on the side of caution.
            return android.os.LocaleList.getDefault().toLanguageTags();
        }
    }
}

