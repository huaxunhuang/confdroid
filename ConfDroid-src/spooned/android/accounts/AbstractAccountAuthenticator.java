/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.accounts;


/**
 * Abstract base class for creating AccountAuthenticators.
 * In order to be an authenticator one must extend this class, provider implementations for the
 * abstract methods and write a service that returns the result of {@link #getIBinder()}
 * in the service's {@link android.app.Service#onBind(android.content.Intent)} when invoked
 * with an intent with action {@link AccountManager#ACTION_AUTHENTICATOR_INTENT}. This service
 * must specify the following intent filter and metadata tags in its AndroidManifest.xml file
 * <pre>
 *   &lt;intent-filter&gt;
 *     &lt;action android:name="android.accounts.AccountAuthenticator" /&gt;
 *   &lt;/intent-filter&gt;
 *   &lt;meta-data android:name="android.accounts.AccountAuthenticator"
 *             android:resource="@xml/authenticator" /&gt;
 * </pre>
 * The <code>android:resource</code> attribute must point to a resource that looks like:
 * <pre>
 * &lt;account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
 *    android:accountType="typeOfAuthenticator"
 *    android:icon="@drawable/icon"
 *    android:smallIcon="@drawable/miniIcon"
 *    android:label="@string/label"
 *    android:accountPreferences="@xml/account_preferences"
 * /&gt;
 * </pre>
 * Replace the icons and labels with your own resources. The <code>android:accountType</code>
 * attribute must be a string that uniquely identifies your authenticator and will be the same
 * string that user will use when making calls on the {@link AccountManager} and it also
 * corresponds to {@link Account#type} for your accounts. One user of the android:icon is the
 * "Account & Sync" settings page and one user of the android:smallIcon is the Contact Application's
 * tab panels.
 * <p>
 * The preferences attribute points to a PreferenceScreen xml hierarchy that contains
 * a list of PreferenceScreens that can be invoked to manage the authenticator. An example is:
 * <pre>
 * &lt;PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"&gt;
 *    &lt;PreferenceCategory android:title="@string/title_fmt" /&gt;
 *    &lt;PreferenceScreen
 *         android:key="key1"
 *         android:title="@string/key1_action"
 *         android:summary="@string/key1_summary"&gt;
 *         &lt;intent
 *             android:action="key1.ACTION"
 *             android:targetPackage="key1.package"
 *             android:targetClass="key1.class" /&gt;
 *     &lt;/PreferenceScreen&gt;
 * &lt;/PreferenceScreen&gt;
 * </pre>
 *
 * <p>
 * The standard pattern for implementing any of the abstract methods is the following:
 * <ul>
 * <li> If the supplied arguments are enough for the authenticator to fully satisfy the request
 * then it will do so and return a {@link Bundle} that contains the results.
 * <li> If the authenticator needs information from the user to satisfy the request then it
 * will create an {@link Intent} to an activity that will prompt the user for the information
 * and then carry out the request. This intent must be returned in a Bundle as key
 * {@link AccountManager#KEY_INTENT}.
 * <p>
 * The activity needs to return the final result when it is complete so the Intent should contain
 * the {@link AccountAuthenticatorResponse} as {@link AccountManager#KEY_ACCOUNT_MANAGER_RESPONSE}.
 * The activity must then call {@link AccountAuthenticatorResponse#onResult} or
 * {@link AccountAuthenticatorResponse#onError} when it is complete.
 * <li> If the authenticator cannot synchronously process the request and return a result then it
 * may choose to return null and then use the AccountManagerResponse to send the result
 * when it has completed the request.
 * </ul>
 * <p>
 * The following descriptions of each of the abstract authenticator methods will not describe the
 * possible asynchronous nature of the request handling and will instead just describe the input
 * parameters and the expected result.
 * <p>
 * When writing an activity to satisfy these requests one must pass in the AccountManagerResponse
 * and return the result via that response when the activity finishes (or whenever else  the
 * activity author deems it is the correct time to respond).
 * The {@link AccountAuthenticatorActivity} handles this, so one may wish to extend that when
 * writing activities to handle these requests.
 */
public abstract class AbstractAccountAuthenticator {
    private static final java.lang.String TAG = "AccountAuthenticator";

