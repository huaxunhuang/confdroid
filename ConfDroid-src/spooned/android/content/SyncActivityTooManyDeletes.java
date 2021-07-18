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
package android.content;


/**
 * Presents multiple options for handling the case where a sync was aborted because there
 * were too many pending deletes. One option is to force the delete, another is to rollback
 * the deletes, the third is to do nothing.
 *
 * @unknown 
 */
public class SyncActivityTooManyDeletes extends android.app.Activity implements android.widget.AdapterView.OnItemClickListener {
    private long mNumDeletes;

    private android.accounts.Account mAccount;

    private java.lang.String mAuthority;

    private java.lang.String mProvider;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.os.Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        mNumDeletes = extras.getLong("numDeletes");
        mAccount = ((android.accounts.Account) (extras.getParcelable("account")));
        mAuthority = extras.getString("authority");
        mProvider = extras.getString("provider");
        // the order of these must match up with the constants for position used in onItemClick
        java.lang.CharSequence[] options = new java.lang.CharSequence[]{ getResources().getText(R.string.sync_really_delete), getResources().getText(R.string.sync_undo_deletes), getResources().getText(R.string.sync_do_nothing) };
        android.widget.ListAdapter adapter = new android.widget.ArrayAdapter<java.lang.CharSequence>(this, android.R.layout.simple_list_item_1, android.R.id.text1, options);
        android.widget.ListView listView = new android.widget.ListView(this);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(this);
        android.widget.TextView textView = new android.widget.TextView(this);
        java.lang.CharSequence tooManyDeletesDescFormat = getResources().getText(R.string.sync_too_many_deletes_desc);
        textView.setText(java.lang.String.format(tooManyDeletesDescFormat.toString(), mNumDeletes, mProvider, mAccount.name));
        final android.widget.LinearLayout ll = new android.widget.LinearLayout(this);
        ll.setOrientation(android.widget.LinearLayout.VERTICAL);
        final android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        ll.addView(textView, lp);
        ll.addView(listView, lp);
        // TODO: consider displaying the icon of the account type
        // AuthenticatorDescription[] descs = AccountManager.get(this).getAuthenticatorTypes();
        // for (AuthenticatorDescription desc : descs) {
        // if (desc.type.equals(mAccount.type)) {
        // try {
        // final Context authContext = createPackageContext(desc.packageName, 0);
        // ImageView imageView = new ImageView(this);
        // imageView.setImageDrawable(authContext.getDrawable(desc.iconId));
        // ll.addView(imageView, lp);
        // } catch (PackageManager.NameNotFoundException e) {
        // }
        // break;
        // }
        // }
        setContentView(ll);
    }

    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        // the constants for position correspond to the items options array in onCreate()
        if (position == 0)
            startSyncReallyDelete();
        else
            if (position == 1)
                startSyncUndoDeletes();


        finish();
    }

    private void startSyncReallyDelete() {
        android.os.Bundle extras = new android.os.Bundle();
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS, true);
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        android.content.ContentResolver.requestSync(mAccount, mAuthority, extras);
    }

    private void startSyncUndoDeletes() {
        android.os.Bundle extras = new android.os.Bundle();
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS, true);
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        extras.putBoolean(android.content.ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        android.content.ContentResolver.requestSync(mAccount, mAuthority, extras);
    }
}

