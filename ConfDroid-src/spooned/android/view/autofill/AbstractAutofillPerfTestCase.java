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
 * Base class for all autofill tests.
 */
@androidx.test.filters.LargeTest
public abstract class AbstractAutofillPerfTestCase {
    @org.junit.ClassRule
    public static final android.perftests.utils.SettingsStateKeeperRule mServiceSettingsKeeper = new android.perftests.utils.SettingsStateKeeperRule(androidx.test.InstrumentationRegistry.getTargetContext(), Settings.Secure.AUTOFILL_SERVICE);

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity>(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    private final int mLayoutId;

    protected AbstractAutofillPerfTestCase(int layoutId) {
        mLayoutId = layoutId;
    }

    /**
     * Prepares the activity so that by the time the test is run it has reference to its fields.
     */
    @org.junit.Before
    public void prepareActivity() throws java.lang.Throwable {
        mActivityRule.runOnUiThread(() -> {
            assertTrue("We should be running on the main thread", android.os.Looper.getMainLooper().getThread() == java.lang.Thread.currentThread());
            assertTrue("We should be running on the main thread", android.os.Looper.myLooper() == android.os.Looper.getMainLooper());
            android.perftests.utils.StubActivity activity = mActivityRule.getActivity();
            activity.setContentView(mLayoutId);
            onCreate(activity);
        });
    }

    @org.junit.Before
    public void enableService() {
        android.view.autofill.MyAutofillService.resetStaticState();
        android.view.autofill.MyAutofillService.setEnabled(true);
    }

    @org.junit.After
    public void disableService() {
        // Must disable service so calls are ignored in case of errors during the test case;
        // otherwise, other tests will fail because these calls are made in the UI thread (as both
        // the service, the tests, and the app run in the same process).
        android.view.autofill.MyAutofillService.setEnabled(false);
    }

    /**
     * Initializes the {@link StubActivity} after it was launched.
     */
    protected abstract void onCreate(android.perftests.utils.StubActivity activity);

    /**
     * Uses the {@code settings} binary to set the autofill service.
     */
    protected void setService() {
        android.perftests.utils.SettingsHelper.syncSet(androidx.test.InstrumentationRegistry.getTargetContext(), SettingsHelper.NAMESPACE_SECURE, Settings.Secure.AUTOFILL_SERVICE, android.view.autofill.MyAutofillService.COMPONENT_NAME);
    }

    /**
     * Uses the {@code settings} binary to reset the autofill service.
     */
    protected void resetService() {
        android.perftests.utils.SettingsHelper.syncDelete(androidx.test.InstrumentationRegistry.getTargetContext(), SettingsHelper.NAMESPACE_SECURE, Settings.Secure.AUTOFILL_SERVICE);
    }
}

