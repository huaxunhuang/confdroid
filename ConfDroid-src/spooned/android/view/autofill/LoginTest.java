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


public class LoginTest extends android.view.autofill.AbstractAutofillPerfTestCase {
    private android.widget.EditText mUsername;

    private android.widget.EditText mPassword;

    private android.view.autofill.AutofillManager mAfm;

    public LoginTest() {
        super(R.layout.test_autofill_login);
    }

    @java.lang.Override
    protected void onCreate(android.perftests.utils.StubActivity activity) {
        android.view.View root = activity.getWindow().getDecorView();
        mUsername = root.findViewById(R.id.username);
        mPassword = root.findViewById(R.id.password);
        mAfm = activity.getSystemService(android.view.autofill.AutofillManager.class);
    }

    /**
     * This is the baseline test for focusing the 2 views when autofill is disabled.
     */
    @org.junit.Test
    public void testFocus_noService() throws java.lang.Throwable {
        resetService();
        mActivityRule.runOnUiThread(() -> {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            while (state.keepRunning()) {
                mUsername.requestFocus();
                mPassword.requestFocus();
            } 
        });
    }

    /**
     * This time the service is called, but it returns a {@code null} response so the UI behaves
     * as if autofill was disabled.
     */
    @org.junit.Test
    public void testFocus_serviceDoesNotAutofill() throws java.lang.Throwable {
        android.view.autofill.MyAutofillService.newCannedResponse().reply();
        setService();
        // Must first focus in a field to trigger autofill and wait for service response
        // outside the loop
        mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
        android.view.autofill.MyAutofillService.getLastFillRequest();
        // Then focus on password so loop start with focus away from username
        mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
        mActivityRule.runOnUiThread(() -> {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            while (state.keepRunning()) {
                mUsername.requestFocus();
                mPassword.requestFocus();
            } 
        });
    }

