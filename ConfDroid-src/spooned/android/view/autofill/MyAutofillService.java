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
 * limitations under the License
 */
package android.view.autofill;


/**
 * An {@link AutofillService} implementation whose replies can be programmed by the test case.
 */
public class MyAutofillService extends android.service.autofill.AutofillService {
    private static final java.lang.String TAG = "MyAutofillService";

    private static final int TIMEOUT_MS = 5000;

    private static final java.lang.String PACKAGE_NAME = "com.android.perftests.autofill";

    static final java.lang.String COMPONENT_NAME = android.view.autofill.MyAutofillService.PACKAGE_NAME + "/android.view.autofill.MyAutofillService";

    private static final java.util.concurrent.BlockingQueue<android.service.autofill.FillRequest> sFillRequests = new java.util.concurrent.LinkedBlockingQueue<>();

    private static final java.util.concurrent.BlockingQueue<android.view.autofill.MyAutofillService.CannedResponse> sCannedResponses = new java.util.concurrent.LinkedBlockingQueue<>();

    private static boolean sEnabled;

    /**
     * Resets the static state associated with the service.
     */
    static void resetStaticState() {
        android.view.autofill.MyAutofillService.sFillRequests.clear();
        android.view.autofill.MyAutofillService.sCannedResponses.clear();
        android.view.autofill.MyAutofillService.sEnabled = false;
    }

    /**
     * Sets whether the service is enabled or not - when disabled, calls to
     * {@link #onFillRequest(FillRequest, CancellationSignal, FillCallback)} will be ignored.
     */
    static void setEnabled(boolean enabled) {
        android.view.autofill.MyAutofillService.sEnabled = enabled;
    }

