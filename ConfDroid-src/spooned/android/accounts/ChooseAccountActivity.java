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
public class ChooseAccountActivity extends android.app.Activity {
    private static final java.lang.String TAG = "AccountManager";

    private android.os.Parcelable[] mAccounts = null;

    private android.accounts.AccountManagerResponse mAccountManagerResponse = null;

    private android.os.Bundle mResult;

    private java.util.HashMap<java.lang.String, android.accounts.AuthenticatorDescription> mTypeToAuthDescription = new java.util.HashMap<java.lang.String, android.accounts.AuthenticatorDescription>();

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccounts = getIntent().getParcelableArrayExtra(android.accounts.AccountManager.KEY_ACCOUNTS);
        mAccountManagerResponse = getIntent().getParcelableExtra(android.accounts.AccountManager.KEY_ACCOUNT_MANAGER_RESPONSE);
        // KEY_ACCOUNTS is a required parameter
        if (mAccounts == null) {
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
            return;
        }
        getAuthDescriptions();
        android.accounts.ChooseAccountActivity.AccountInfo[] mAccountInfos = new android.accounts.ChooseAccountActivity.AccountInfo[mAccounts.length];
        for (int i = 0; i < mAccounts.length; i++) {
            mAccountInfos[i] = new android.accounts.ChooseAccountActivity.AccountInfo(((android.accounts.Account) (mAccounts[i])).name, getDrawableForType(((android.accounts.Account) (mAccounts[i])).type));
        }
        setContentView(R.layout.choose_account);
        // Setup the list
        android.widget.ListView list = ((android.widget.ListView) (findViewById(android.R.id.list)));
        // Use an existing ListAdapter that will map an array of strings to TextViews
        list.setAdapter(new android.accounts.ChooseAccountActivity.AccountArrayAdapter(this, android.R.layout.simple_list_item_1, mAccountInfos));
        list.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
        list.setTextFilterEnabled(true);
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                onListItemClick(((android.widget.ListView) (parent)), v, position, id);
            }
        });
    }

    private void getAuthDescriptions() {
        for (android.accounts.AuthenticatorDescription desc : android.accounts.AccountManager.get(this).getAuthenticatorTypes()) {
            mTypeToAuthDescription.put(desc.type, desc);
        }
    }

    private android.graphics.drawable.Drawable getDrawableForType(java.lang.String accountType) {
        android.graphics.drawable.Drawable icon = null;
        if (mTypeToAuthDescription.containsKey(accountType)) {
            try {
                android.accounts.AuthenticatorDescription desc = mTypeToAuthDescription.get(accountType);
                android.content.Context authContext = createPackageContext(desc.packageName, 0);
                icon = authContext.getDrawable(desc.iconId);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                // Nothing we can do much here, just log
                if (android.util.Log.isLoggable(android.accounts.ChooseAccountActivity.TAG, android.util.Log.WARN)) {
                    android.util.Log.w(android.accounts.ChooseAccountActivity.TAG, "No icon name for account type " + accountType);
                }
            } catch (android.content.res.Resources.NotFoundException e) {
                // Nothing we can do much here, just log
                if (android.util.Log.isLoggable(android.accounts.ChooseAccountActivity.TAG, android.util.Log.WARN)) {
                    android.util.Log.w(android.accounts.ChooseAccountActivity.TAG, "No icon resource for account type " + accountType);
                }
            }
        }
        return icon;
    }

    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        android.accounts.Account account = ((android.accounts.Account) (mAccounts[position]));
        android.util.Log.d(android.accounts.ChooseAccountActivity.TAG, "selected account " + account);
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(android.accounts.AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(android.accounts.AccountManager.KEY_ACCOUNT_TYPE, account.type);
        mResult = bundle;
        finish();
    }

    public void finish() {
        if (mAccountManagerResponse != null) {
            if (mResult != null) {
                mAccountManagerResponse.onResult(mResult);
            } else {
                mAccountManagerResponse.onError(android.accounts.AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
        }
        super.finish();
    }

    private static class AccountInfo {
        final java.lang.String name;

        final android.graphics.drawable.Drawable drawable;

        AccountInfo(java.lang.String name, android.graphics.drawable.Drawable drawable) {
            this.name = name;
            this.drawable = drawable;
        }
    }

    private static class ViewHolder {
        android.widget.ImageView icon;

        android.widget.TextView text;
    }

    private static class AccountArrayAdapter extends android.widget.ArrayAdapter<android.accounts.ChooseAccountActivity.AccountInfo> {
        private android.view.LayoutInflater mLayoutInflater;

        private android.accounts.ChooseAccountActivity.AccountInfo[] mInfos;

        public AccountArrayAdapter(android.content.Context context, int textViewResourceId, android.accounts.ChooseAccountActivity.AccountInfo[] infos) {
            super(context, textViewResourceId, infos);
            mInfos = infos;
            mLayoutInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.accounts.ChooseAccountActivity.ViewHolder holder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.choose_account_row, null);
                holder = new android.accounts.ChooseAccountActivity.ViewHolder();
                holder.text = ((android.widget.TextView) (convertView.findViewById(R.id.account_row_text)));
                holder.icon = ((android.widget.ImageView) (convertView.findViewById(R.id.account_row_icon)));
                convertView.setTag(holder);
            } else {
                holder = ((android.accounts.ChooseAccountActivity.ViewHolder) (convertView.getTag()));
            }
            holder.text.setText(mInfos[position].name);
            holder.icon.setImageDrawable(mInfos[position].drawable);
            return convertView;
        }
    }
}