    /**
     * Now the service returns autofill data, for both username and password.
     */
    @org.junit.Test
    public void testFocus_autofillBothFields() throws java.lang.Throwable {
        android.view.autofill.MyAutofillService.newCannedResponse().setUsername(mUsername.getAutofillId(), "user").setPassword(mPassword.getAutofillId(), "pass").reply();
        setService();
        // Callback is used to slow down the calls made to the autofill server so the
        // app is not crashed due to binder exhaustion. But the time spent waiting for the callbacks
        // is not measured here...
        android.view.autofill.MyAutofillCallback callback = new android.view.autofill.MyAutofillCallback();
        mAfm.registerCallback(callback);
        // Must first trigger autofill and wait for service response outside the loop
        mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
        android.view.autofill.MyAutofillService.getLastFillRequest();
        callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
        // Then focus on password so loop start with focus away from username
        mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
        callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
        callback.expectEvent(mPassword, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
        // NOTE: we cannot run the whole loop inside the UI thread, because the autofill callback
        // is called on it, which would cause a deadlock on expectEvent().
        try {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            while (state.keepRunning()) {
                mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
                state.pauseTiming();// Ignore time spent waiting for callbacks

                callback.expectEvent(mPassword, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
                callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
                state.resumeTiming();
                mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
                state.pauseTiming();// Ignore time spent waiting for callbacks

                callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
                callback.expectEvent(mPassword, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
                state.resumeTiming();
            } 
            // Sanity check
            callback.assertNoAsyncErrors();
        } finally {
            mAfm.unregisterCallback(callback);
        }
    }

    /**
     * Now the service returns autofill data, but just for username.
     */
    @org.junit.Test
    public void testFocus_autofillUsernameOnly() throws java.lang.Throwable {
        // Must set ignored ids so focus on password does not trigger new requests
        android.view.autofill.MyAutofillService.newCannedResponse().setUsername(mUsername.getAutofillId(), "user").setIgnored(mPassword.getAutofillId()).reply();
        setService();
        // Callback is used to slow down the calls made to the autofill server so the
        // app is not crashed due to binder exhaustion. But the time spent waiting for the callbacks
        // is not measured here...
        android.view.autofill.MyAutofillCallback callback = new android.view.autofill.MyAutofillCallback();
        mAfm.registerCallback(callback);
        // Must first trigger autofill and wait for service response outside the loop
        mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
        android.view.autofill.MyAutofillService.getLastFillRequest();
        callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
        // Then focus on password so loop start with focus away from username
        mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
        callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
        // NOTE: we cannot run the whole loop inside the UI thread, because the autofill callback
        // is called on it, which would cause a deadlock on expectEvent().
        try {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            while (state.keepRunning()) {
                mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
                state.pauseTiming();// Ignore time spent waiting for callbacks

                callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
                state.resumeTiming();
                mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
                state.pauseTiming();// Ignore time spent waiting for callbacks

                callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
                state.resumeTiming();
            } 
            // Sanity check
            callback.assertNoAsyncErrors();
        } finally {
            mAfm.unregisterCallback(callback);
        }
    }

    /**
     * This is the baseline test for changing the 2 views when autofill is disabled.
     */
    @org.junit.Test
    public void testChange_noService() throws java.lang.Throwable {
        resetService();
        changeTest(false);
    }

    /**
     * This time the service is called, but it returns a {@code null} response so the UI behaves
     * as if autofill was disabled.
     */
    @org.junit.Test
    public void testChange_serviceDoesNotAutofill() throws java.lang.Throwable {
        android.view.autofill.MyAutofillService.newCannedResponse().reply();
        setService();
        changeTest(true);
    }

    /**
     * Now the service returns autofill data, for both username and password.
     */
    @org.junit.Test
    public void testChange_autofillBothFields() throws java.lang.Throwable {
        android.view.autofill.MyAutofillService.newCannedResponse().setUsername(mUsername.getAutofillId(), "user").setPassword(mPassword.getAutofillId(), "pass").reply();
        setService();
        changeTest(true);
    }

    /**
     * Now the service returns autofill data, but just for username.
     */
    @org.junit.Test
    public void testChange_autofillUsernameOnly() throws java.lang.Throwable {
        // Must set ignored ids so focus on password does not trigger new requests
        android.view.autofill.MyAutofillService.newCannedResponse().setUsername(mUsername.getAutofillId(), "user").setIgnored(mPassword.getAutofillId()).reply();
        setService();
        changeTest(true);
    }

    private void changeTest(boolean waitForService) throws java.lang.Throwable {
        // Must first focus in a field to trigger autofill and wait for service response
        // outside the loop
        mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
        if (waitForService) {
            android.view.autofill.MyAutofillService.getLastFillRequest();
        }
        mActivityRule.runOnUiThread(() -> {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            while (state.keepRunning()) {
                mUsername.setText("");
                mUsername.setText("a");
                mPassword.setText("");
                mPassword.setText("x");
            } 
        });
    }

    @org.junit.Test
    public void testCallbacks() throws java.lang.Throwable {
        android.view.autofill.MyAutofillService.newCannedResponse().setUsername(mUsername.getAutofillId(), "user").setPassword(mPassword.getAutofillId(), "pass").reply();
        setService();
        android.view.autofill.MyAutofillCallback callback = new android.view.autofill.MyAutofillCallback();
        mAfm.registerCallback(callback);
        // Must first focus in a field to trigger autofill and wait for service response
        // outside the loop
        mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
        android.view.autofill.MyAutofillService.getLastFillRequest();
        callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
        // Now focus on password to prepare loop state
        mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
        callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
        callback.expectEvent(mPassword, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
        // NOTE: we cannot run the whole loop inside the UI thread, because the autofill callback
        // is called on it, which would cause a deadlock on expectEvent().
        try {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            while (state.keepRunning()) {
                mActivityRule.runOnUiThread(() -> mUsername.requestFocus());
                callback.expectEvent(mPassword, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
                callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
                mActivityRule.runOnUiThread(() -> mPassword.requestFocus());
                callback.expectEvent(mUsername, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN);
                callback.expectEvent(mPassword, android.view.autofill.AutofillManager.AutofillCallback.EVENT_INPUT_SHOWN);
            } 
            // Sanity check
            callback.assertNoAsyncErrors();
        } finally {
            mAfm.unregisterCallback(callback);
        }
    }
}

