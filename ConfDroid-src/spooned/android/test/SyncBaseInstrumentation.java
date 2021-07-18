/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.test;


/**
 * If you would like to test sync a single provider with an
 * {@link InstrumentationTestCase}, this provides some of the boiler plate in {@link #setUp} and
 * {@link #tearDown}.
 *
 * @deprecated Use
<a href="{@docRoot }reference/android/support/test/InstrumentationRegistry.html">
InstrumentationRegistry</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class SyncBaseInstrumentation extends android.test.InstrumentationTestCase {
    private android.content.Context mTargetContext;

    android.content.ContentResolver mContentResolver;

    private static final int MAX_TIME_FOR_SYNC_IN_MINS = 20;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mTargetContext = getInstrumentation().getTargetContext();
        mContentResolver = mTargetContext.getContentResolver();
    }

    /**
     * Syncs the specified provider.
     *
     * @throws Exception
     * 		
     */
    protected void syncProvider(android.net.Uri uri, java.lang.String accountName, java.lang.String authority) throws java.lang.Exception {
        android.os.Bundle extras = new android.os.Bundle();
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_IGNORE_SETTINGS, true);
        android.accounts.Account account = new android.accounts.Account(accountName, "com.google");
        android.content.ContentResolver.requestSync(account, authority, extras);
        long startTimeInMillis = android.os.SystemClock.elapsedRealtime();
        long endTimeInMillis = startTimeInMillis + (android.test.SyncBaseInstrumentation.MAX_TIME_FOR_SYNC_IN_MINS * 60000);
        int counter = 0;
        // Making sure race condition does not occur when en entry have been removed from pending
        // and active tables and loaded in memory (therefore sync might be still in progress)
        while (counter < 2) {
            // Sleep for 1 second.
            java.lang.Thread.sleep(1000);
            // Finish test if time to sync has exceeded max time.
            if (android.os.SystemClock.elapsedRealtime() > endTimeInMillis) {
                break;
            }
            if (android.content.ContentResolver.isSyncActive(account, authority)) {
                counter = 0;
                continue;
            }
            counter++;
        } 
    }

    protected void cancelSyncsandDisableAutoSync() {
        android.content.ContentResolver.setMasterSyncAutomatically(false);
        /* all accounts */
        /* all authorities */
        android.content.ContentResolver.cancelSync(null, null);
    }
}

