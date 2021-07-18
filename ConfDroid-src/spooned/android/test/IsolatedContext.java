/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * A mock context which prevents its users from talking to the rest of the device while
 * stubbing enough methods to satify code that tries to talk to other packages.
 *
 * @deprecated New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class IsolatedContext extends android.content.ContextWrapper {
    private android.content.ContentResolver mResolver;

    private final android.test.IsolatedContext.MockAccountManager mMockAccountManager;

    private java.util.List<android.content.Intent> mBroadcastIntents = com.google.android.collect.Lists.newArrayList();

    public IsolatedContext(android.content.ContentResolver resolver, android.content.Context targetContext) {
        super(targetContext);
        mResolver = resolver;
        mMockAccountManager = new android.test.IsolatedContext.MockAccountManager();
    }

    /**
     * Returns the list of intents that were broadcast since the last call to this method.
     */
    public java.util.List<android.content.Intent> getAndClearBroadcastIntents() {
        java.util.List<android.content.Intent> intents = mBroadcastIntents;
        mBroadcastIntents = com.google.android.collect.Lists.newArrayList();
        return intents;
    }

    @java.lang.Override
    public android.content.ContentResolver getContentResolver() {
        // We need to return the real resolver so that MailEngine.makeRight can get to the
        // subscribed feeds provider. TODO: mock out subscribed feeds too.
        return mResolver;
    }

    @java.lang.Override
    public boolean bindService(android.content.Intent service, android.content.ServiceConnection conn, int flags) {
        return false;
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
        return null;
    }

    @java.lang.Override
    public void unregisterReceiver(android.content.BroadcastReceiver receiver) {
        // Ignore
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent) {
        mBroadcastIntents.add(intent);
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        mBroadcastIntents.add(intent);
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags) {
        return android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags) {
        return android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    @java.lang.Override
    public java.lang.Object getSystemService(java.lang.String name) {
        if (android.content.Context.ACCOUNT_SERVICE.equals(name)) {
            return mMockAccountManager;
        }
        // No other services exist in this context.
        return null;
    }

    private class MockAccountManager extends android.accounts.AccountManager {
        public MockAccountManager() {
            /* IAccountManager */
            /* handler */
            super(android.test.IsolatedContext.this, null, null);
        }

        public void addOnAccountsUpdatedListener(android.accounts.OnAccountsUpdateListener listener, android.os.Handler handler, boolean updateImmediately) {
            // do nothing
        }

        public android.accounts.Account[] getAccounts() {
            return new android.accounts.Account[]{  };
        }

        public android.accounts.AccountManagerFuture<android.accounts.Account[]> getAccountsByTypeAndFeatures(final java.lang.String type, final java.lang.String[] features, android.accounts.AccountManagerCallback<android.accounts.Account[]> callback, android.os.Handler handler) {
            return new android.test.IsolatedContext.MockAccountManager.MockAccountManagerFuture<android.accounts.Account[]>(new android.accounts.Account[0]);
        }

        public java.lang.String blockingGetAuthToken(android.accounts.Account account, java.lang.String authTokenType, boolean notifyAuthFailure) throws android.accounts.AuthenticatorException, android.accounts.OperationCanceledException, java.io.IOException {
            return null;
        }

        /**
         * A very simple AccountManagerFuture class
         * that returns what ever was passed in
         */
        private class MockAccountManagerFuture<T> implements android.accounts.AccountManagerFuture<T> {
            T mResult;

            public MockAccountManagerFuture(T result) {
                mResult = result;
            }

            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            public boolean isCancelled() {
                return false;
            }

            public boolean isDone() {
                return true;
            }

            public T getResult() throws android.accounts.AuthenticatorException, android.accounts.OperationCanceledException, java.io.IOException {
                return mResult;
            }

            public T getResult(long timeout, java.util.concurrent.TimeUnit unit) throws android.accounts.AuthenticatorException, android.accounts.OperationCanceledException, java.io.IOException {
                return getResult();
            }
        }
    }

    @java.lang.Override
    public java.io.File getFilesDir() {
        return new java.io.File("/dev/null");
    }
}

