/**
 * Copyright (C) 2011 The Android Open Source Project
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
public class ChooseTypeAndAccountActivity extends android.app.Activity implements android.accounts.AccountManagerCallback<android.os.Bundle> {
    private static final java.lang.String TAG = "AccountChooser";

    /**
     * A Parcelable ArrayList of Account objects that limits the choosable accounts to those
     * in this list, if this parameter is supplied.
     */
    public static final java.lang.String EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST = "allowableAccounts";

    /**
     * A Parcelable ArrayList of String objects that limits the accounts to choose to those
     * that match the types in this list, if this parameter is supplied. This list is also
     * used to filter the allowable account types if add account is selected.
     */
    public static final java.lang.String EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY = "allowableAccountTypes";

    /**
     * This is passed as the addAccountOptions parameter in AccountManager.addAccount()
     * if it is called.
     */
    public static final java.lang.String EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE = "addAccountOptions";

    /**
     * This is passed as the requiredFeatures parameter in AccountManager.addAccount()
     * if it is called.
     */
    public static final java.lang.String EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY = "addAccountRequiredFeatures";

    /**
     * This is passed as the authTokenType string in AccountManager.addAccount()
     * if it is called.
     */
    public static final java.lang.String EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING = "authTokenType";

    /**
     * If set then the specified account is already "selected".
     */
    public static final java.lang.String EXTRA_SELECTED_ACCOUNT = "selectedAccount";

    /**
     * Deprecated. Providing this extra to {@link ChooseTypeAndAccountActivity}
     * will have no effect.
     */
    @java.lang.Deprecated
    public static final java.lang.String EXTRA_ALWAYS_PROMPT_FOR_ACCOUNT = "alwaysPromptForAccount";

    /**
     * If set then this string willb e used as the description rather than
     * the default.
     */
    public static final java.lang.String EXTRA_DESCRIPTION_TEXT_OVERRIDE = "descriptionTextOverride";

    public static final int REQUEST_NULL = 0;

    public static final int REQUEST_CHOOSE_TYPE = 1;

    public static final int REQUEST_ADD_ACCOUNT = 2;

    private static final java.lang.String KEY_INSTANCE_STATE_PENDING_REQUEST = "pendingRequest";

    private static final java.lang.String KEY_INSTANCE_STATE_EXISTING_ACCOUNTS = "existingAccounts";

    private static final java.lang.String KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME = "selectedAccountName";

    private static final java.lang.String KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT = "selectedAddAccount";

    private static final java.lang.String KEY_INSTANCE_STATE_ACCOUNT_LIST = "accountList";

    private static final int SELECTED_ITEM_NONE = -1;

    private java.util.Set<android.accounts.Account> mSetOfAllowableAccounts;

    private java.util.Set<java.lang.String> mSetOfRelevantAccountTypes;

    private java.lang.String mSelectedAccountName = null;

    private boolean mSelectedAddNewAccount = false;

    private java.lang.String mDescriptionOverride;

    private java.util.ArrayList<android.accounts.Account> mAccounts;

    private int mPendingRequest = android.accounts.ChooseTypeAndAccountActivity.REQUEST_NULL;

    private android.os.Parcelable[] mExistingAccounts = null;

    private int mSelectedItemIndex;

    private android.widget.Button mOkButton;

    private int mCallingUid;

    private java.lang.String mCallingPackage;

    private boolean mDisallowAddAccounts;

    private boolean mDontShowPicker;

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, ("ChooseTypeAndAccountActivity.onCreate(savedInstanceState=" + savedInstanceState) + ")");
        }
        java.lang.String message = null;
        try {
            android.os.IBinder activityToken = getActivityToken();
            mCallingUid = android.app.ActivityManagerNative.getDefault().getLaunchedFromUid(activityToken);
            mCallingPackage = android.app.ActivityManagerNative.getDefault().getLaunchedFromPackage(activityToken);
            if ((mCallingUid != 0) && (mCallingPackage != null)) {
                android.os.Bundle restrictions = android.os.UserManager.get(this).getUserRestrictions(new android.os.UserHandle(android.os.UserHandle.getUserId(mCallingUid)));
                mDisallowAddAccounts = restrictions.getBoolean(android.os.UserManager.DISALLOW_MODIFY_ACCOUNTS, false);
            }
        } catch (android.os.RemoteException re) {
            // Couldn't figure out caller details
            android.util.Log.w(getClass().getSimpleName(), "Unable to get caller identity \n" + re);
        }
        // save some items we use frequently
        final android.content.Intent intent = getIntent();
        if (savedInstanceState != null) {
            mPendingRequest = savedInstanceState.getInt(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_PENDING_REQUEST);
            mExistingAccounts = savedInstanceState.getParcelableArray(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_EXISTING_ACCOUNTS);
            // Makes sure that any user selection is preserved across orientation changes.
            mSelectedAccountName = savedInstanceState.getString(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME);
            mSelectedAddNewAccount = savedInstanceState.getBoolean(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
            mAccounts = savedInstanceState.getParcelableArrayList(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_ACCOUNT_LIST);
        } else {
            mPendingRequest = android.accounts.ChooseTypeAndAccountActivity.REQUEST_NULL;
            mExistingAccounts = null;
            // If the selected account as specified in the intent matches one in the list we will
            // show is as pre-selected.
            android.accounts.Account selectedAccount = ((android.accounts.Account) (intent.getParcelableExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_SELECTED_ACCOUNT)));
            if (selectedAccount != null) {
                mSelectedAccountName = selectedAccount.name;
            }
        }
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, "selected account name is " + mSelectedAccountName);
        }
        mSetOfAllowableAccounts = getAllowableAccountSet(intent);
        mSetOfRelevantAccountTypes = getReleventAccountTypes(intent);
        mDescriptionOverride = intent.getStringExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_DESCRIPTION_TEXT_OVERRIDE);
        mAccounts = getAcceptableAccountChoices(android.accounts.AccountManager.get(this));
        if (mAccounts.isEmpty() && mDisallowAddAccounts) {
            requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            setContentView(R.layout.app_not_authorized);
            mDontShowPicker = true;
        }
        if (mDontShowPicker) {
            super.onCreate(savedInstanceState);
            return;
        }
        // In cases where the activity does not need to show an account picker, cut the chase
        // and return the result directly. Eg:
        // Single account -> select it directly
        // No account -> launch add account activity directly
        if (mPendingRequest == android.accounts.ChooseTypeAndAccountActivity.REQUEST_NULL) {
            // If there are no relevant accounts and only one relevant account type go directly to
            // add account. Otherwise let the user choose.
            if (mAccounts.isEmpty()) {
                setNonLabelThemeAndCallSuperCreate(savedInstanceState);
                if (mSetOfRelevantAccountTypes.size() == 1) {
                    runAddAccountForAuthenticator(mSetOfRelevantAccountTypes.iterator().next());
                } else {
                    startChooseAccountTypeActivity();
                }
            }
        }
        java.lang.String[] listItems = getListOfDisplayableOptions(mAccounts);
        mSelectedItemIndex = getItemIndexToSelect(mAccounts, mSelectedAccountName, mSelectedAddNewAccount);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_type_and_account);
        overrideDescriptionIfSupplied(mDescriptionOverride);
        populateUIAccountList(listItems);
        // Only enable "OK" button if something has been selected.
        mOkButton = ((android.widget.Button) (findViewById(android.R.id.button2)));
        mOkButton.setEnabled(mSelectedItemIndex != android.accounts.ChooseTypeAndAccountActivity.SELECTED_ITEM_NONE);
    }

    @java.lang.Override
    protected void onDestroy() {
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, "ChooseTypeAndAccountActivity.onDestroy()");
        }
        super.onDestroy();
    }

    @java.lang.Override
    protected void onSaveInstanceState(final android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_PENDING_REQUEST, mPendingRequest);
        if (mPendingRequest == android.accounts.ChooseTypeAndAccountActivity.REQUEST_ADD_ACCOUNT) {
            outState.putParcelableArray(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_EXISTING_ACCOUNTS, mExistingAccounts);
        }
        if (mSelectedItemIndex != android.accounts.ChooseTypeAndAccountActivity.SELECTED_ITEM_NONE) {
            if (mSelectedItemIndex == mAccounts.size()) {
                outState.putBoolean(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, true);
            } else {
                outState.putBoolean(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
                outState.putString(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME, mAccounts.get(mSelectedItemIndex).name);
            }
        }
        outState.putParcelableArrayList(android.accounts.ChooseTypeAndAccountActivity.KEY_INSTANCE_STATE_ACCOUNT_LIST, mAccounts);
    }

    public void onCancelButtonClicked(android.view.View view) {
        onBackPressed();
    }

    public void onOkButtonClicked(android.view.View view) {
        if (mSelectedItemIndex == mAccounts.size()) {
            // Selected "Add New Account" option
            startChooseAccountTypeActivity();
        } else
            if (mSelectedItemIndex != android.accounts.ChooseTypeAndAccountActivity.SELECTED_ITEM_NONE) {
                onAccountSelected(mAccounts.get(mSelectedItemIndex));
            }

    }

    // Called when the choose account type activity (for adding an account) returns.
    // If it was a success read the account and set it in the result. In all cases
    // return the result and finish this activity.
    @java.lang.Override
    protected void onActivityResult(final int requestCode, final int resultCode, final android.content.Intent data) {
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            if ((data != null) && (data.getExtras() != null))
                data.getExtras().keySet();

            android.os.Bundle extras = (data != null) ? data.getExtras() : null;
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, ((((("ChooseTypeAndAccountActivity.onActivityResult(reqCode=" + requestCode) + ", resCode=") + resultCode) + ", extras=") + extras) + ")");
        }
        // we got our result, so clear the fact that we had a pending request
        mPendingRequest = android.accounts.ChooseTypeAndAccountActivity.REQUEST_NULL;
        if (resultCode == android.app.Activity.RESULT_CANCELED) {
            // if canceling out of addAccount and the original state caused us to skip this,
            // finish this activity
            if (mAccounts.isEmpty()) {
                setResult(android.app.Activity.RESULT_CANCELED);
                finish();
            }
            return;
        }
        if (resultCode == android.app.Activity.RESULT_OK) {
            if (requestCode == android.accounts.ChooseTypeAndAccountActivity.REQUEST_CHOOSE_TYPE) {
                if (data != null) {
                    java.lang.String accountType = data.getStringExtra(android.accounts.AccountManager.KEY_ACCOUNT_TYPE);
                    if (accountType != null) {
                        runAddAccountForAuthenticator(accountType);
                        return;
                    }
                }
                android.util.Log.d(android.accounts.ChooseTypeAndAccountActivity.TAG, "ChooseTypeAndAccountActivity.onActivityResult: unable to find account " + "type, pretending the request was canceled");
            } else
                if (requestCode == android.accounts.ChooseTypeAndAccountActivity.REQUEST_ADD_ACCOUNT) {
                    java.lang.String accountName = null;
                    java.lang.String accountType = null;
                    if (data != null) {
                        accountName = data.getStringExtra(android.accounts.AccountManager.KEY_ACCOUNT_NAME);
                        accountType = data.getStringExtra(android.accounts.AccountManager.KEY_ACCOUNT_TYPE);
                    }
                    if ((accountName == null) || (accountType == null)) {
                        android.accounts.Account[] currentAccounts = android.accounts.AccountManager.get(this).getAccountsForPackage(mCallingPackage, mCallingUid);
                        java.util.Set<android.accounts.Account> preExistingAccounts = new java.util.HashSet<android.accounts.Account>();
                        for (android.os.Parcelable accountParcel : mExistingAccounts) {
                            preExistingAccounts.add(((android.accounts.Account) (accountParcel)));
                        }
                        for (android.accounts.Account account : currentAccounts) {
                            if (!preExistingAccounts.contains(account)) {
                                accountName = account.name;
                                accountType = account.type;
                                break;
                            }
                        }
                    }
                    if ((accountName != null) || (accountType != null)) {
                        setResultAndFinish(accountName, accountType);
                        return;
                    }
                }

            android.util.Log.d(android.accounts.ChooseTypeAndAccountActivity.TAG, "ChooseTypeAndAccountActivity.onActivityResult: unable to find added " + "account, pretending the request was canceled");
        }
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, "ChooseTypeAndAccountActivity.onActivityResult: canceled");
        }
        setResult(android.app.Activity.RESULT_CANCELED);
        finish();
    }

    protected void runAddAccountForAuthenticator(java.lang.String type) {
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, "runAddAccountForAuthenticator: " + type);
        }
        final android.os.Bundle options = getIntent().getBundleExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE);
        final java.lang.String[] requiredFeatures = getIntent().getStringArrayExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY);
        final java.lang.String authTokenType = getIntent().getStringExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING);
        /* activity */
        /* callback */
        /* Handler */
        android.accounts.AccountManager.get(this).addAccount(type, authTokenType, requiredFeatures, options, null, this, null);
    }

    @java.lang.Override
    public void run(final android.accounts.AccountManagerFuture<android.os.Bundle> accountManagerFuture) {
        try {
            final android.os.Bundle accountManagerResult = accountManagerFuture.getResult();
            final android.content.Intent intent = ((android.content.Intent) (accountManagerResult.getParcelable(android.accounts.AccountManager.KEY_INTENT)));
            if (intent != null) {
                mPendingRequest = android.accounts.ChooseTypeAndAccountActivity.REQUEST_ADD_ACCOUNT;
                mExistingAccounts = android.accounts.AccountManager.get(this).getAccountsForPackage(mCallingPackage, mCallingUid);
                intent.setFlags(intent.getFlags() & (~android.content.Intent.FLAG_ACTIVITY_NEW_TASK));
                startActivityForResult(intent, android.accounts.ChooseTypeAndAccountActivity.REQUEST_ADD_ACCOUNT);
                return;
            }
        } catch (android.accounts.OperationCanceledException e) {
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
            return;
        } catch (java.io.IOException e) {
        } catch (android.accounts.AuthenticatorException e) {
        }
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(android.accounts.AccountManager.KEY_ERROR_MESSAGE, "error communicating with server");
        setResult(android.app.Activity.RESULT_OK, new android.content.Intent().putExtras(bundle));
        finish();
    }

    /**
     * The default activity theme shows label at the top. Set a theme which does
     * not show label, which effectively makes the activity invisible. Note that
     * no content is being set. If something gets set, using this theme may be
     * useless.
     */
    private void setNonLabelThemeAndCallSuperCreate(android.os.Bundle savedInstanceState) {
        setTheme(R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        super.onCreate(savedInstanceState);
    }

    private void onAccountSelected(android.accounts.Account account) {
        android.util.Log.d(android.accounts.ChooseTypeAndAccountActivity.TAG, "selected account " + account);
        setResultAndFinish(account.name, account.type);
    }

    private void setResultAndFinish(final java.lang.String accountName, final java.lang.String accountType) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(android.accounts.AccountManager.KEY_ACCOUNT_NAME, accountName);
        bundle.putString(android.accounts.AccountManager.KEY_ACCOUNT_TYPE, accountType);
        setResult(android.app.Activity.RESULT_OK, new android.content.Intent().putExtras(bundle));
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, ((("ChooseTypeAndAccountActivity.setResultAndFinish: " + "selected account ") + accountName) + ", ") + accountType);
        }
        finish();
    }

    private void startChooseAccountTypeActivity() {
        if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, "ChooseAccountTypeActivity.startChooseAccountTypeActivity()");
        }
        final android.content.Intent intent = new android.content.Intent(this, android.accounts.ChooseAccountTypeActivity.class);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY, getIntent().getStringArrayExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY));
        intent.putExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE, getIntent().getBundleExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE));
        intent.putExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY, getIntent().getStringArrayExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY));
        intent.putExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING, getIntent().getStringExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING));
        startActivityForResult(intent, android.accounts.ChooseTypeAndAccountActivity.REQUEST_CHOOSE_TYPE);
        mPendingRequest = android.accounts.ChooseTypeAndAccountActivity.REQUEST_CHOOSE_TYPE;
    }

    /**
     *
     *
     * @return a value between 0 (inclusive) and accounts.size() (inclusive) or SELECTED_ITEM_NONE.
    An index value of accounts.size() indicates 'Add account' option.
     */
    private int getItemIndexToSelect(java.util.ArrayList<android.accounts.Account> accounts, java.lang.String selectedAccountName, boolean selectedAddNewAccount) {
        // If "Add account" option was previously selected by user, preserve it across
        // orientation changes.
        if (selectedAddNewAccount) {
            return accounts.size();
        }
        // search for the selected account name if present
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).name.equals(selectedAccountName)) {
                return i;
            }
        }
        // no account selected.
        return android.accounts.ChooseTypeAndAccountActivity.SELECTED_ITEM_NONE;
    }

    private java.lang.String[] getListOfDisplayableOptions(java.util.ArrayList<android.accounts.Account> accounts) {
        // List of options includes all accounts found together with "Add new account" as the
        // last item in the list.
        java.lang.String[] listItems = new java.lang.String[accounts.size() + (mDisallowAddAccounts ? 0 : 1)];
        for (int i = 0; i < accounts.size(); i++) {
            listItems[i] = accounts.get(i).name;
        }
        if (!mDisallowAddAccounts) {
            listItems[accounts.size()] = getResources().getString(R.string.add_account_button_label);
        }
        return listItems;
    }

    /**
     * Create a list of Account objects for each account that is acceptable. Filter out
     * accounts that don't match the allowable types, if provided, or that don't match the
     * allowable accounts, if provided.
     */
    private java.util.ArrayList<android.accounts.Account> getAcceptableAccountChoices(android.accounts.AccountManager accountManager) {
        final android.accounts.Account[] accounts = accountManager.getAccountsForPackage(mCallingPackage, mCallingUid);
        java.util.ArrayList<android.accounts.Account> accountsToPopulate = new java.util.ArrayList<android.accounts.Account>(accounts.length);
        for (android.accounts.Account account : accounts) {
            if ((mSetOfAllowableAccounts != null) && (!mSetOfAllowableAccounts.contains(account))) {
                continue;
            }
            if ((mSetOfRelevantAccountTypes != null) && (!mSetOfRelevantAccountTypes.contains(account.type))) {
                continue;
            }
            accountsToPopulate.add(account);
        }
        return accountsToPopulate;
    }

    /**
     * Return a set of account types specified by the intent as well as supported by the
     * AccountManager.
     */
    private java.util.Set<java.lang.String> getReleventAccountTypes(final android.content.Intent intent) {
        // An account type is relevant iff it is allowed by the caller and supported by the account
        // manager.
        java.util.Set<java.lang.String> setOfRelevantAccountTypes = null;
        final java.lang.String[] allowedAccountTypes = intent.getStringArrayExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY);
        android.accounts.AuthenticatorDescription[] descs = android.accounts.AccountManager.get(this).getAuthenticatorTypes();
        java.util.Set<java.lang.String> supportedAccountTypes = new java.util.HashSet<java.lang.String>(descs.length);
        for (android.accounts.AuthenticatorDescription desc : descs) {
            supportedAccountTypes.add(desc.type);
        }
        if (allowedAccountTypes != null) {
            setOfRelevantAccountTypes = com.google.android.collect.Sets.newHashSet(allowedAccountTypes);
            setOfRelevantAccountTypes.retainAll(supportedAccountTypes);
        } else {
            setOfRelevantAccountTypes = supportedAccountTypes;
        }
        return setOfRelevantAccountTypes;
    }

    /**
     * Returns a set of whitelisted accounts given by the intent or null if none specified by the
     * intent.
     */
    private java.util.Set<android.accounts.Account> getAllowableAccountSet(final android.content.Intent intent) {
        java.util.Set<android.accounts.Account> setOfAllowableAccounts = null;
        final java.util.ArrayList<android.os.Parcelable> validAccounts = intent.getParcelableArrayListExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST);
        if (validAccounts != null) {
            setOfAllowableAccounts = new java.util.HashSet<android.accounts.Account>(validAccounts.size());
            for (android.os.Parcelable parcelable : validAccounts) {
                setOfAllowableAccounts.add(((android.accounts.Account) (parcelable)));
            }
        }
        return setOfAllowableAccounts;
    }

    /**
     * Overrides the description text view for the picker activity if specified by the intent.
     * If not specified then makes the description invisible.
     */
    private void overrideDescriptionIfSupplied(java.lang.String descriptionOverride) {
        android.widget.TextView descriptionView = ((android.widget.TextView) (findViewById(R.id.description)));
        if (!android.text.TextUtils.isEmpty(descriptionOverride)) {
            descriptionView.setText(descriptionOverride);
        } else {
            descriptionView.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Populates the UI ListView with the given list of items and selects an item
     * based on {@code mSelectedItemIndex} member variable.
     */
    private final void populateUIAccountList(java.lang.String[] listItems) {
        android.widget.ListView list = ((android.widget.ListView) (findViewById(android.R.id.list)));
        list.setAdapter(new android.widget.ArrayAdapter<java.lang.String>(this, android.R.layout.simple_list_item_single_choice, listItems));
        list.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @java.lang.Override
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                mSelectedItemIndex = position;
                mOkButton.setEnabled(true);
            }
        });
        if (mSelectedItemIndex != android.accounts.ChooseTypeAndAccountActivity.SELECTED_ITEM_NONE) {
            list.setItemChecked(mSelectedItemIndex, true);
            if (android.util.Log.isLoggable(android.accounts.ChooseTypeAndAccountActivity.TAG, android.util.Log.VERBOSE)) {
                android.util.Log.v(android.accounts.ChooseTypeAndAccountActivity.TAG, ("List item " + mSelectedItemIndex) + " should be selected");
            }
        }
    }
}

