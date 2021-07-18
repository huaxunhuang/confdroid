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
 * Proxy to the system's default TextClassifier.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
public final class SystemTextClassifier implements android.view.textclassifier.TextClassifier {
    private static final java.lang.String LOG_TAG = "SystemTextClassifier";

    private final android.service.textclassifier.ITextClassifierService mManagerService;

    private final android.view.textclassifier.TextClassificationConstants mSettings;

    private final android.view.textclassifier.TextClassifier mFallback;

    private final java.lang.String mPackageName;

    private android.view.textclassifier.TextClassificationSessionId mSessionId;

    public SystemTextClassifier(android.content.Context context, android.view.textclassifier.TextClassificationConstants settings) throws ServiceManager.ServiceNotFoundException {
        mManagerService = ITextClassifierService.Stub.asInterface(android.os.ServiceManager.getServiceOrThrow(android.content.Context.TEXT_CLASSIFICATION_SERVICE));
        mSettings = com.android.internal.util.Preconditions.checkNotNull(settings);
        mFallback = context.getSystemService(android.view.textclassifier.TextClassificationManager.class).getTextClassifier(android.view.textclassifier.TextClassifier.LOCAL);
        mPackageName = com.android.internal.util.Preconditions.checkNotNull(context.getOpPackageName());
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
            request.setCallingPackageName(mPackageName);
            final android.view.textclassifier.SystemTextClassifier.BlockingCallback<android.view.textclassifier.TextSelection> callback = new android.view.textclassifier.SystemTextClassifier.BlockingCallback("textselection");
            mManagerService.onSuggestSelection(mSessionId, request, callback);
            final android.view.textclassifier.TextSelection selection = callback.get();
            if (selection != null) {
                return selection;
            }
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error suggesting selection for text. Using fallback.", e);
        }
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
            request.setCallingPackageName(mPackageName);
            final android.view.textclassifier.SystemTextClassifier.BlockingCallback<android.view.textclassifier.TextClassification> callback = new android.view.textclassifier.SystemTextClassifier.BlockingCallback("textclassification");
            mManagerService.onClassifyText(mSessionId, request, callback);
            final android.view.textclassifier.TextClassification classification = callback.get();
            if (classification != null) {
                return classification;
            }
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error classifying text. Using fallback.", e);
        }
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
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        if ((!mSettings.isSmartLinkifyEnabled()) && request.isLegacyFallback()) {
            return android.view.textclassifier.TextClassifier.Utils.generateLegacyLinks(request);
        }
        try {
            request.setCallingPackageName(mPackageName);
            final android.view.textclassifier.SystemTextClassifier.BlockingCallback<android.view.textclassifier.TextLinks> callback = new android.view.textclassifier.SystemTextClassifier.BlockingCallback("textlinks");
            mManagerService.onGenerateLinks(mSessionId, request, callback);
            final android.view.textclassifier.TextLinks links = callback.get();
            if (links != null) {
                return links;
            }
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error generating links. Using fallback.", e);
        }
        return mFallback.generateLinks(request);
    }

    @java.lang.Override
    public void onSelectionEvent(android.view.textclassifier.SelectionEvent event) {
        com.android.internal.util.Preconditions.checkNotNull(event);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            mManagerService.onSelectionEvent(mSessionId, event);
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error reporting selection event.", e);
        }
    }

    @java.lang.Override
    public void onTextClassifierEvent(@android.annotation.NonNull
    android.view.textclassifier.TextClassifierEvent event) {
        com.android.internal.util.Preconditions.checkNotNull(event);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            mManagerService.onTextClassifierEvent(mSessionId, event);
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error reporting textclassifier event.", e);
        }
    }

    @java.lang.Override
    public android.view.textclassifier.TextLanguage detectLanguage(android.view.textclassifier.TextLanguage.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            request.setCallingPackageName(mPackageName);
            final android.view.textclassifier.SystemTextClassifier.BlockingCallback<android.view.textclassifier.TextLanguage> callback = new android.view.textclassifier.SystemTextClassifier.BlockingCallback("textlanguage");
            mManagerService.onDetectLanguage(mSessionId, request, callback);
            final android.view.textclassifier.TextLanguage textLanguage = callback.get();
            if (textLanguage != null) {
                return textLanguage;
            }
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error detecting language.", e);
        }
        return mFallback.detectLanguage(request);
    }

    @java.lang.Override
    public android.view.textclassifier.ConversationActions suggestConversationActions(android.view.textclassifier.ConversationActions.Request request) {
        com.android.internal.util.Preconditions.checkNotNull(request);
        android.view.textclassifier.TextClassifier.Utils.checkMainThread();
        try {
            request.setCallingPackageName(mPackageName);
            final android.view.textclassifier.SystemTextClassifier.BlockingCallback<android.view.textclassifier.ConversationActions> callback = new android.view.textclassifier.SystemTextClassifier.BlockingCallback("conversation-actions");
            mManagerService.onSuggestConversationActions(mSessionId, request, callback);
            final android.view.textclassifier.ConversationActions conversationActions = callback.get();
            if (conversationActions != null) {
                return conversationActions;
            }
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error reporting selection event.", e);
        }
        return mFallback.suggestConversationActions(request);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.WorkerThread
    public int getMaxGenerateLinksTextLength() {
        // TODO: retrieve this from the bound service.
        return mFallback.getMaxGenerateLinksTextLength();
    }

    @java.lang.Override
    public void destroy() {
        try {
            if (mSessionId != null) {
                mManagerService.onDestroyTextClassificationSession(mSessionId);
            }
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error destroying classification session.", e);
        }
    }

    @java.lang.Override
    public void dump(@android.annotation.NonNull
    com.android.internal.util.IndentingPrintWriter printWriter) {
        printWriter.println("SystemTextClassifier:");
        printWriter.increaseIndent();
        printWriter.printPair("mFallback", mFallback);
        printWriter.printPair("mPackageName", mPackageName);
        printWriter.printPair("mSessionId", mSessionId);
        printWriter.decreaseIndent();
        printWriter.println();
    }

    /**
     * Attempts to initialize a new classification session.
     *
     * @param classificationContext
     * 		the classification context
     * @param sessionId
     * 		the session's id
     */
    void initializeRemoteSession(@android.annotation.NonNull
    android.view.textclassifier.TextClassificationContext classificationContext, @android.annotation.NonNull
    android.view.textclassifier.TextClassificationSessionId sessionId) {
        mSessionId = com.android.internal.util.Preconditions.checkNotNull(sessionId);
        try {
            mManagerService.onCreateTextClassificationSession(classificationContext, mSessionId);
        } catch (android.os.RemoteException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Error starting a new classification session.", e);
        }
    }

    private static final class BlockingCallback<T extends android.os.Parcelable> extends android.service.textclassifier.ITextClassifierCallback.Stub {
        private final android.view.textclassifier.SystemTextClassifier.ResponseReceiver<T> mReceiver;

        BlockingCallback(java.lang.String name) {
            mReceiver = new android.view.textclassifier.SystemTextClassifier.ResponseReceiver<>(name);
        }

        @java.lang.Override
        public void onSuccess(android.os.Bundle result) {
            mReceiver.onSuccess(android.service.textclassifier.TextClassifierService.getResponse(result));
        }

        @java.lang.Override
        public void onFailure() {
            mReceiver.onFailure();
        }

        public T get() {
            return mReceiver.get();
        }
    }

    private static final class ResponseReceiver<T> {
        private final java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);

        private final java.lang.String mName;

        private T mResponse;

        private ResponseReceiver(java.lang.String name) {
            mName = name;
        }

        public void onSuccess(T response) {
            mResponse = response;
            mLatch.countDown();
        }

        public void onFailure() {
            android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Request failed.", null);
            mLatch.countDown();
        }

        @android.annotation.Nullable
        public T get() {
            // If this is running on the main thread, do not block for a response.
            // The response will unfortunately be null and the TextClassifier should depend on its
            // fallback.
            // NOTE that TextClassifier calls should preferably always be called on a worker thread.
            if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
                try {
                    boolean success = mLatch.await(2, java.util.concurrent.TimeUnit.SECONDS);
                    if (!success) {
                        android.view.textclassifier.Log.w(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Timeout in ResponseReceiver.get(): " + mName);
                    }
                } catch (java.lang.InterruptedException e) {
                    java.lang.Thread.currentThread().interrupt();
                    android.view.textclassifier.Log.e(android.view.textclassifier.SystemTextClassifier.LOG_TAG, "Interrupted during ResponseReceiver.get(): " + mName, e);
                }
            }
            return mResponse;
        }
    }
}

