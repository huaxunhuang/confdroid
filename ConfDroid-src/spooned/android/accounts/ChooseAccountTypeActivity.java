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
public class ChooseAccountTypeActivity extends android.app.Activity {
    private static final java.lang.String TAG = "AccountChooser";

    private java.util.HashMap<java.lang.String, android.accounts.ChooseAccountTypeActivity.AuthInfo> mTypeToAuthenticatorInfo = new java.util.HashMap<java.lang.String, android.accounts.ChooseAccountTypeActivity.AuthInfo>();

    private java.util.ArrayList<android.accounts.ChooseAccountTypeActivity.AuthInfo> mAuthenticatorInfosToDisplay;

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.util.Log.isLoggable(android.accounts.ChooseAccountTypeActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseAccountTypeActivity.TAG, ("ChooseAccountTypeActivity.onCreate(savedInstanceState=" + savedInstanceState) + ")");
        }
        // Read the validAccountTypes, if present, and add them to the setOfAllowableAccountTypes
        java.util.Set<java.lang.String> setOfAllowableAccountTypes = null;
        java.lang.String[] validAccountTypes = getIntent().getStringArrayExtra(android.accounts.ChooseTypeAndAccountActivity.EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY);
        if (validAccountTypes != null) {
            setOfAllowableAccountTypes = new java.util.HashSet<java.lang.String>(validAccountTypes.length);
            for (java.lang.String type : validAccountTypes) {
                setOfAllowableAccountTypes.add(type);
            }
        }
        // create a map of account authenticators
        buildTypeToAuthDescriptionMap();
        // Create a list of authenticators that are allowable. Filter out those that
        // don't match the allowable account types, if provided.
        mAuthenticatorInfosToDisplay = new java.util.ArrayList<android.accounts.ChooseAccountTypeActivity.AuthInfo>(mTypeToAuthenticatorInfo.size());
        for (java.util.Map.Entry<java.lang.String, android.accounts.ChooseAccountTypeActivity.AuthInfo> entry : mTypeToAuthenticatorInfo.entrySet()) {
            final java.lang.String type = entry.getKey();
            final android.accounts.ChooseAccountTypeActivity.AuthInfo info = entry.getValue();
            if ((setOfAllowableAccountTypes != null) && (!setOfAllowableAccountTypes.contains(type))) {
                continue;
            }
            mAuthenticatorInfosToDisplay.add(info);
        }
        if (mAuthenticatorInfosToDisplay.isEmpty()) {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString(android.accounts.AccountManager.KEY_ERROR_MESSAGE, "no allowable account types");
            setResult(android.app.Activity.RESULT_OK, new android.content.Intent().putExtras(bundle));
            finish();
            return;
        }
        if (mAuthenticatorInfosToDisplay.size() == 1) {
            setResultAndFinish(mAuthenticatorInfosToDisplay.get(0).desc.type);
            return;
        }
        setContentView(R.layout.choose_account_type);
        // Setup the list
        android.widget.ListView list = ((android.widget.ListView) (findViewById(android.R.id.list)));
        // Use an existing ListAdapter that will map an array of strings to TextViews
        list.setAdapter(new android.accounts.ChooseAccountTypeActivity.AccountArrayAdapter(this, android.R.layout.simple_list_item_1, mAuthenticatorInfosToDisplay));
        list.setChoiceMode(android.widget.ListView.CHOICE_MODE_NONE);
        list.setTextFilterEnabled(false);
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                setResultAndFinish(mAuthenticatorInfosToDisplay.get(position).desc.type);
            }
        });
    }

    private void setResultAndFinish(final java.lang.String type) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(android.accounts.AccountManager.KEY_ACCOUNT_TYPE, type);
        setResult(android.app.Activity.RESULT_OK, new android.content.Intent().putExtras(bundle));
        if (android.util.Log.isLoggable(android.accounts.ChooseAccountTypeActivity.TAG, android.util.Log.VERBOSE)) {
            android.util.Log.v(android.accounts.ChooseAccountTypeActivity.TAG, ("ChooseAccountTypeActivity.setResultAndFinish: " + "selected account type ") + type);
        }
        finish();
    }

    private void buildTypeToAuthDescriptionMap() {
        for (android.accounts.AuthenticatorDescription desc : android.accounts.AccountManager.get(this).getAuthenticatorTypes()) {
            java.lang.String name = null;
            android.graphics.drawable.Drawable icon = null;
            try {
                android.content.Context authContext = createPackageContext(desc.packageName, 0);
                icon = authContext.getDrawable(desc.iconId);
                final java.lang.CharSequence sequence = authContext.getResources().getText(desc.labelId);
                if (sequence != null) {
                    name = sequence.toString();
                }
                name = sequence.toString();
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                // Nothing we can do much here, just log
                if (android.util.Log.isLoggable(android.accounts.ChooseAccountTypeActivity.TAG, android.util.Log.WARN)) {
                    android.util.Log.w(android.accounts.ChooseAccountTypeActivity.TAG, "No icon name for account type " + desc.type);
                }
            } catch (android.content.res.Resources.NotFoundException e) {
                // Nothing we can do much here, just log
                if (android.util.Log.isLoggable(android.accounts.ChooseAccountTypeActivity.TAG, android.util.Log.WARN)) {
                    android.util.Log.w(android.accounts.ChooseAccountTypeActivity.TAG, "No icon resource for account type " + desc.type);
                }
            }
            android.accounts.ChooseAccountTypeActivity.AuthInfo authInfo = new android.accounts.ChooseAccountTypeActivity.AuthInfo(desc, name, icon);
            mTypeToAuthenticatorInfo.put(desc.type, authInfo);
        }
    }

    private static class AuthInfo {
        final android.accounts.AuthenticatorDescription desc;

        final java.lang.String name;

        final android.graphics.drawable.Drawable drawable;

        AuthInfo(android.accounts.AuthenticatorDescription desc, java.lang.String name, android.graphics.drawable.Drawable drawable) {
            this.desc = desc;
            this.name = name;
            this.drawable = drawable;
        }
    }

    private static class ViewHolder {
        android.widget.ImageView icon;

        android.widget.TextView text;
    }

    private static class AccountArrayAdapter extends android.widget.ArrayAdapter<android.accounts.ChooseAccountTypeActivity.AuthInfo> {
        private android.view.LayoutInflater mLayoutInflater;

        private java.util.ArrayList<android.accounts.ChooseAccountTypeActivity.AuthInfo> mInfos;

        public AccountArrayAdapter(android.content.Context context, int textViewResourceId, java.util.ArrayList<android.accounts.ChooseAccountTypeActivity.AuthInfo> infos) {
            super(context, textViewResourceId, infos);
            mInfos = infos;
            mLayoutInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.accounts.ChooseAccountTypeActivity.ViewHolder holder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.choose_account_row, null);
                holder = new android.accounts.ChooseAccountTypeActivity.ViewHolder();
                holder.text = ((android.widget.TextView) (convertView.findViewById(R.id.account_row_text)));
                holder.icon = ((android.widget.ImageView) (convertView.findViewById(R.id.account_row_icon)));
                convertView.setTag(holder);
            } else {
                holder = ((android.accounts.ChooseAccountTypeActivity.ViewHolder) (convertView.getTag()));
            }
            holder.text.setText(mInfos.get(position).name);
            holder.icon.setImageDrawable(mInfos.get(position).drawable);
            return convertView;
        }
    }
}

