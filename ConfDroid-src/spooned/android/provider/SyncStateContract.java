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
 * limitations under the License
 */
package android.provider;


/**
 * The ContentProvider contract for associating data with ana data array account.
 * This may be used by providers that want to store this data in a standard way.
 */
public class SyncStateContract {
    public interface Columns extends android.provider.BaseColumns {
        /**
         * A reference to the name of the account to which this data belongs
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ACCOUNT_NAME = "account_name";

        /**
         * A reference to the type of the account to which this data belongs
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ACCOUNT_TYPE = "account_type";

        /**
         * The sync data associated with this account.
         * <P>Type: NONE</P>
         */
        public static final java.lang.String DATA = "data";
    }

    public static class Constants implements android.provider.SyncStateContract.Columns {
        public static final java.lang.String CONTENT_DIRECTORY = "syncstate";
    }

    public static final class Helpers {
        private static final java.lang.String[] DATA_PROJECTION = new java.lang.String[]{ android.provider.SyncStateContract.Columns.DATA, android.provider.SyncStateContract.Columns._ID };

        private static final java.lang.String SELECT_BY_ACCOUNT = ((android.provider.SyncStateContract.Columns.ACCOUNT_NAME + "=? AND ") + android.provider.SyncStateContract.Columns.ACCOUNT_TYPE) + "=?";

        /**
         * Get the sync state that is associated with the account or null.
         *
         * @param provider
         * 		the {@link ContentProviderClient} that is to be used to communicate
         * 		with the {@link android.content.ContentProvider} that contains the sync state.
         * @param uri
         * 		the uri of the sync state
         * @param account
         * 		the {@link Account} whose sync state should be returned
         * @return the sync state or null if there is no sync state associated with the account
         * @throws RemoteException
         * 		if there is a failure communicating with the remote
         * 		{@link android.content.ContentProvider}
         */
        public static byte[] get(android.content.ContentProviderClient provider, android.net.Uri uri, android.accounts.Account account) throws android.os.RemoteException {
            android.database.Cursor c = provider.query(uri, android.provider.SyncStateContract.Helpers.DATA_PROJECTION, android.provider.SyncStateContract.Helpers.SELECT_BY_ACCOUNT, new java.lang.String[]{ account.name, account.type }, null);
            // Unable to query the provider
            if (c == null) {
                throw new android.os.RemoteException();
            }
            try {
                if (c.moveToNext()) {
                    return c.getBlob(c.getColumnIndexOrThrow(android.provider.SyncStateContract.Columns.DATA));
                }
            } finally {
                c.close();
            }
            return null;
        }

        /**
         * Assigns the data array as the sync state for the given account.
         *
         * @param provider
         * 		the {@link ContentProviderClient} that is to be used to communicate
         * 		with the {@link android.content.ContentProvider} that contains the sync state.
         * @param uri
         * 		the uri of the sync state
         * @param account
         * 		the {@link Account} whose sync state should be set
         * @param data
         * 		the byte[] that contains the sync state
         * @throws RemoteException
         * 		if there is a failure communicating with the remote
         * 		{@link android.content.ContentProvider}
         */
        public static void set(android.content.ContentProviderClient provider, android.net.Uri uri, android.accounts.Account account, byte[] data) throws android.os.RemoteException {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.SyncStateContract.Columns.DATA, data);
            values.put(android.provider.SyncStateContract.Columns.ACCOUNT_NAME, account.name);
            values.put(android.provider.SyncStateContract.Columns.ACCOUNT_TYPE, account.type);
            provider.insert(uri, values);
        }

        public static android.net.Uri insert(android.content.ContentProviderClient provider, android.net.Uri uri, android.accounts.Account account, byte[] data) throws android.os.RemoteException {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.SyncStateContract.Columns.DATA, data);
            values.put(android.provider.SyncStateContract.Columns.ACCOUNT_NAME, account.name);
            values.put(android.provider.SyncStateContract.Columns.ACCOUNT_TYPE, account.type);
            return provider.insert(uri, values);
        }

        public static void update(android.content.ContentProviderClient provider, android.net.Uri uri, byte[] data) throws android.os.RemoteException {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.SyncStateContract.Columns.DATA, data);
            provider.update(uri, values, null, null);
        }

        public static android.util.Pair<android.net.Uri, byte[]> getWithUri(android.content.ContentProviderClient provider, android.net.Uri uri, android.accounts.Account account) throws android.os.RemoteException {
            android.database.Cursor c = provider.query(uri, android.provider.SyncStateContract.Helpers.DATA_PROJECTION, android.provider.SyncStateContract.Helpers.SELECT_BY_ACCOUNT, new java.lang.String[]{ account.name, account.type }, null);
            if (c == null) {
                throw new android.os.RemoteException();
            }
            try {
                if (c.moveToNext()) {
                    long rowId = c.getLong(1);
                    byte[] blob = c.getBlob(c.getColumnIndexOrThrow(android.provider.SyncStateContract.Columns.DATA));
                    return android.util.Pair.create(android.content.ContentUris.withAppendedId(uri, rowId), blob);
                }
            } finally {
                c.close();
            }
            return null;
        }

        /**
         * Creates and returns a ContentProviderOperation that assigns the data array as the
         * sync state for the given account.
         *
         * @param uri
         * 		the uri of the sync state
         * @param account
         * 		the {@link Account} whose sync state should be set
         * @param data
         * 		the byte[] that contains the sync state
         * @return the new ContentProviderOperation that assigns the data array as the
        account's sync state
         */
        public static android.content.ContentProviderOperation newSetOperation(android.net.Uri uri, android.accounts.Account account, byte[] data) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.SyncStateContract.Columns.DATA, data);
            return android.content.ContentProviderOperation.newInsert(uri).withValue(android.provider.SyncStateContract.Columns.ACCOUNT_NAME, account.name).withValue(android.provider.SyncStateContract.Columns.ACCOUNT_TYPE, account.type).withValues(values).build();
        }

        /**
         * Creates and returns a ContentProviderOperation that assigns the data array as the
         * sync state for the given account.
         *
         * @param uri
         * 		the uri of the specific sync state to set
         * @param data
         * 		the byte[] that contains the sync state
         * @return the new ContentProviderOperation that assigns the data array as the
        account's sync state
         */
        public static android.content.ContentProviderOperation newUpdateOperation(android.net.Uri uri, byte[] data) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.SyncStateContract.Columns.DATA, data);
            return android.content.ContentProviderOperation.newUpdate(uri).withValues(values).build();
        }
    }
}