    /**
     * Bundle key used for the {@code long} expiration time (in millis from the unix epoch) of the
     * associated auth token.
     *
     * @see #getAuthToken
     */
    public static final java.lang.String KEY_CUSTOM_TOKEN_EXPIRY = "android.accounts.expiry";

    /**
     * Bundle key used for the {@link String} account type in session bundle.
     * This is used in the default implementation of
     * {@link #startAddAccountSession} and {@link startUpdateCredentialsSession}.
     */
    private static final java.lang.String KEY_AUTH_TOKEN_TYPE = "android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE";

    /**
     * Bundle key used for the {@link String} array of required features in
     * session bundle. This is used in the default implementation of
     * {@link #startAddAccountSession} and {@link startUpdateCredentialsSession}.
     */
    private static final java.lang.String KEY_REQUIRED_FEATURES = "android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES";

    /**
     * Bundle key used for the {@link Bundle} options in session bundle. This is
     * used in default implementation of {@link #startAddAccountSession} and
     * {@link startUpdateCredentialsSession}.
     */
    private static final java.lang.String KEY_OPTIONS = "android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS";

    /**
     * Bundle key used for the {@link Account} account in session bundle. This is used
     * used in default implementation of {@link startUpdateCredentialsSession}.
     */
    private static final java.lang.String KEY_ACCOUNT = "android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT";

    private final android.content.Context mContext;

    public AbstractAccountAuthenticator(android.content.Context context) {
        mContext = context;
    }

