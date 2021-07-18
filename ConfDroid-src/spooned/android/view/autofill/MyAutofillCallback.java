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
 * Custom {@link AutofillCallback} used to recover events during tests.
 */
public final class MyAutofillCallback extends android.view.autofill.AutofillManager.AutofillCallback {
    private static final java.lang.String TAG = "MyAutofillCallback";

    private static final int TIMEOUT_MS = 5000;

    private final java.util.concurrent.BlockingQueue<android.view.autofill.MyAutofillCallback.MyEvent> mEvents = new java.util.concurrent.LinkedBlockingQueue<>(2);

    private final java.util.List<java.lang.String> mAsyncErrors = new java.util.ArrayList<>();

    @java.lang.Override
    public void onAutofillEvent(android.view.View view, int event) {
        boolean offered = false;
        try {
            offered = mEvents.offer(new android.view.autofill.MyAutofillCallback.MyEvent(view, event), android.view.autofill.MyAutofillCallback.TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
        }
        if (!offered) {
            java.lang.String error = ((("could not offer " + android.view.autofill.MyAutofillCallback.toString(view, event)) + " in ") + android.view.autofill.MyAutofillCallback.TIMEOUT_MS) + "ms";
            android.util.Log.e(android.view.autofill.MyAutofillCallback.TAG, error);
            mAsyncErrors.add(error);
        }
    }

    /**
     * Asserts the callback is called for the given view and event, or fail if it times out.
     */
    public void expectEvent(@androidx.annotation.NonNull
    android.view.View view, int event) {
        android.view.autofill.MyAutofillCallback.MyEvent myEvent;
        try {
            myEvent = mEvents.poll(android.view.autofill.MyAutofillCallback.TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (myEvent == null) {
                throw new java.lang.IllegalStateException((("no event received in " + android.view.autofill.MyAutofillCallback.TIMEOUT_MS) + "ms while waiting for ") + android.view.autofill.MyAutofillCallback.toString(view, event));
            }
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.IllegalStateException("interrupted waiting for " + android.view.autofill.MyAutofillCallback.toString(view, event));
        }
        if ((!myEvent.view.equals(view)) || (myEvent.event != event)) {
            throw new java.lang.AssertionError((("Invalid event: expected " + myEvent) + ", got ") + android.view.autofill.MyAutofillCallback.toString(view, event));
        }
    }

    /**
     * Throws an exception if an error happened asynchronously while handing
     * {@link #onAutofillEvent(View, int)}.
     */
    public void assertNoAsyncErrors() {
        if (!mAsyncErrors.isEmpty()) {
            throw new java.lang.IllegalStateException((mAsyncErrors.size() + " errors: ") + mAsyncErrors);
        }
    }

    private static java.lang.String eventToString(int event) {
        switch (event) {
            case android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN :
                return "HIDDEN";
            case android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN :
                return "SHOWN";
            case android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_UNAVAILABLE :
                return "UNAVAILABLE";
            default :
                throw new java.lang.IllegalArgumentException("invalid event: " + event);
        }
    }

    private static java.lang.String toString(android.view.View view, int event) {
        return ((android.view.autofill.MyAutofillCallback.eventToString(event) + ": ") + view) + ")";
    }

    private static final class MyEvent {
        public final android.view.View view;

        public final int event;

        MyEvent(android.view.View view, int event) {
            this.view = view;
            this.event = event;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return android.view.autofill.MyAutofillCallback.toString(view, event);
        }
    }
}

