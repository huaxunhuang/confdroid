/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.telecom;


/**
 * A generic stub account authenticator service often used for sync adapters that do not directly
 * involve accounts.
 *
 * @unknown 
 */
public class AuthenticatorService extends android.app.Service {
    private static android.telecom.AuthenticatorService.Authenticator mAuthenticator;

    @java.lang.Override
    public void onCreate() {
        android.telecom.AuthenticatorService.mAuthenticator = new android.telecom.AuthenticatorService.Authenticator(this);
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return android.telecom.AuthenticatorService.mAuthenticator.getIBinder();
    }

    /**
     * Stub account authenticator. All methods either return null or throw an exception.
     */
    public class Authenticator extends android.accounts.AbstractAccountAuthenticator {
        public Authenticator(android.content.Context context) {
            super(context);
        }

        @java.lang.Override
        public android.os.Bundle editProperties(android.accounts.AccountAuthenticatorResponse accountAuthenticatorResponse, java.lang.String s) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public android.os.Bundle addAccount(android.accounts.AccountAuthenticatorResponse accountAuthenticatorResponse, java.lang.String s, java.lang.String s2, java.lang.String[] strings, android.os.Bundle bundle) throws android.accounts.NetworkErrorException {
            return null;
        }

        @java.lang.Override
        public android.os.Bundle confirmCredentials(android.accounts.AccountAuthenticatorResponse accountAuthenticatorResponse, android.accounts.Account account, android.os.Bundle bundle) throws android.accounts.NetworkErrorException {
            return null;
        }

        @java.lang.Override
        public android.os.Bundle getAuthToken(android.accounts.AccountAuthenticatorResponse accountAuthenticatorResponse, android.accounts.Account account, java.lang.String s, android.os.Bundle bundle) throws android.accounts.NetworkErrorException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public java.lang.String getAuthTokenLabel(java.lang.String s) {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public android.os.Bundle updateCredentials(android.accounts.AccountAuthenticatorResponse accountAuthenticatorResponse, android.accounts.Account account, java.lang.String s, android.os.Bundle bundle) throws android.accounts.NetworkErrorException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public android.os.Bundle hasFeatures(android.accounts.AccountAuthenticatorResponse accountAuthenticatorResponse, android.accounts.Account account, java.lang.String[] strings) throws android.accounts.NetworkErrorException {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