    private class Transport extends android.accounts.IAccountAuthenticator.Stub {
        @java.lang.Override
        public void addAccount(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] features, android.os.Bundle options) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, (((("addAccount: accountType " + accountType) + ", authTokenType ") + authTokenType) + ", features ") + (features == null ? "[]" : java.util.Arrays.toString(features)));
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.addAccount(new android.accounts.AccountAuthenticatorResponse(response), accountType, authTokenType, features, options);
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "addAccount: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "addAccount", accountType, e);
            }
        }

        @java.lang.Override
        public void confirmCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle options) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "confirmCredentials: " + account);
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.confirmCredentials(new android.accounts.AccountAuthenticatorResponse(response), account, options);
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "confirmCredentials: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "confirmCredentials", account.toString(), e);
            }
        }

        @java.lang.Override
        public void getAuthTokenLabel(android.accounts.IAccountAuthenticatorResponse response, java.lang.String authTokenType) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "getAuthTokenLabel: authTokenType " + authTokenType);
            }
            checkBinderPermission();
            try {
                android.os.Bundle result = new android.os.Bundle();
                result.putString(android.accounts.AccountManager.KEY_AUTH_TOKEN_LABEL, android.accounts.AbstractAccountAuthenticator.this.getAuthTokenLabel(authTokenType));
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "getAuthTokenLabel: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                response.onResult(result);
            } catch (java.lang.Exception e) {
                handleException(response, "getAuthTokenLabel", authTokenType, e);
            }
        }

        @java.lang.Override
        public void getAuthToken(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle loginOptions) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, (("getAuthToken: " + account) + ", authTokenType ") + authTokenType);
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.getAuthToken(new android.accounts.AccountAuthenticatorResponse(response), account, authTokenType, loginOptions);
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "getAuthToken: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "getAuthToken", (account.toString() + ",") + authTokenType, e);
            }
        }

        @java.lang.Override
        public void updateCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle loginOptions) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, (("updateCredentials: " + account) + ", authTokenType ") + authTokenType);
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.updateCredentials(new android.accounts.AccountAuthenticatorResponse(response), account, authTokenType, loginOptions);
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    // Result may be null.
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "updateCredentials: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "updateCredentials", (account.toString() + ",") + authTokenType, e);
            }
        }

        @java.lang.Override
        public void editProperties(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType) throws android.os.RemoteException {
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.editProperties(new android.accounts.AccountAuthenticatorResponse(response), accountType);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "editProperties", accountType, e);
            }
        }

        @java.lang.Override
        public void hasFeatures(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String[] features) throws android.os.RemoteException {
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.hasFeatures(new android.accounts.AccountAuthenticatorResponse(response), account, features);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "hasFeatures", account.toString(), e);
            }
        }

        @java.lang.Override
        public void getAccountRemovalAllowed(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException {
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.getAccountRemovalAllowed(new android.accounts.AccountAuthenticatorResponse(response), account);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "getAccountRemovalAllowed", account.toString(), e);
            }
        }

        @java.lang.Override
        public void getAccountCredentialsForCloning(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException {
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.getAccountCredentialsForCloning(new android.accounts.AccountAuthenticatorResponse(response), account);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "getAccountCredentialsForCloning", account.toString(), e);
            }
        }

        @java.lang.Override
        public void addAccountFromCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle accountCredentials) throws android.os.RemoteException {
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.addAccountFromCredentials(new android.accounts.AccountAuthenticatorResponse(response), account, accountCredentials);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "addAccountFromCredentials", account.toString(), e);
            }
        }

        @java.lang.Override
        public void startAddAccountSession(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] features, android.os.Bundle options) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, (((("startAddAccountSession: accountType " + accountType) + ", authTokenType ") + authTokenType) + ", features ") + (features == null ? "[]" : java.util.Arrays.toString(features)));
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.startAddAccountSession(new android.accounts.AccountAuthenticatorResponse(response), accountType, authTokenType, features, options);
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "startAddAccountSession: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "startAddAccountSession", accountType, e);
            }
        }

        @java.lang.Override
        public void startUpdateCredentialsSession(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle loginOptions) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, (("startUpdateCredentialsSession: " + account) + ", authTokenType ") + authTokenType);
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.startUpdateCredentialsSession(new android.accounts.AccountAuthenticatorResponse(response), account, authTokenType, loginOptions);
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    // Result may be null.
                    if (result != null) {
                        result.keySet();// force it to be unparcelled

                    }
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "startUpdateCredentialsSession: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "startUpdateCredentialsSession", (account.toString() + ",") + authTokenType, e);
            }
        }

        @java.lang.Override
        public void finishSession(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType, android.os.Bundle sessionBundle) throws android.os.RemoteException {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "finishSession: accountType " + accountType);
            }
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.finishSession(new android.accounts.AccountAuthenticatorResponse(response), accountType, sessionBundle);
                if (result != null) {
                    result.keySet();// force it to be unparcelled

                }
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, "finishSession: result " + android.accounts.AccountManager.sanitizeResult(result));
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "finishSession", accountType, e);
            }
        }

        @java.lang.Override
        public void isCredentialsUpdateSuggested(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String statusToken) throws android.os.RemoteException {
            checkBinderPermission();
            try {
                final android.os.Bundle result = android.accounts.AbstractAccountAuthenticator.this.isCredentialsUpdateSuggested(new android.accounts.AccountAuthenticatorResponse(response), account, statusToken);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (java.lang.Exception e) {
                handleException(response, "isCredentialsUpdateSuggested", account.toString(), e);
            }
        }
    }

    private void handleException(android.accounts.IAccountAuthenticatorResponse response, java.lang.String method, java.lang.String data, java.lang.Exception e) throws android.os.RemoteException {
        if (e instanceof android.accounts.NetworkErrorException) {
            if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, ((method + "(") + data) + ")", e);
            }
            response.onError(android.accounts.AccountManager.ERROR_CODE_NETWORK_ERROR, e.getMessage());
        } else
            if (e instanceof java.lang.UnsupportedOperationException) {
                if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                    android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, ((method + "(") + data) + ")", e);
                }
                response.onError(android.accounts.AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION, method + " not supported");
            } else
                if (e instanceof java.lang.IllegalArgumentException) {
                    if (android.util.Log.isLoggable(android.accounts.AbstractAccountAuthenticator.TAG, android.util.Log.VERBOSE)) {
                        android.util.Log.v(android.accounts.AbstractAccountAuthenticator.TAG, ((method + "(") + data) + ")", e);
                    }
                    response.onError(android.accounts.AccountManager.ERROR_CODE_BAD_ARGUMENTS, method + " not supported");
                } else {
                    android.util.Log.w(android.accounts.AbstractAccountAuthenticator.TAG, ((method + "(") + data) + ")", e);
                    response.onError(android.accounts.AccountManager.ERROR_CODE_REMOTE_EXCEPTION, method + " failed");
                }


    }

    private void checkBinderPermission() {
        final int uid = android.os.Binder.getCallingUid();
        final java.lang.String perm = Manifest.permission.ACCOUNT_MANAGER;
        if (mContext.checkCallingOrSelfPermission(perm) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            throw new java.lang.SecurityException((("caller uid " + uid) + " lacks ") + perm);
        }
    }

    private android.accounts.AbstractAccountAuthenticator.Transport mTransport = new android.accounts.AbstractAccountAuthenticator.Transport();

    /**
     *
     *
     * @return the IBinder for the AccountAuthenticator
     */
    public final android.os.IBinder getIBinder() {
        return mTransport.asBinder();
    }

    /**
     * Returns a Bundle that contains the Intent of the activity that can be used to edit the
     * properties. In order to indicate success the activity should call response.setResult()
     * with a non-null Bundle.
     *
     * @param response
     * 		used to set the result for the request. If the Constants.INTENT_KEY
     * 		is set in the bundle then this response field is to be used for sending future
     * 		results if and when the Intent is started.
     * @param accountType
     * 		the AccountType whose properties are to be edited.
     * @return a Bundle containing the result or the Intent to start to continue the request.
    If this is null then the request is considered to still be active and the result should
    sent later using response.
     */
    public abstract android.os.Bundle editProperties(android.accounts.AccountAuthenticatorResponse response, java.lang.String accountType);

    /**
     * Adds an account of the specified accountType.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param accountType
     * 		the type of account to add, will never be null
     * @param authTokenType
     * 		the type of auth token to retrieve after adding the account, may be null
     * @param requiredFeatures
     * 		a String array of authenticator-specific features that the added
     * 		account must support, may be null
     * @param options
     * 		a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
    will contain either:
    <ul>
    <li> {@link AccountManager#KEY_INTENT}, or
    <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
    the account that was added, or
    <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
    indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     */
    public abstract android.os.Bundle addAccount(android.accounts.AccountAuthenticatorResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, android.os.Bundle options) throws android.accounts.NetworkErrorException;

    /**
     * Checks that the user knows the credentials of an account.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account whose credentials are to be checked, will never be null
     * @param options
     * 		a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
    will contain either:
    <ul>
    <li> {@link AccountManager#KEY_INTENT}, or
    <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the check succeeded, false otherwise
    <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
    indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     */
    public abstract android.os.Bundle confirmCredentials(android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle options) throws android.accounts.NetworkErrorException;

    /**
     * Gets an authtoken for an account.
     *
     * If not {@code null}, the resultant {@link Bundle} will contain different sets of keys
     * depending on whether a token was successfully issued and, if not, whether one
     * could be issued via some {@link android.app.Activity}.
     * <p>
     * If a token cannot be provided without some additional activity, the Bundle should contain
     * {@link AccountManager#KEY_INTENT} with an associated {@link Intent}. On the other hand, if
     * there is no such activity, then a Bundle containing
     * {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} should be
     * returned.
     * <p>
     * If a token can be successfully issued, the implementation should return the
     * {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of the
     * account associated with the token as well as the {@link AccountManager#KEY_AUTHTOKEN}. In
     * addition {@link AbstractAccountAuthenticator} implementations that declare themselves
     * {@code android:customTokens=true} may also provide a non-negative {@link #KEY_CUSTOM_TOKEN_EXPIRY} long value containing the expiration timestamp of the expiration
     * time (in millis since the unix epoch).
     * <p>
     * Implementers should assume that tokens will be cached on the basis of account and
     * authTokenType. The system may ignore the contents of the supplied options Bundle when
     * determining to re-use a cached token. Furthermore, implementers should assume a supplied
     * expiration time will be treated as non-binding advice.
     * <p>
     * Finally, note that for android:customTokens=false authenticators, tokens are cached
     * indefinitely until some client calls {@link AccountManager#invalidateAuthToken(String,String)}.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account whose credentials are to be retrieved, will never be null
     * @param authTokenType
     * 		the type of auth token to retrieve, will never be null
     * @param options
     * 		a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     */
    public abstract android.os.Bundle getAuthToken(android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.accounts.NetworkErrorException;

    /**
     * Ask the authenticator for a localized label for the given authTokenType.
     *
     * @param authTokenType
     * 		the authTokenType whose label is to be returned, will never be null
     * @return the localized label of the auth token type, may be null if the type isn't known
     */
    public abstract java.lang.String getAuthTokenLabel(java.lang.String authTokenType);

    /**
     * Update the locally stored credentials for an account.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account whose credentials are to be updated, will never be null
     * @param authTokenType
     * 		the type of auth token to retrieve after updating the credentials,
     * 		may be null
     * @param options
     * 		a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
    will contain either:
    <ul>
    <li> {@link AccountManager#KEY_INTENT}, or
    <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
    the account whose credentials were updated, or
    <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
    indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     */
    public abstract android.os.Bundle updateCredentials(android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.accounts.NetworkErrorException;

    /**
     * Checks if the account supports all the specified authenticator specific features.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account to check, will never be null
     * @param features
     * 		an array of features to check, will never be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
    will contain either:
    <ul>
    <li> {@link AccountManager#KEY_INTENT}, or
    <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the account has all the features,
    false otherwise
    <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
    indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     */
    public abstract android.os.Bundle hasFeatures(android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String[] features) throws android.accounts.NetworkErrorException;

    /**
     * Checks if the removal of this account is allowed.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account to check, will never be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
    will contain either:
    <ul>
    <li> {@link AccountManager#KEY_INTENT}, or
    <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the removal of the account is
    allowed, false otherwise
    <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
    indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     */
    public android.os.Bundle getAccountRemovalAllowed(android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account) throws android.accounts.NetworkErrorException {
        final android.os.Bundle result = new android.os.Bundle();
        result.putBoolean(android.accounts.AccountManager.KEY_BOOLEAN_RESULT, true);
        return result;
    }

    /**
     * Returns a Bundle that contains whatever is required to clone the account on a different
     * user. The Bundle is passed to the authenticator instance in the target user via
     * {@link #addAccountFromCredentials(AccountAuthenticatorResponse, Account, Bundle)}.
     * The default implementation returns null, indicating that cloning is not supported.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account to clone, will never be null
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException
     * 		
     * @see {@link #addAccountFromCredentials(AccountAuthenticatorResponse, Account, Bundle)}
     */
    public android.os.Bundle getAccountCredentialsForCloning(final android.accounts.AccountAuthenticatorResponse response, final android.accounts.Account account) throws android.accounts.NetworkErrorException {
        new java.lang.Thread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.os.Bundle result = new android.os.Bundle();
                result.putBoolean(android.accounts.AccountManager.KEY_BOOLEAN_RESULT, false);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    /**
     * Creates an account based on credentials provided by the authenticator instance of another
     * user on the device, who has chosen to share the account with this user.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null
     * @param account
     * 		the account to clone, will never be null
     * @param accountCredentials
     * 		the Bundle containing the required credentials to create the
     * 		account. Contents of the Bundle are only meaningful to the authenticator. This Bundle is
     * 		provided by {@link #getAccountCredentialsForCloning(AccountAuthenticatorResponse, Account)}.
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException
     * 		
     * @see {@link #getAccountCredentialsForCloning(AccountAuthenticatorResponse, Account)}
     */
    public android.os.Bundle addAccountFromCredentials(final android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle accountCredentials) throws android.accounts.NetworkErrorException {
        new java.lang.Thread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.os.Bundle result = new android.os.Bundle();
                result.putBoolean(android.accounts.AccountManager.KEY_BOOLEAN_RESULT, false);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    /**
     * Starts the add account session to authenticate user to an account of the
     * specified accountType. No file I/O should be performed in this call.
     * Account should be added to device only when {@link #finishSession} is
     * called after this.
     * <p>
     * Note: when overriding this method, {@link #finishSession} should be
     * overridden too.
     * </p>
     *
     * @param response
     * 		to send the result back to the AccountManager, will never
     * 		be null
     * @param accountType
     * 		the type of account to authenticate with, will never
     * 		be null
     * @param authTokenType
     * 		the type of auth token to retrieve after
     * 		authenticating with the account, may be null
     * @param requiredFeatures
     * 		a String array of authenticator-specific features
     * 		that the account authenticated with must support, may be null
     * @param options
     * 		a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the
    response. The result will contain either:
    <ul>
    <li>{@link AccountManager#KEY_INTENT}, or
    <li>{@link AccountManager#KEY_ACCOUNT_SESSION_BUNDLE} for adding
    the account to device later, and if account is authenticated,
    optional {@link AccountManager#KEY_PASSWORD} and
    {@link AccountManager#KEY_ACCOUNT_STATUS_TOKEN} for checking the
    status of the account, or
    <li>{@link AccountManager#KEY_ERROR_CODE} and
    {@link AccountManager#KEY_ERROR_MESSAGE} to indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the
     * 		request due to a network error
     * @see #finishSession(AccountAuthenticatorResponse, String, Bundle)
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.os.Bundle startAddAccountSession(final android.accounts.AccountAuthenticatorResponse response, final java.lang.String accountType, final java.lang.String authTokenType, final java.lang.String[] requiredFeatures, final android.os.Bundle options) throws android.accounts.NetworkErrorException {
        new java.lang.Thread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.os.Bundle sessionBundle = new android.os.Bundle();
                sessionBundle.putString(android.accounts.AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE, authTokenType);
                sessionBundle.putStringArray(android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES, requiredFeatures);
                sessionBundle.putBundle(android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS, options);
                android.os.Bundle result = new android.os.Bundle();
                result.putBundle(android.accounts.AccountManager.KEY_ACCOUNT_SESSION_BUNDLE, sessionBundle);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    /**
     * Asks user to re-authenticate for an account but defers updating the
     * locally stored credentials. No file I/O should be performed in this call.
     * Local credentials should be updated only when {@link #finishSession} is
     * called after this.
     * <p>
     * Note: when overriding this method, {@link #finishSession} should be
     * overridden too.
     * </p>
     *
     * @param response
     * 		to send the result back to the AccountManager, will never
     * 		be null
     * @param account
     * 		the account whose credentials are to be updated, will
     * 		never be null
     * @param authTokenType
     * 		the type of auth token to retrieve after updating
     * 		the credentials, may be null
     * @param options
     * 		a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the
    response. The result will contain either:
    <ul>
    <li>{@link AccountManager#KEY_INTENT}, or
    <li>{@link AccountManager#KEY_ACCOUNT_SESSION_BUNDLE} for
    updating the locally stored credentials later, and if account is
    re-authenticated, optional {@link AccountManager#KEY_PASSWORD}
    and {@link AccountManager#KEY_ACCOUNT_STATUS_TOKEN} for checking
    the status of the account later, or
    <li>{@link AccountManager#KEY_ERROR_CODE} and
    {@link AccountManager#KEY_ERROR_MESSAGE} to indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the
     * 		request due to a network error
     * @see #finishSession(AccountAuthenticatorResponse, String, Bundle)
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.os.Bundle startUpdateCredentialsSession(final android.accounts.AccountAuthenticatorResponse response, final android.accounts.Account account, final java.lang.String authTokenType, final android.os.Bundle options) throws android.accounts.NetworkErrorException {
        new java.lang.Thread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.os.Bundle sessionBundle = new android.os.Bundle();
                sessionBundle.putString(android.accounts.AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE, authTokenType);
                sessionBundle.putParcelable(android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT, account);
                sessionBundle.putBundle(android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS, options);
                android.os.Bundle result = new android.os.Bundle();
                result.putBundle(android.accounts.AccountManager.KEY_ACCOUNT_SESSION_BUNDLE, sessionBundle);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    /**
     * Finishes the session started by #startAddAccountSession or
     * #startUpdateCredentials by installing the account to device with
     * AccountManager, or updating the local credentials. File I/O may be
     * performed in this call.
     * <p>
     * Note: when overriding this method, {@link #startAddAccountSession} and
     * {@link #startUpdateCredentialsSession} should be overridden too.
     * </p>
     *
     * @param response
     * 		to send the result back to the AccountManager, will never
     * 		be null
     * @param accountType
     * 		the type of account to authenticate with, will never
     * 		be null
     * @param sessionBundle
     * 		a bundle of session data created by
     * 		{@link #startAddAccountSession} used for adding account to
     * 		device, or by {@link #startUpdateCredentialsSession} used for
     * 		updating local credentials.
     * @return a Bundle result or null if the result is to be returned via the
    response. The result will contain either:
    <ul>
    <li>{@link AccountManager#KEY_INTENT}, or
    <li>{@link AccountManager#KEY_ACCOUNT_NAME} and
    {@link AccountManager#KEY_ACCOUNT_TYPE} of the account that was
    added or local credentials were updated, or
    <li>{@link AccountManager#KEY_ERROR_CODE} and
    {@link AccountManager#KEY_ERROR_MESSAGE} to indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     * @see #startAddAccountSession and #startUpdateCredentialsSession
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.os.Bundle finishSession(final android.accounts.AccountAuthenticatorResponse response, final java.lang.String accountType, final android.os.Bundle sessionBundle) throws android.accounts.NetworkErrorException {
        if (android.text.TextUtils.isEmpty(accountType)) {
            android.util.Log.e(android.accounts.AbstractAccountAuthenticator.TAG, "Account type cannot be empty.");
            android.os.Bundle result = new android.os.Bundle();
            result.putInt(android.accounts.AccountManager.KEY_ERROR_CODE, android.accounts.AccountManager.ERROR_CODE_BAD_ARGUMENTS);
            result.putString(android.accounts.AccountManager.KEY_ERROR_MESSAGE, "accountType cannot be empty.");
            return result;
        }
        if (sessionBundle == null) {
            android.util.Log.e(android.accounts.AbstractAccountAuthenticator.TAG, "Session bundle cannot be null.");
            android.os.Bundle result = new android.os.Bundle();
            result.putInt(android.accounts.AccountManager.KEY_ERROR_CODE, android.accounts.AccountManager.ERROR_CODE_BAD_ARGUMENTS);
            result.putString(android.accounts.AccountManager.KEY_ERROR_MESSAGE, "sessionBundle cannot be null.");
            return result;
        }
        if (!sessionBundle.containsKey(android.accounts.AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE)) {
            // We cannot handle Session bundle not created by default startAddAccountSession(...)
            // nor startUpdateCredentialsSession(...) implementation. Return error.
            android.os.Bundle result = new android.os.Bundle();
            result.putInt(android.accounts.AccountManager.KEY_ERROR_CODE, android.accounts.AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION);
            result.putString(android.accounts.AccountManager.KEY_ERROR_MESSAGE, "Authenticator must override finishSession if startAddAccountSession" + " or startUpdateCredentialsSession is overridden.");
            response.onResult(result);
            return result;
        }
        java.lang.String authTokenType = sessionBundle.getString(android.accounts.AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE);
        android.os.Bundle options = sessionBundle.getBundle(android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS);
        java.lang.String[] requiredFeatures = sessionBundle.getStringArray(android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES);
        android.accounts.Account account = sessionBundle.getParcelable(android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT);
        boolean containsKeyAccount = sessionBundle.containsKey(android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT);
        // Actual options passed to add account or update credentials flow.
        android.os.Bundle sessionOptions = new android.os.Bundle(sessionBundle);
        // Remove redundant extras in session bundle before passing it to addAccount(...) or
        // updateCredentials(...).
        sessionOptions.remove(android.accounts.AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE);
        sessionOptions.remove(android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES);
        sessionOptions.remove(android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS);
        sessionOptions.remove(android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT);
        if (options != null) {
            // options may contains old system info such as
            // AccountManager.KEY_ANDROID_PACKAGE_NAME required by the add account flow or update
            // credentials flow, we should replace with the new values of the current call added
            // to sessionBundle by AccountManager or AccountManagerService.
            options.putAll(sessionOptions);
            sessionOptions = options;
        }
        // Session bundle created by startUpdateCredentialsSession default implementation should
        // contain KEY_ACCOUNT.
        if (containsKeyAccount) {
            return updateCredentials(response, account, authTokenType, options);
        }
        // Otherwise, session bundle was created by startAddAccountSession default implementation.
        return addAccount(response, accountType, authTokenType, requiredFeatures, sessionOptions);
    }

    /**
     * Checks if update of the account credentials is suggested.
     *
     * @param response
     * 		to send the result back to the AccountManager, will never be null.
     * @param account
     * 		the account to check, will never be null
     * @param statusToken
     * 		a String of token to check if update of credentials is suggested.
     * @return a Bundle result or null if the result is to be returned via the response. The result
    will contain either:
    <ul>
    <li>{@link AccountManager#KEY_BOOLEAN_RESULT}, true if update of account's
    credentials is suggested, false otherwise
    <li>{@link AccountManager#KEY_ERROR_CODE} and
    {@link AccountManager#KEY_ERROR_MESSAGE} to indicate an error
    </ul>
     * @throws NetworkErrorException
     * 		if the authenticator could not honor the request due to a
     * 		network error
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.os.Bundle isCredentialsUpdateSuggested(final android.accounts.AccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String statusToken) throws android.accounts.NetworkErrorException {
        android.os.Bundle result = new android.os.Bundle();
        result.putBoolean(android.accounts.AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}

