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
 *
 *
 * @unknown 
 */
public class GrantCredentialsPermissionActivity extends android.app.Activity implements android.view.View.OnClickListener {
    public static final java.lang.String EXTRAS_ACCOUNT = "account";

    public static final java.lang.String EXTRAS_AUTH_TOKEN_TYPE = "authTokenType";

    public static final java.lang.String EXTRAS_RESPONSE = "response";

    public static final java.lang.String EXTRAS_REQUESTING_UID = "uid";

    private android.accounts.Account mAccount;

    private java.lang.String mAuthTokenType;

    private int mUid;

    private android.os.Bundle mResultBundle = null;

    protected android.view.LayoutInflater mInflater;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grant_credentials_permission);
        setTitle(R.string.grant_permissions_header_text);
        mInflater = ((android.view.LayoutInflater) (getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        final android.os.Bundle extras = getIntent().getExtras();
        if (extras == null) {
            // we were somehow started with bad parameters. abort the activity.
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
            return;
        }
        // Grant 'account'/'type' to mUID
        mAccount = extras.getParcelable(android.accounts.GrantCredentialsPermissionActivity.EXTRAS_ACCOUNT);
        mAuthTokenType = extras.getString(android.accounts.GrantCredentialsPermissionActivity.EXTRAS_AUTH_TOKEN_TYPE);
        mUid = extras.getInt(android.accounts.GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID);
        final android.content.pm.PackageManager pm = getPackageManager();
        final java.lang.String[] packages = pm.getPackagesForUid(mUid);
        if (((mAccount == null) || (mAuthTokenType == null)) || (packages == null)) {
            // we were somehow started with bad parameters. abort the activity.
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
            return;
        }
        java.lang.String accountTypeLabel;
        try {
            accountTypeLabel = getAccountLabel(mAccount);
        } catch (java.lang.IllegalArgumentException e) {
            // label or resource was missing. abort the activity.
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
            return;
        }
        final android.widget.TextView authTokenTypeView = ((android.widget.TextView) (findViewById(R.id.authtoken_type)));
        authTokenTypeView.setVisibility(android.view.View.GONE);
        final android.accounts.AccountManagerCallback<java.lang.String> callback = new android.accounts.AccountManagerCallback<java.lang.String>() {
            public void run(android.accounts.AccountManagerFuture<java.lang.String> future) {
                try {
                    final java.lang.String authTokenLabel = future.getResult();
                    if (!android.text.TextUtils.isEmpty(authTokenLabel)) {
                        runOnUiThread(new java.lang.Runnable() {
                            public void run() {
                                if (!isFinishing()) {
                                    authTokenTypeView.setText(authTokenLabel);
                                    authTokenTypeView.setVisibility(android.view.View.VISIBLE);
                                }
                            }
                        });
                    }
                } catch (android.accounts.OperationCanceledException e) {
                } catch (java.io.IOException e) {
                } catch (android.accounts.AuthenticatorException e) {
                }
            }
        };
        if (!android.accounts.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE.equals(mAuthTokenType)) {
            android.accounts.AccountManager.get(this).getAuthTokenLabel(mAccount.type, mAuthTokenType, callback, null);
        }
        findViewById(R.id.allow_button).setOnClickListener(this);
        findViewById(R.id.deny_button).setOnClickListener(this);
        android.widget.LinearLayout packagesListView = ((android.widget.LinearLayout) (findViewById(R.id.packages_list)));
        for (java.lang.String pkg : packages) {
            java.lang.String packageLabel;
            try {
                packageLabel = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString();
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                packageLabel = pkg;
            }
            packagesListView.addView(newPackageView(packageLabel));
        }
        ((android.widget.TextView) (findViewById(R.id.account_name))).setText(mAccount.name);
        ((android.widget.TextView) (findViewById(R.id.account_type))).setText(accountTypeLabel);
    }

    private java.lang.String getAccountLabel(android.accounts.Account account) {
        final android.accounts.AuthenticatorDescription[] authenticatorTypes = android.accounts.AccountManager.get(this).getAuthenticatorTypes();
        for (int i = 0, N = authenticatorTypes.length; i < N; i++) {
            final android.accounts.AuthenticatorDescription desc = authenticatorTypes[i];
            if (desc.type.equals(account.type)) {
                try {
                    return createPackageContext(desc.packageName, 0).getString(desc.labelId);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    return account.type;
                } catch (android.content.res.Resources.NotFoundException e) {
                    return account.type;
                }
            }
        }
        return account.type;
    }

    private android.view.View newPackageView(java.lang.String packageLabel) {
        android.view.View view = mInflater.inflate(R.layout.permissions_package_list_item, null);
        ((android.widget.TextView) (view.findViewById(R.id.package_label))).setText(packageLabel);
        return view;
    }

    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.allow_button :
                android.accounts.AccountManager.get(this).updateAppPermission(mAccount, mAuthTokenType, mUid, true);
                android.content.Intent result = new android.content.Intent();
                result.putExtra("retry", true);
                setResult(android.app.Activity.RESULT_OK, result);
                setAccountAuthenticatorResult(result.getExtras());
                break;
            case R.id.deny_button :
                android.accounts.AccountManager.get(this).updateAppPermission(mAccount, mAuthTokenType, mUid, false);
                setResult(android.app.Activity.RESULT_CANCELED);
                break;
        }
        finish();
    }

    public final void setAccountAuthenticatorResult(android.os.Bundle result) {
        mResultBundle = result;
    }

    /**
     * Sends the result or a {@link AccountManager#ERROR_CODE_CANCELED} error if a
     * result isn't present.
     */
    public void finish() {
        android.content.Intent intent = getIntent();
        android.accounts.AccountAuthenticatorResponse response = intent.getParcelableExtra(android.accounts.GrantCredentialsPermissionActivity.EXTRAS_RESPONSE);
        if (response != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                response.onResult(mResultBundle);
            } else {
                response.onError(android.accounts.AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
        }
        super.finish();
    }
}

