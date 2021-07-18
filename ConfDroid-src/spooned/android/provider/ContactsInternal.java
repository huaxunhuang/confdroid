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
package android.provider;


/**
 * Contacts related internal methods.
 *
 * @unknown 
 */
public class ContactsInternal {
    private ContactsInternal() {
    }

    /**
     * URI matcher used to parse contact URIs.
     */
    private static final android.content.UriMatcher sContactsUriMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);

    private static final int CONTACTS_URI_LOOKUP_ID = 1000;

    private static final int CONTACTS_URI_LOOKUP = 1001;

    static {
        // Contacts URI matching table
        final android.content.UriMatcher matcher = android.provider.ContactsInternal.sContactsUriMatcher;
        matcher.addURI(android.provider.ContactsContract.AUTHORITY, "contacts/lookup/*", android.provider.ContactsInternal.CONTACTS_URI_LOOKUP);
        matcher.addURI(android.provider.ContactsContract.AUTHORITY, "contacts/lookup/*/#", android.provider.ContactsInternal.CONTACTS_URI_LOOKUP_ID);
    }

    /**
     * Called by {@link ContactsContract} to star Quick Contact, possibly on the managed profile.
     */
    public static void startQuickContactWithErrorToast(android.content.Context context, android.content.Intent intent) {
        final android.net.Uri uri = intent.getData();
        final int match = android.provider.ContactsInternal.sContactsUriMatcher.match(uri);
        switch (match) {
            case android.provider.ContactsInternal.CONTACTS_URI_LOOKUP :
            case android.provider.ContactsInternal.CONTACTS_URI_LOOKUP_ID :
                {
                    if (android.provider.ContactsInternal.maybeStartManagedQuickContact(context, intent)) {
                        return;// Request handled by DPM.  Just return here.

                    }
                    break;
                }
        }
        // Launch on the current profile.
        android.provider.ContactsInternal.startQuickContactWithErrorToastForUser(context, intent, android.os.Process.myUserHandle());
    }

    public static void startQuickContactWithErrorToastForUser(android.content.Context context, android.content.Intent intent, android.os.UserHandle user) {
        try {
            context.startActivityAsUser(intent, user);
        } catch (android.content.ActivityNotFoundException e) {
            android.widget.Toast.makeText(context, com.android.internal.R.string.quick_contacts_not_available, android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * If the URI in {@code intent} is of a corp contact, launch quick contact on the managed
     * profile.
     *
     * @return the URI in {@code intent} is of a corp contact thus launched on the managed profile.
     */
    private static boolean maybeStartManagedQuickContact(android.content.Context context, android.content.Intent originalIntent) {
        final android.net.Uri uri = originalIntent.getData();
        // Decompose into an ID and a lookup key.
        final java.util.List<java.lang.String> pathSegments = uri.getPathSegments();
        final boolean isContactIdIgnored = pathSegments.size() < 4;
        final long contactId = (isContactIdIgnored) ? android.provider.ContactsContract.Contacts.ENTERPRISE_CONTACT_ID_BASE// contact id will be ignored
         : android.content.ContentUris.parseId(uri);
        final java.lang.String lookupKey = pathSegments.get(2);
        final java.lang.String directoryIdStr = uri.getQueryParameter(android.provider.ContactsContract.DIRECTORY_PARAM_KEY);
        final long directoryId = (directoryIdStr == null) ? android.provider.ContactsContract.Directory.ENTERPRISE_DIRECTORY_ID_BASE : java.lang.Long.parseLong(directoryIdStr);
        // See if it has a corp lookupkey.
        if (android.text.TextUtils.isEmpty(lookupKey) || (!lookupKey.startsWith(android.provider.ContactsContract.Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX))) {
            return false;// It's not a corp lookup key.

        }
        if (!android.provider.ContactsContract.Contacts.isEnterpriseContactId(contactId)) {
            throw new java.lang.IllegalArgumentException("Invalid enterprise contact id: " + contactId);
        }
        if (!android.provider.ContactsContract.Directory.isEnterpriseDirectoryId(directoryId)) {
            throw new java.lang.IllegalArgumentException("Invalid enterprise directory id: " + directoryId);
        }
        // Launch Quick Contact on the managed profile, if the policy allows.
        final android.app.admin.DevicePolicyManager dpm = context.getSystemService(android.app.admin.DevicePolicyManager.class);
        final java.lang.String actualLookupKey = lookupKey.substring(android.provider.ContactsContract.Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX.length());
        final long actualContactId = contactId - android.provider.ContactsContract.Contacts.ENTERPRISE_CONTACT_ID_BASE;
        final long actualDirectoryId = directoryId - android.provider.ContactsContract.Directory.ENTERPRISE_DIRECTORY_ID_BASE;
        dpm.startManagedQuickContact(actualLookupKey, actualContactId, isContactIdIgnored, actualDirectoryId, originalIntent);
        return true;
    }
}