    /**
     * Gets the the last {@link FillRequest} passed to
     * {@link #onFillRequest(FillRequest, CancellationSignal, FillCallback)} or throws an
     * exception if that method was not called.
     */
    @androidx.annotation.NonNull
    static android.service.autofill.FillRequest getLastFillRequest() {
        android.service.autofill.FillRequest request = null;
        try {
            request = android.view.autofill.MyAutofillService.sFillRequests.poll(android.view.autofill.MyAutofillService.TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException("onFillRequest() interrupted");
        }
        if (request == null) {
            throw new java.lang.IllegalStateException(("onFillRequest() not called in " + android.view.autofill.MyAutofillService.TIMEOUT_MS) + "ms");
        }
        return request;
    }

    @java.lang.Override
    public void onFillRequest(android.service.autofill.FillRequest request, android.os.CancellationSignal cancellationSignal, android.service.autofill.FillCallback callback) {
        try {
            handleRequest(request, callback);
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            onError("onFillRequest() interrupted", e, callback);
        } catch (java.lang.Exception e) {
            onError("exception on onFillRequest()", e, callback);
        }
    }

    private void handleRequest(android.service.autofill.FillRequest request, android.service.autofill.FillCallback callback) throws java.lang.Exception {
        if (!android.view.autofill.MyAutofillService.sEnabled) {
            onError("ignoring onFillRequest(): service is disabled", callback);
            return;
        }
        android.view.autofill.MyAutofillService.CannedResponse response = android.view.autofill.MyAutofillService.sCannedResponses.poll(android.view.autofill.MyAutofillService.TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
        if (response == null) {
            onError("ignoring onFillRequest(): response not set", callback);
            return;
        }
        android.service.autofill.Dataset.Builder dataset = new android.service.autofill.Dataset.Builder(android.view.autofill.MyAutofillService.newDatasetPresentation("dataset"));
        boolean hasData = false;
        if (response.mUsername != null) {
            hasData = true;
            dataset.setValue(response.mUsername.first, android.view.autofill.AutofillValue.forText(response.mUsername.second));
        }
        if (response.mPassword != null) {
            hasData = true;
            dataset.setValue(response.mPassword.first, android.view.autofill.AutofillValue.forText(response.mPassword.second));
        }
        if (hasData) {
            android.service.autofill.FillResponse.Builder fillResponse = new android.service.autofill.FillResponse.Builder();
            if (response.mIgnoredIds != null) {
                fillResponse.setIgnoredIds(response.mIgnoredIds);
            }
            callback.onSuccess(fillResponse.addDataset(dataset.build()).build());
        } else {
            callback.onSuccess(null);
        }
        if (!android.view.autofill.MyAutofillService.sFillRequests.offer(request, android.view.autofill.MyAutofillService.TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS)) {
            android.util.Log.w(android.view.autofill.MyAutofillService.TAG, ("could not offer request in " + android.view.autofill.MyAutofillService.TIMEOUT_MS) + "ms");
        }
    }

    @java.lang.Override
    public void onSaveRequest(android.service.autofill.SaveRequest request, android.service.autofill.SaveCallback callback) {
        // No current test should have triggered it...
        android.util.Log.e(android.view.autofill.MyAutofillService.TAG, "onSaveRequest() should not have been called");
        callback.onFailure("onSaveRequest() should not have been called");
    }

    static final class CannedResponse {
        private final android.util.Pair<android.view.autofill.AutofillId, java.lang.String> mUsername;

        private final android.util.Pair<android.view.autofill.AutofillId, java.lang.String> mPassword;

        private final android.view.autofill.AutofillId[] mIgnoredIds;

        private CannedResponse(@androidx.annotation.NonNull
        android.view.autofill.MyAutofillService.CannedResponse.Builder builder) {
            mUsername = builder.mUsername;
            mPassword = builder.mPassword;
            mIgnoredIds = builder.mIgnoredIds;
        }

        static class Builder {
            private android.util.Pair<android.view.autofill.AutofillId, java.lang.String> mUsername;

            private android.util.Pair<android.view.autofill.AutofillId, java.lang.String> mPassword;

            private android.view.autofill.AutofillId[] mIgnoredIds;

            @androidx.annotation.NonNull
            android.view.autofill.MyAutofillService.CannedResponse.Builder setUsername(@androidx.annotation.NonNull
            android.view.autofill.AutofillId id, @androidx.annotation.NonNull
            java.lang.String value) {
                mUsername = new android.util.Pair(id, value);
                return this;
            }

            @androidx.annotation.NonNull
            android.view.autofill.MyAutofillService.CannedResponse.Builder setPassword(@androidx.annotation.NonNull
            android.view.autofill.AutofillId id, @androidx.annotation.NonNull
            java.lang.String value) {
                mPassword = new android.util.Pair(id, value);
                return this;
            }

            @androidx.annotation.NonNull
            android.view.autofill.MyAutofillService.CannedResponse.Builder setIgnored(android.view.autofill.AutofillId... ids) {
                mIgnoredIds = ids;
                return this;
            }

            void reply() {
                android.view.autofill.MyAutofillService.sCannedResponses.add(new android.view.autofill.MyAutofillService.CannedResponse(this));
            }
        }
    }

    /**
     * Sets the expected canned {@link FillResponse} for the next
     * {@link AutofillService#onFillRequest(FillRequest, CancellationSignal, FillCallback)}.
     */
    static android.view.autofill.MyAutofillService.CannedResponse.Builder newCannedResponse() {
        return new android.view.autofill.MyAutofillService.CannedResponse.Builder();
    }

    private void onError(@androidx.annotation.NonNull
    java.lang.String msg, @androidx.annotation.NonNull
    android.service.autofill.FillCallback callback) {
        android.util.Log.e(android.view.autofill.MyAutofillService.TAG, msg);
        callback.onFailure(msg);
    }

    private void onError(@androidx.annotation.NonNull
    java.lang.String msg, @androidx.annotation.NonNull
    java.lang.Exception e, @androidx.annotation.NonNull
    android.service.autofill.FillCallback callback) {
        android.util.Log.e(android.view.autofill.MyAutofillService.TAG, msg, e);
        callback.onFailure(msg);
    }

    @androidx.annotation.NonNull
    private static android.widget.RemoteViews newDatasetPresentation(@androidx.annotation.NonNull
    java.lang.CharSequence text) {
        android.widget.RemoteViews presentation = new android.widget.RemoteViews(android.view.autofill.MyAutofillService.PACKAGE_NAME, R.layout.autofill_dataset_picker_text_only);
        presentation.setTextViewText(R.id.text, text);
        return presentation;
    }
}

