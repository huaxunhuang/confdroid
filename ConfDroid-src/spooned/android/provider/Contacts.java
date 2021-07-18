/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.provider;


/**
 * The Contacts provider stores all information about contacts.
 *
 * @deprecated The APIs have been superseded by {@link ContactsContract}. The newer APIs allow
access multiple accounts and support aggregation of similar contacts. These APIs continue to
work but will only return data for the first Google account created, which matches the original
behavior.
 */
@java.lang.Deprecated
public class Contacts {
    private static final java.lang.String TAG = "Contacts";

    /**
     *
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final java.lang.String AUTHORITY = "contacts";

    /**
     * The content:// style URL for this provider
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://" + android.provider.Contacts.AUTHORITY);

    /**
     * Signifies an email address row that is stored in the ContactMethods table
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final int KIND_EMAIL = 1;

    /**
     * Signifies a postal address row that is stored in the ContactMethods table
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final int KIND_POSTAL = 2;

    /**
     * Signifies an IM address row that is stored in the ContactMethods table
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final int KIND_IM = 3;

    /**
     * Signifies an Organization row that is stored in the Organizations table
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final int KIND_ORGANIZATION = 4;

    /**
     * Signifies a Phone row that is stored in the Phones table
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final int KIND_PHONE = 5;

    /**
     * no public constructor since this is a utility class
     */
    private Contacts() {
    }

    /**
     * Columns from the Settings table that other columns join into themselves.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface SettingsColumns {
        /**
         * The _SYNC_ACCOUNT to which this setting corresponds. This may be null.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String _SYNC_ACCOUNT = "_sync_account";

        /**
         * The _SYNC_ACCOUNT_TYPE to which this setting corresponds. This may be null.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String _SYNC_ACCOUNT_TYPE = "_sync_account_type";

        /**
         * The key of this setting.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String KEY = "key";

        /**
         * The value of this setting.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String VALUE = "value";
    }

    /**
     * The settings over all of the people
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Settings implements android.provider.BaseColumns , android.provider.Contacts.SettingsColumns {
        /**
         * no public constructor since this is a utility class
         */
        private Settings() {
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/settings");

        /**
         * The directory twig for this sub-table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_DIRECTORY = "settings";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "key ASC";

        /**
         * A setting that is used to indicate if we should sync down all groups for the
         * specified account. For this setting the _SYNC_ACCOUNT column must be set.
         * If this isn't set then we will only sync the groups whose SHOULD_SYNC column
         * is set to true.
         * <p>
         * This is a boolean setting. It is true if it is set and it is anything other than the
         * emptry string or "0".
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SYNC_EVERYTHING = "syncEverything";

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static java.lang.String getSetting(android.content.ContentResolver cr, java.lang.String account, java.lang.String key) {
            // For now we only support a single account and the UI doesn't know what
            // the account name is, so we're using a global setting for SYNC_EVERYTHING.
            // Some day when we add multiple accounts to the UI this should honor the account
            // that was asked for.
            java.lang.String selectString;
            java.lang.String[] selectArgs;
            if (false) {
                selectString = (account == null) ? "_sync_account is null AND key=?" : "_sync_account=? AND key=?";
                // : "_sync_account=? AND _sync_account_type=? AND key=?";
                selectArgs = (account == null) ? new java.lang.String[]{ key } : new java.lang.String[]{ account, key };
            } else {
                selectString = "key=?";
                selectArgs = new java.lang.String[]{ key };
            }
            android.database.Cursor cursor = cr.query(android.provider.Contacts.Settings.CONTENT_URI, new java.lang.String[]{ android.provider.Contacts.SettingsColumns.VALUE }, selectString, selectArgs, null);
            try {
                if (!cursor.moveToNext())
                    return null;

                return cursor.getString(0);
            } finally {
                cursor.close();
            }
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static void setSetting(android.content.ContentResolver cr, java.lang.String account, java.lang.String key, java.lang.String value) {
            android.content.ContentValues values = new android.content.ContentValues();
            // For now we only support a single account and the UI doesn't know what
            // the account name is, so we're using a global setting for SYNC_EVERYTHING.
            // Some day when we add multiple accounts to the UI this should honor the account
            // that was asked for.
            // values.put(_SYNC_ACCOUNT, account.mName);
            // values.put(_SYNC_ACCOUNT_TYPE, account.mType);
            values.put(android.provider.Contacts.SettingsColumns.KEY, key);
            values.put(android.provider.Contacts.SettingsColumns.VALUE, value);
            cr.update(android.provider.Contacts.Settings.CONTENT_URI, values, null, null);
        }
    }

    /**
     * Columns from the People table that other tables join into themselves.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface PeopleColumns {
        /**
         * The person's name.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NAME = "name";

        /**
         * Phonetic equivalent of the person's name, in a locale-dependent
         * character set (e.g. hiragana for Japanese).
         * Used for pronunciation and/or collation in some languages.
         * <p>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PHONETIC_NAME = "phonetic_name";

        /**
         * The display name. If name is not null name, else if number is not null number,
         * else if email is not null email.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DISPLAY_NAME = "display_name";

        /**
         * The field for sorting list phonetically. The content of this field
         * may not be human readable but phonetically sortable.
         * <P>Type: TEXT</p>
         *
         * @unknown Used only in Contacts application for now.
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SORT_STRING = "sort_string";

        /**
         * Notes about the person.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NOTES = "notes";

        /**
         * The number of times a person has been contacted
         * <P>Type: INTEGER</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String TIMES_CONTACTED = "times_contacted";

        /**
         * The last time a person was contacted.
         * <P>Type: INTEGER</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String LAST_TIME_CONTACTED = "last_time_contacted";

        /**
         * A custom ringtone associated with a person. Not always present.
         * <P>Type: TEXT (URI to the ringtone)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CUSTOM_RINGTONE = "custom_ringtone";

        /**
         * Whether the person should always be sent to voicemail. Not always
         * present.
         * <P>Type: INTEGER (0 for false, 1 for true)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SEND_TO_VOICEMAIL = "send_to_voicemail";

        /**
         * Is the contact starred?
         * <P>Type: INTEGER (boolean)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String STARRED = "starred";

        /**
         * The server version of the photo
         * <P>Type: TEXT (the version number portion of the photo URI)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PHOTO_VERSION = "photo_version";
    }

    /**
     * This table contains people.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class People implements android.provider.BaseColumns , android.provider.Contacts.PeopleColumns , android.provider.Contacts.PhonesColumns , android.provider.Contacts.PresenceColumns {
        /**
         * no public constructor since this is a utility class
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        private People() {
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/people");

        /**
         * The content:// style URL for filtering people by name. The filter
         * argument should be passed as an additional path segment after this URI.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_FILTER_URI = android.net.Uri.parse("content://contacts/people/filter");

        /**
         * The content:// style URL for the table that holds the deleted
         * contacts.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri DELETED_CONTENT_URI = android.net.Uri.parse("content://contacts/deleted_people");

        /**
         * The content:// style URL for filtering people that have a specific
         * E-mail or IM address. The filter argument should be passed as an
         * additional path segment after this URI. This matches any people with
         * at least one E-mail or IM {@link ContactMethods} that match the
         * filter.
         *
         * Not exposed because we expect significant changes in the contacts
         * schema and do not want to have to support this.
         *
         * @unknown 
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri WITH_EMAIL_OR_IM_FILTER_URI = android.net.Uri.parse("content://contacts/people/with_email_or_im_filter");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * people.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/person";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/person";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = android.provider.Contacts.People.NAME + " ASC";

        /**
         * The ID of the persons preferred phone number.
         * <P>Type: INTEGER (foreign key to phones table on the _ID field)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PRIMARY_PHONE_ID = "primary_phone";

        /**
         * The ID of the persons preferred email.
         * <P>Type: INTEGER (foreign key to contact_methods table on the
         * _ID field)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PRIMARY_EMAIL_ID = "primary_email";

        /**
         * The ID of the persons preferred organization.
         * <P>Type: INTEGER (foreign key to organizations table on the
         * _ID field)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PRIMARY_ORGANIZATION_ID = "primary_organization";

        /**
         * Mark a person as having been contacted.
         *
         * @param resolver
         * 		the ContentResolver to use
         * @param personId
         * 		the person who was contacted
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static void markAsContacted(android.content.ContentResolver resolver, long personId) {
            android.net.Uri uri = android.content.ContentUris.withAppendedId(android.provider.Contacts.People.CONTENT_URI, personId);
            uri = android.net.Uri.withAppendedPath(uri, "update_contact_time");
            android.content.ContentValues values = new android.content.ContentValues();
            // There is a trigger in place that will update TIMES_CONTACTED when
            // LAST_TIME_CONTACTED is modified.
            values.put(android.provider.Contacts.PeopleColumns.LAST_TIME_CONTACTED, java.lang.System.currentTimeMillis());
            resolver.update(uri, values, null, null);
        }

        /**
         *
         *
         * @unknown Used in vCard parser code.
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static long tryGetMyContactsGroupId(android.content.ContentResolver resolver) {
            android.database.Cursor groupsCursor = resolver.query(android.provider.Contacts.Groups.CONTENT_URI, android.provider.Contacts.People.GROUPS_PROJECTION, ((android.provider.Contacts.Groups.SYSTEM_ID + "='") + android.provider.Contacts.Groups.GROUP_MY_CONTACTS) + "'", null, null);
            if (groupsCursor != null) {
                try {
                    if (groupsCursor.moveToFirst()) {
                        return groupsCursor.getLong(0);
                    }
                } finally {
                    groupsCursor.close();
                }
            }
            return 0;
        }

        /**
         * Adds a person to the My Contacts group.
         *
         * @param resolver
         * 		the resolver to use
         * @param personId
         * 		the person to add to the group
         * @return the URI of the group membership row
         * @throws IllegalStateException
         * 		if the My Contacts group can't be found
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static android.net.Uri addToMyContactsGroup(android.content.ContentResolver resolver, long personId) {
            long groupId = android.provider.Contacts.People.tryGetMyContactsGroupId(resolver);
            if (groupId == 0) {
                throw new java.lang.IllegalStateException("Failed to find the My Contacts group");
            }
            return android.provider.Contacts.People.addToGroup(resolver, personId, groupId);
        }

        /**
         * Adds a person to a group referred to by name.
         *
         * @param resolver
         * 		the resolver to use
         * @param personId
         * 		the person to add to the group
         * @param groupName
         * 		the name of the group to add the contact to
         * @return the URI of the group membership row
         * @throws IllegalStateException
         * 		if the group can't be found
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static android.net.Uri addToGroup(android.content.ContentResolver resolver, long personId, java.lang.String groupName) {
            long groupId = 0;
            android.database.Cursor groupsCursor = resolver.query(android.provider.Contacts.Groups.CONTENT_URI, android.provider.Contacts.People.GROUPS_PROJECTION, android.provider.Contacts.Groups.NAME + "=?", new java.lang.String[]{ groupName }, null);
            if (groupsCursor != null) {
                try {
                    if (groupsCursor.moveToFirst()) {
                        groupId = groupsCursor.getLong(0);
                    }
                } finally {
                    groupsCursor.close();
                }
            }
            if (groupId == 0) {
                throw new java.lang.IllegalStateException("Failed to find the My Contacts group");
            }
            return android.provider.Contacts.People.addToGroup(resolver, personId, groupId);
        }

        /**
         * Adds a person to a group.
         *
         * @param resolver
         * 		the resolver to use
         * @param personId
         * 		the person to add to the group
         * @param groupId
         * 		the group to add the person to
         * @return the URI of the group membership row
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static android.net.Uri addToGroup(android.content.ContentResolver resolver, long personId, long groupId) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.Contacts.GroupMembership.PERSON_ID, personId);
            values.put(android.provider.Contacts.GroupMembership.GROUP_ID, groupId);
            return resolver.insert(android.provider.Contacts.GroupMembership.CONTENT_URI, values);
        }

        private static final java.lang.String[] GROUPS_PROJECTION = new java.lang.String[]{ android.provider.Contacts.Groups._ID };

        /**
         * Creates a new contacts and adds it to the "My Contacts" group.
         *
         * @param resolver
         * 		the ContentResolver to use
         * @param values
         * 		the values to use when creating the contact
         * @return the URI of the contact, or null if the operation fails
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static android.net.Uri createPersonInMyContactsGroup(android.content.ContentResolver resolver, android.content.ContentValues values) {
            android.net.Uri contactUri = resolver.insert(android.provider.Contacts.People.CONTENT_URI, values);
            if (contactUri == null) {
                android.util.Log.e(android.provider.Contacts.TAG, "Failed to create the contact");
                return null;
            }
            if (android.provider.Contacts.People.addToMyContactsGroup(resolver, android.content.ContentUris.parseId(contactUri)) == null) {
                resolver.delete(contactUri, null, null);
                return null;
            }
            return contactUri;
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static android.database.Cursor queryGroups(android.content.ContentResolver resolver, long person) {
            return resolver.query(android.provider.Contacts.GroupMembership.CONTENT_URI, null, "person=?", new java.lang.String[]{ java.lang.String.valueOf(person) }, android.provider.Contacts.Groups.DEFAULT_SORT_ORDER);
        }

        /**
         * Set the photo for this person. data may be null
         *
         * @param cr
         * 		the ContentResolver to use
         * @param person
         * 		the Uri of the person whose photo is to be updated
         * @param data
         * 		the byte[] that represents the photo
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static void setPhotoData(android.content.ContentResolver cr, android.net.Uri person, byte[] data) {
            android.net.Uri photoUri = android.net.Uri.withAppendedPath(person, android.provider.Contacts.Photos.CONTENT_DIRECTORY);
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.Contacts.Photos.DATA, data);
            cr.update(photoUri, values, null, null);
        }

        /**
         * Opens an InputStream for the person's photo and returns the photo as a Bitmap.
         * If the person's photo isn't present returns the placeholderImageResource instead.
         *
         * @param person
         * 		the person whose photo should be used
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static java.io.InputStream openContactPhotoInputStream(android.content.ContentResolver cr, android.net.Uri person) {
            android.net.Uri photoUri = android.net.Uri.withAppendedPath(person, android.provider.Contacts.Photos.CONTENT_DIRECTORY);
            android.database.Cursor cursor = cr.query(photoUri, new java.lang.String[]{ android.provider.Contacts.Photos.DATA }, null, null, null);
            try {
                if ((cursor == null) || (!cursor.moveToNext())) {
                    return null;
                }
                byte[] data = cursor.getBlob(0);
                if (data == null) {
                    return null;
                }
                return new java.io.ByteArrayInputStream(data);
            } finally {
                if (cursor != null)
                    cursor.close();

            }
        }

        /**
         * Opens an InputStream for the person's photo and returns the photo as a Bitmap.
         * If the person's photo isn't present returns the placeholderImageResource instead.
         *
         * @param context
         * 		the Context
         * @param person
         * 		the person whose photo should be used
         * @param placeholderImageResource
         * 		the image resource to use if the person doesn't
         * 		have a photo
         * @param options
         * 		the decoding options, can be set to null
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static android.graphics.Bitmap loadContactPhoto(android.content.Context context, android.net.Uri person, int placeholderImageResource, android.graphics.BitmapFactory.Options options) {
            if (person == null) {
                return android.provider.Contacts.People.loadPlaceholderPhoto(placeholderImageResource, context, options);
            }
            java.io.InputStream stream = android.provider.Contacts.People.openContactPhotoInputStream(context.getContentResolver(), person);
            android.graphics.Bitmap bm = (stream != null) ? android.graphics.BitmapFactory.decodeStream(stream, null, options) : null;
            if (bm == null) {
                bm = android.provider.Contacts.People.loadPlaceholderPhoto(placeholderImageResource, context, options);
            }
            return bm;
        }

        private static android.graphics.Bitmap loadPlaceholderPhoto(int placeholderImageResource, android.content.Context context, android.graphics.BitmapFactory.Options options) {
            if (placeholderImageResource == 0) {
                return null;
            }
            return android.graphics.BitmapFactory.decodeResource(context.getResources(), placeholderImageResource, options);
        }

        /**
         * A sub directory of a single person that contains all of their Phones.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final class Phones implements android.provider.BaseColumns , android.provider.Contacts.PeopleColumns , android.provider.Contacts.PhonesColumns {
            /**
             * no public constructor since this is a utility class
             */
            private Phones() {
            }

            /**
             * The directory twig for this sub-table
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String CONTENT_DIRECTORY = "phones";

            /**
             * The default sort order for this table
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String DEFAULT_SORT_ORDER = "number ASC";
        }

        /**
         * A subdirectory of a single person that contains all of their
         * ContactMethods.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final class ContactMethods implements android.provider.BaseColumns , android.provider.Contacts.ContactMethodsColumns , android.provider.Contacts.PeopleColumns {
            /**
             * no public constructor since this is a utility class
             */
            private ContactMethods() {
            }

            /**
             * The directory twig for this sub-table
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String CONTENT_DIRECTORY = "contact_methods";

            /**
             * The default sort order for this table
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String DEFAULT_SORT_ORDER = "data ASC";
        }

        /**
         * The extensions for a person
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static class Extensions implements android.provider.BaseColumns , android.provider.Contacts.ExtensionsColumns {
            /**
             * no public constructor since this is a utility class
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            private Extensions() {
            }

            /**
             * The directory twig for this sub-table
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String CONTENT_DIRECTORY = "extensions";

            /**
             * The default sort order for this table
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String DEFAULT_SORT_ORDER = "name ASC";

            /**
             * The ID of the person this phone number is assigned to.
             * <P>Type: INTEGER (long)</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String PERSON_ID = "person";
        }
    }

    /**
     * Columns from the groups table.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface GroupsColumns {
        /**
         * The group name.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NAME = "name";

        /**
         * Notes about the group.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NOTES = "notes";

        /**
         * Whether this group should be synced if the SYNC_EVERYTHING settings is false
         * for this group's account.
         * <P>Type: INTEGER (boolean)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SHOULD_SYNC = "should_sync";

        /**
         * The ID of this group if it is a System Group, null otherwise.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SYSTEM_ID = "system_id";
    }

    /**
     * This table contains the groups for an account.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Groups implements android.provider.BaseColumns , android.provider.Contacts.GroupsColumns {
        /**
         * no public constructor since this is a utility class
         */
        private Groups() {
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/groups");

        /**
         * The content:// style URL for the table that holds the deleted
         * groups.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri DELETED_CONTENT_URI = android.net.Uri.parse("content://contacts/deleted_groups");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * groups.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/contactsgroup";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * group.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contactsgroup";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = android.provider.Contacts.GroupsColumns.NAME + " ASC";

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String GROUP_ANDROID_STARRED = "Starred in Android";

        /**
         * The "My Contacts" system group.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String GROUP_MY_CONTACTS = "Contacts";
    }

    /**
     * Columns from the Phones table that other columns join into themselves.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface PhonesColumns {
        /**
         * The type of the the phone number.
         * <P>Type: INTEGER (one of the constants below)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String TYPE = "type";

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_CUSTOM = 0;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_HOME = 1;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_MOBILE = 2;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_WORK = 3;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_FAX_WORK = 4;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_FAX_HOME = 5;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_PAGER = 6;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_OTHER = 7;

        /**
         * The user provided label for the phone number, only used if TYPE is TYPE_CUSTOM.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String LABEL = "label";

        /**
         * The phone number as the user entered it.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NUMBER = "number";

        /**
         * The normalized phone number
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NUMBER_KEY = "number_key";

        /**
         * Whether this is the primary phone number
         * <P>Type: INTEGER (if set, non-0 means true)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String ISPRIMARY = "isprimary";
    }

    /**
     * This table stores phone numbers and a reference to the person that the
     * contact method belongs to. Phone numbers are stored separately from
     * other contact methods to make caller ID lookup more efficient.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Phones implements android.provider.BaseColumns , android.provider.Contacts.PeopleColumns , android.provider.Contacts.PhonesColumns {
        /**
         * no public constructor since this is a utility class
         */
        private Phones() {
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.CharSequence getDisplayLabel(android.content.Context context, int type, java.lang.CharSequence label, java.lang.CharSequence[] labelArray) {
            java.lang.CharSequence display = "";
            if (type != android.provider.Contacts.People.Phones.TYPE_CUSTOM) {
                java.lang.CharSequence[] labels = (labelArray != null) ? labelArray : context.getResources().getTextArray(com.android.internal.R.array.phoneTypes);
                try {
                    display = labels[type - 1];
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    display = labels[android.provider.Contacts.People.Phones.TYPE_HOME - 1];
                }
            } else {
                if (!android.text.TextUtils.isEmpty(label)) {
                    display = label;
                }
            }
            return display;
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.CharSequence getDisplayLabel(android.content.Context context, int type, java.lang.CharSequence label) {
            return android.provider.Contacts.Phones.getDisplayLabel(context, type, label, null);
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/phones");

        /**
         * The content:// style URL for filtering phone numbers
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_FILTER_URL = android.net.Uri.parse("content://contacts/phones/filter");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * phones.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/phone";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * phone.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/phone";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "name ASC";

        /**
         * The ID of the person this phone number is assigned to.
         * <P>Type: INTEGER (long)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";
    }

    /**
     *
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class GroupMembership implements android.provider.BaseColumns , android.provider.Contacts.GroupsColumns {
        /**
         * no public constructor since this is a utility class
         */
        private GroupMembership() {
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/groupmembership");

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri RAW_CONTENT_URI = android.net.Uri.parse("content://contacts/groupmembershipraw");

        /**
         * The directory twig for this sub-table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_DIRECTORY = "groupmembership";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of all
         * person groups.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/contactsgroupmembership";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * person group.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contactsgroupmembership";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "group_id ASC";

        /**
         * The row id of the accounts group.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String GROUP_ID = "group_id";

        /**
         * The sync id of the group.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String GROUP_SYNC_ID = "group_sync_id";

        /**
         * The account of the group.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String GROUP_SYNC_ACCOUNT = "group_sync_account";

        /**
         * The account type of the group.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String GROUP_SYNC_ACCOUNT_TYPE = "group_sync_account_type";

        /**
         * The row id of the person.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";
    }

    /**
     * Columns from the ContactMethods table that other tables join into
     * themseleves.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface ContactMethodsColumns {
        /**
         * The kind of the the contact method. For example, email address,
         * postal address, etc.
         * <P>Type: INTEGER (one of the values below)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String KIND = "kind";

        /**
         * The type of the contact method, must be one of the types below.
         * <P>Type: INTEGER (one of the values below)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String TYPE = "type";

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_CUSTOM = 0;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_HOME = 1;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_WORK = 2;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_OTHER = 3;

        /**
         *
         *
         * @unknown This is temporal. TYPE_MOBILE should be added to TYPE in the future.
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int MOBILE_EMAIL_TYPE_INDEX = 2;

        /**
         *
         *
         * @unknown This is temporal. TYPE_MOBILE should be added to TYPE in the future.
        This is not "mobile" but "CELL" since vCard uses it for identifying mobile phone.
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String MOBILE_EMAIL_TYPE_NAME = "_AUTO_CELL";

        /**
         * The user defined label for the the contact method.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String LABEL = "label";

        /**
         * The data for the contact method.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DATA = "data";

        /**
         * Auxiliary data for the contact method.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String AUX_DATA = "aux_data";

        /**
         * Whether this is the primary organization
         * <P>Type: INTEGER (if set, non-0 means true)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String ISPRIMARY = "isprimary";
    }

    /**
     * This table stores all non-phone contact methods and a reference to the
     * person that the contact method belongs to.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class ContactMethods implements android.provider.BaseColumns , android.provider.Contacts.ContactMethodsColumns , android.provider.Contacts.PeopleColumns {
        /**
         * The column with latitude data for postal locations
         * <P>Type: REAL</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String POSTAL_LOCATION_LATITUDE = android.provider.Contacts.ContactMethodsColumns.DATA;

        /**
         * The column with longitude data for postal locations
         * <P>Type: REAL</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String POSTAL_LOCATION_LONGITUDE = android.provider.Contacts.ContactMethodsColumns.AUX_DATA;

        /**
         * The predefined IM protocol types. The protocol can either be non-present, one
         * of these types, or a free-form string. These cases are encoded in the AUX_DATA
         * column as:
         *  - null
         *  - pre:<an integer, one of the protocols below>
         *  - custom:<a string>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_AIM = 0;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_MSN = 1;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_YAHOO = 2;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_SKYPE = 3;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_QQ = 4;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_GOOGLE_TALK = 5;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_ICQ = 6;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int PROTOCOL_JABBER = 7;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static java.lang.String encodePredefinedImProtocol(int protocol) {
            return "pre:" + protocol;
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static java.lang.String encodeCustomImProtocol(java.lang.String protocolString) {
            return "custom:" + protocolString;
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static java.lang.Object decodeImProtocol(java.lang.String encodedString) {
            if (encodedString == null) {
                return null;
            }
            if (encodedString.startsWith("pre:")) {
                return java.lang.Integer.parseInt(encodedString.substring(4));
            }
            if (encodedString.startsWith("custom:")) {
                return encodedString.substring(7);
            }
            throw new java.lang.IllegalArgumentException("the value is not a valid encoded protocol, " + encodedString);
        }

        /**
         * TODO find a place to put the canonical version of these.
         */
        interface ProviderNames {
            // 
            // NOTE: update Contacts.java with new providers when they're added.
            // 
            java.lang.String YAHOO = "Yahoo";

            java.lang.String GTALK = "GTalk";

            java.lang.String MSN = "MSN";

            java.lang.String ICQ = "ICQ";

            java.lang.String AIM = "AIM";

            java.lang.String XMPP = "XMPP";

            java.lang.String JABBER = "JABBER";

            java.lang.String SKYPE = "SKYPE";

            java.lang.String QQ = "QQ";
        }

        /**
         * This looks up the provider name defined in
         * from the predefined IM protocol id.
         * This is used for interacting with the IM application.
         *
         * @param protocol
         * 		the protocol ID
         * @return the provider name the IM app uses for the given protocol, or null if no
        provider is defined for the given protocol
         * @deprecated see {@link android.provider.ContactsContract}
         * @unknown 
         */
        @java.lang.Deprecated
        public static java.lang.String lookupProviderNameFromId(int protocol) {
            switch (protocol) {
                case android.provider.Contacts.ContactMethods.PROTOCOL_GOOGLE_TALK :
                    return android.provider.Contacts.ContactMethods.ProviderNames.GTALK;
                case android.provider.Contacts.ContactMethods.PROTOCOL_AIM :
                    return android.provider.Contacts.ContactMethods.ProviderNames.AIM;
                case android.provider.Contacts.ContactMethods.PROTOCOL_MSN :
                    return android.provider.Contacts.ContactMethods.ProviderNames.MSN;
                case android.provider.Contacts.ContactMethods.PROTOCOL_YAHOO :
                    return android.provider.Contacts.ContactMethods.ProviderNames.YAHOO;
                case android.provider.Contacts.ContactMethods.PROTOCOL_ICQ :
                    return android.provider.Contacts.ContactMethods.ProviderNames.ICQ;
                case android.provider.Contacts.ContactMethods.PROTOCOL_JABBER :
                    return android.provider.Contacts.ContactMethods.ProviderNames.JABBER;
                case android.provider.Contacts.ContactMethods.PROTOCOL_SKYPE :
                    return android.provider.Contacts.ContactMethods.ProviderNames.SKYPE;
                case android.provider.Contacts.ContactMethods.PROTOCOL_QQ :
                    return android.provider.Contacts.ContactMethods.ProviderNames.QQ;
            }
            return null;
        }

        /**
         * no public constructor since this is a utility class
         */
        private ContactMethods() {
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.CharSequence getDisplayLabel(android.content.Context context, int kind, int type, java.lang.CharSequence label) {
            java.lang.CharSequence display = "";
            switch (kind) {
                case android.provider.Contacts.KIND_EMAIL :
                    {
                        if (type != android.provider.Contacts.People.ContactMethods.TYPE_CUSTOM) {
                            java.lang.CharSequence[] labels = context.getResources().getTextArray(com.android.internal.R.array.emailAddressTypes);
                            try {
                                display = labels[type - 1];
                            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                display = labels[android.provider.Contacts.ContactMethods.TYPE_HOME - 1];
                            }
                        } else {
                            if (!android.text.TextUtils.isEmpty(label)) {
                                display = label;
                            }
                        }
                        break;
                    }
                case android.provider.Contacts.KIND_POSTAL :
                    {
                        if (type != android.provider.Contacts.People.ContactMethods.TYPE_CUSTOM) {
                            java.lang.CharSequence[] labels = context.getResources().getTextArray(com.android.internal.R.array.postalAddressTypes);
                            try {
                                display = labels[type - 1];
                            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                display = labels[android.provider.Contacts.ContactMethods.TYPE_HOME - 1];
                            }
                        } else {
                            if (!android.text.TextUtils.isEmpty(label)) {
                                display = label;
                            }
                        }
                        break;
                    }
                default :
                    display = context.getString(R.string.untitled);
            }
            return display;
        }

        /**
         * Add a longitude and latitude location to a postal address.
         *
         * @param context
         * 		the context to use when updating the database
         * @param postalId
         * 		the address to update
         * @param latitude
         * 		the latitude for the address
         * @param longitude
         * 		the longitude for the address
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public void addPostalLocation(android.content.Context context, long postalId, double latitude, double longitude) {
            final android.content.ContentResolver resolver = context.getContentResolver();
            // Insert the location
            android.content.ContentValues values = new android.content.ContentValues(2);
            values.put(android.provider.Contacts.ContactMethods.POSTAL_LOCATION_LATITUDE, latitude);
            values.put(android.provider.Contacts.ContactMethods.POSTAL_LOCATION_LONGITUDE, longitude);
            android.net.Uri loc = resolver.insert(android.provider.Contacts.ContactMethods.CONTENT_URI, values);
            long locId = android.content.ContentUris.parseId(loc);
            // Update the postal address
            values.clear();
            values.put(android.provider.Contacts.ContactMethodsColumns.AUX_DATA, locId);
            resolver.update(android.content.ContentUris.withAppendedId(android.provider.Contacts.ContactMethods.CONTENT_URI, postalId), values, null, null);
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/contact_methods");

        /**
         * The content:// style URL for sub-directory of e-mail addresses.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_EMAIL_URI = android.net.Uri.parse("content://contacts/contact_methods/email");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         *
         * @deprecated see {@link android.provider.ContactsContract}
        phones.
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/contact-methods";

        /**
         * The MIME type of a {@link #CONTENT_EMAIL_URI} sub-directory of
         * multiple {@link Contacts#KIND_EMAIL} entries.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_EMAIL_TYPE = "vnd.android.cursor.dir/email";

        /**
         * The MIME type of a {@link #CONTENT_EMAIL_URI} sub-directory of
         * multiple {@link Contacts#KIND_POSTAL} entries.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_POSTAL_TYPE = "vnd.android.cursor.dir/postal-address";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * {@link Contacts#KIND_EMAIL} entry.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_EMAIL_ITEM_TYPE = "vnd.android.cursor.item/email";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * {@link Contacts#KIND_POSTAL} entry.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_POSTAL_ITEM_TYPE = "vnd.android.cursor.item/postal-address";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * {@link Contacts#KIND_IM} entry.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_IM_ITEM_TYPE = "vnd.android.cursor.item/jabber-im";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "name ASC";

        /**
         * The ID of the person this contact method is assigned to.
         * <P>Type: INTEGER (long)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";
    }

    /**
     * The IM presence columns with some contacts specific columns mixed in.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface PresenceColumns {
        /**
         * The priority, an integer, used by XMPP presence
         * <P>Type: INTEGER</P>
         */
        java.lang.String PRIORITY = "priority";

        /**
         * The server defined status.
         * <P>Type: INTEGER (one of the values below)</P>
         */
        java.lang.String PRESENCE_STATUS = android.provider.ContactsContract.StatusUpdates.PRESENCE;

        /**
         * Presence Status definition
         */
        int OFFLINE = android.provider.ContactsContract.StatusUpdates.OFFLINE;

        int INVISIBLE = android.provider.ContactsContract.StatusUpdates.INVISIBLE;

        int AWAY = android.provider.ContactsContract.StatusUpdates.AWAY;

        int IDLE = android.provider.ContactsContract.StatusUpdates.IDLE;

        int DO_NOT_DISTURB = android.provider.ContactsContract.StatusUpdates.DO_NOT_DISTURB;

        int AVAILABLE = android.provider.ContactsContract.StatusUpdates.AVAILABLE;

        /**
         * The user defined status line.
         * <P>Type: TEXT</P>
         */
        java.lang.String PRESENCE_CUSTOM_STATUS = android.provider.ContactsContract.StatusUpdates.STATUS;

        /**
         * The IM service the presence is coming from. Formatted using either
         * {@link Contacts.ContactMethods#encodePredefinedImProtocol} or
         * {@link Contacts.ContactMethods#encodeCustomImProtocol}.
         * <P>Type: STRING</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String IM_PROTOCOL = "im_protocol";

        /**
         * The IM handle the presence item is for. The handle is scoped to
         * the {@link #IM_PROTOCOL}.
         * <P>Type: STRING</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String IM_HANDLE = "im_handle";

        /**
         * The IM account for the local user that the presence data came from.
         * <P>Type: STRING</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String IM_ACCOUNT = "im_account";
    }

    /**
     * Contains presence information about contacts.
     *
     * @unknown 
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Presence implements android.provider.BaseColumns , android.provider.Contacts.PeopleColumns , android.provider.Contacts.PresenceColumns {
        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/presence");

        /**
         * The ID of the person this presence item is assigned to.
         * <P>Type: INTEGER (long)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";

        /**
         * Gets the resource ID for the proper presence icon.
         *
         * @param status
         * 		the status to get the icon for
         * @return the resource ID for the proper presence icon
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int getPresenceIconResourceId(int status) {
            switch (status) {
                case android.provider.Contacts.People.AVAILABLE :
                    return com.android.internal.R.drawable.presence_online;
                case android.provider.Contacts.People.IDLE :
                case android.provider.Contacts.People.AWAY :
                    return com.android.internal.R.drawable.presence_away;
                case android.provider.Contacts.People.DO_NOT_DISTURB :
                    return com.android.internal.R.drawable.presence_busy;
                case android.provider.Contacts.People.INVISIBLE :
                    return com.android.internal.R.drawable.presence_invisible;
                case android.provider.Contacts.People.OFFLINE :
                default :
                    return com.android.internal.R.drawable.presence_offline;
            }
        }

        /**
         * Sets a presence icon to the proper graphic
         *
         * @param icon
         * 		the icon to to set
         * @param serverStatus
         * 		that status
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final void setPresenceIcon(android.widget.ImageView icon, int serverStatus) {
            icon.setImageResource(android.provider.Contacts.Presence.getPresenceIconResourceId(serverStatus));
        }
    }

    /**
     * Columns from the Organizations table that other columns join into themselves.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface OrganizationColumns {
        /**
         * The type of the organizations.
         * <P>Type: INTEGER (one of the constants below)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String TYPE = "type";

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_CUSTOM = 0;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_WORK = 1;

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final int TYPE_OTHER = 2;

        /**
         * The user provided label, only used if TYPE is TYPE_CUSTOM.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String LABEL = "label";

        /**
         * The name of the company for this organization.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String COMPANY = "company";

        /**
         * The title within this organization.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String TITLE = "title";

        /**
         * The person this organization is tied to.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";

        /**
         * Whether this is the primary organization
         * <P>Type: INTEGER (if set, non-0 means true)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String ISPRIMARY = "isprimary";
    }

    /**
     * A sub directory of a single person that contains all of their Phones.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Organizations implements android.provider.BaseColumns , android.provider.Contacts.OrganizationColumns {
        /**
         * no public constructor since this is a utility class
         */
        private Organizations() {
        }

        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.CharSequence getDisplayLabel(android.content.Context context, int type, java.lang.CharSequence label) {
            java.lang.CharSequence display = "";
            if (type != android.provider.Contacts.OrganizationColumns.TYPE_CUSTOM) {
                java.lang.CharSequence[] labels = context.getResources().getTextArray(com.android.internal.R.array.organizationTypes);
                try {
                    display = labels[type - 1];
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    display = labels[android.provider.Contacts.Organizations.TYPE_WORK - 1];
                }
            } else {
                if (!android.text.TextUtils.isEmpty(label)) {
                    display = label;
                }
            }
            return display;
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/organizations");

        /**
         * The directory twig for this sub-table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_DIRECTORY = "organizations";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "company, title, isprimary ASC";
    }

    /**
     * Columns from the Photos table that other columns join into themselves.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface PhotosColumns {
        /**
         * The _SYNC_VERSION of the photo that was last downloaded
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCAL_VERSION = "local_version";

        /**
         * The person this photo is associated with.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";

        /**
         * non-zero if a download is required and the photo isn't marked as a bad resource.
         * You must specify this in the columns in order to use it in the where clause.
         * <P>Type: INTEGER(boolean)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DOWNLOAD_REQUIRED = "download_required";

        /**
         * non-zero if this photo is known to exist on the server
         * <P>Type: INTEGER(boolean)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String EXISTS_ON_SERVER = "exists_on_server";

        /**
         * Contains the description of the upload or download error from
         * the previous attempt. If null then the previous attempt succeeded.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SYNC_ERROR = "sync_error";

        /**
         * The image data, or null if there is no image.
         * <P>Type: BLOB</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DATA = "data";
    }

    /**
     * The photos over all of the people
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Photos implements android.provider.BaseColumns , android.provider.Contacts.PhotosColumns {
        /**
         * no public constructor since this is a utility class
         */
        private Photos() {
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/photos");

        /**
         * The directory twig for this sub-table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_DIRECTORY = "photo";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "person ASC";
    }

    /**
     *
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public interface ExtensionsColumns {
        /**
         * The name of this extension. May not be null. There may be at most one row for each name.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String NAME = "name";

        /**
         * The value of this extension. May not be null.
         * <P>Type: TEXT</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String VALUE = "value";
    }

    /**
     * The extensions for a person
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Extensions implements android.provider.BaseColumns , android.provider.Contacts.ExtensionsColumns {
        /**
         * no public constructor since this is a utility class
         */
        private Extensions() {
        }

        /**
         * The content:// style URL for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://contacts/extensions");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * phones.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/contact_extensions";

        /**
         * The MIME type of a {@link #CONTENT_URI} subdirectory of a single
         * phone.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact_extensions";

        /**
         * The default sort order for this table
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_SORT_ORDER = "person, name ASC";

        /**
         * The ID of the person this phone number is assigned to.
         * <P>Type: INTEGER (long)</P>
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String PERSON_ID = "person";
    }

    /**
     * Contains helper classes used to create or manage {@link android.content.Intent Intents}
     * that involve contacts.
     *
     * @deprecated see {@link android.provider.ContactsContract}
     */
    @java.lang.Deprecated
    public static final class Intents {
        /**
         *
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public Intents() {
        }

        /**
         * This is the intent that is fired when a search suggestion is clicked on.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SEARCH_SUGGESTION_CLICKED = android.provider.ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED;

        /**
         * This is the intent that is fired when a search suggestion for dialing a number
         * is clicked on.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SEARCH_SUGGESTION_DIAL_NUMBER_CLICKED = android.provider.ContactsContract.Intents.SEARCH_SUGGESTION_DIAL_NUMBER_CLICKED;

        /**
         * This is the intent that is fired when a search suggestion for creating a contact
         * is clicked on.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SEARCH_SUGGESTION_CREATE_CONTACT_CLICKED = android.provider.ContactsContract.Intents.SEARCH_SUGGESTION_CREATE_CONTACT_CLICKED;

        /**
         * Starts an Activity that lets the user pick a contact to attach an image to.
         * After picking the contact it launches the image cropper in face detection mode.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String ATTACH_IMAGE = android.provider.ContactsContract.Intents.ATTACH_IMAGE;

        /**
         * Takes as input a data URI with a mailto: or tel: scheme. If a single
         * contact exists with the given data it will be shown. If no contact
         * exists, a dialog will ask the user if they want to create a new
         * contact with the provided details filled in. If multiple contacts
         * share the data the user will be prompted to pick which contact they
         * want to view.
         * <p>
         * For <code>mailto:</code> URIs, the scheme specific portion must be a
         * raw email address, such as one built using
         * {@link Uri#fromParts(String, String, String)}.
         * <p>
         * For <code>tel:</code> URIs, the scheme specific portion is compared
         * to existing numbers using the standard caller ID lookup algorithm.
         * The number must be properly encoded, for example using
         * {@link Uri#fromParts(String, String, String)}.
         * <p>
         * Any extras from the {@link Insert} class will be passed along to the
         * create activity if there are no contacts to show.
         * <p>
         * Passing true for the {@link #EXTRA_FORCE_CREATE} extra will skip
         * prompting the user when the contact doesn't exist.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String SHOW_OR_CREATE_CONTACT = android.provider.ContactsContract.Intents.SHOW_OR_CREATE_CONTACT;

        /**
         * Used with {@link #SHOW_OR_CREATE_CONTACT} to force creating a new
         * contact if no matching contact found. Otherwise, default behavior is
         * to prompt user with dialog before creating.
         * <p>
         * Type: BOOLEAN
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_FORCE_CREATE = android.provider.ContactsContract.Intents.EXTRA_FORCE_CREATE;

        /**
         * Used with {@link #SHOW_OR_CREATE_CONTACT} to specify an exact
         * description to be shown when prompting user about creating a new
         * contact.
         * <p>
         * Type: STRING
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_CREATE_DESCRIPTION = android.provider.ContactsContract.Intents.EXTRA_CREATE_DESCRIPTION;

        /**
         * Optional extra used with {@link #SHOW_OR_CREATE_CONTACT} to specify a
         * dialog location using screen coordinates. When not specified, the
         * dialog will be centered.
         *
         * @unknown pending API council review
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_TARGET_RECT = android.provider.ContactsContract.Intents.EXTRA_TARGET_RECT;

        /**
         * Intents related to the Contacts app UI.
         *
         * @deprecated Do not use. This is not supported.
         */
        @java.lang.Deprecated
        public static final class UI {
            /**
             *
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public UI() {
            }

            /**
             * The action for the default contacts list tab.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_DEFAULT = "com.android.contacts.action.LIST_DEFAULT";

            /**
             * The action for the contacts list tab.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_GROUP_ACTION = "com.android.contacts.action.LIST_GROUP";

            /**
             * When in LIST_GROUP_ACTION mode, this is the group to display.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String GROUP_NAME_EXTRA_KEY = "com.android.contacts.extra.GROUP";

            /**
             * The action for the all contacts list tab.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_ALL_CONTACTS_ACTION = "com.android.contacts.action.LIST_ALL_CONTACTS";

            /**
             * The action for the contacts with phone numbers list tab.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_CONTACTS_WITH_PHONES_ACTION = "com.android.contacts.action.LIST_CONTACTS_WITH_PHONES";

            /**
             * The action for the starred contacts list tab.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_STARRED_ACTION = "com.android.contacts.action.LIST_STARRED";

            /**
             * The action for the frequent contacts list tab.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_FREQUENT_ACTION = "com.android.contacts.action.LIST_FREQUENT";

            /**
             * The action for the "strequent" contacts list tab. It first lists the starred
             * contacts in alphabetical order and then the frequent contacts in descending
             * order of the number of times they have been contacted.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String LIST_STREQUENT_ACTION = "com.android.contacts.action.LIST_STREQUENT";

            /**
             * A key for to be used as an intent extra to set the activity
             * title to a custom String value.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String TITLE_EXTRA_KEY = "com.android.contacts.extra.TITLE_EXTRA";

            /**
             * Activity Action: Display a filtered list of contacts
             * <p>
             * Input: Extra field {@link #FILTER_TEXT_EXTRA_KEY} is the text to use for
             * filtering
             * <p>
             * Output: Nothing.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String FILTER_CONTACTS_ACTION = "com.android.contacts.action.FILTER_CONTACTS";

            /**
             * Used as an int extra field in {@link #FILTER_CONTACTS_ACTION}
             * intents to supply the text on which to filter.
             *
             * @deprecated Do not use. This is not supported.
             */
            @java.lang.Deprecated
            public static final java.lang.String FILTER_TEXT_EXTRA_KEY = "com.android.contacts.extra.FILTER_TEXT";
        }

        /**
         * Convenience class that contains string constants used
         * to create contact {@link android.content.Intent Intents}.
         *
         * @deprecated see {@link android.provider.ContactsContract}
         */
        @java.lang.Deprecated
        public static final class Insert {
            /**
             *
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public Insert() {
            }

            /**
             * The action code to use when adding a contact
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String ACTION = android.provider.ContactsContract.Intents.Insert.ACTION;

            /**
             * If present, forces a bypass of quick insert mode.
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String FULL_MODE = android.provider.ContactsContract.Intents.Insert.FULL_MODE;

            /**
             * The extra field for the contact name.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String NAME = android.provider.ContactsContract.Intents.Insert.NAME;

            /**
             * The extra field for the contact phonetic name.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String PHONETIC_NAME = android.provider.ContactsContract.Intents.Insert.PHONETIC_NAME;

            /**
             * The extra field for the contact company.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String COMPANY = android.provider.ContactsContract.Intents.Insert.COMPANY;

            /**
             * The extra field for the contact job title.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String JOB_TITLE = android.provider.ContactsContract.Intents.Insert.JOB_TITLE;

            /**
             * The extra field for the contact notes.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String NOTES = android.provider.ContactsContract.Intents.Insert.NOTES;

            /**
             * The extra field for the contact phone number.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String PHONE = android.provider.ContactsContract.Intents.Insert.PHONE;

            /**
             * The extra field for the contact phone number type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.PhonesColumns PhonesColumns},
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String PHONE_TYPE = android.provider.ContactsContract.Intents.Insert.PHONE_TYPE;

            /**
             * The extra field for the phone isprimary flag.
             * <P>Type: boolean</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String PHONE_ISPRIMARY = android.provider.ContactsContract.Intents.Insert.PHONE_ISPRIMARY;

            /**
             * The extra field for an optional second contact phone number.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String SECONDARY_PHONE = android.provider.ContactsContract.Intents.Insert.SECONDARY_PHONE;

            /**
             * The extra field for an optional second contact phone number type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.PhonesColumns PhonesColumns},
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String SECONDARY_PHONE_TYPE = android.provider.ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE;

            /**
             * The extra field for an optional third contact phone number.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String TERTIARY_PHONE = android.provider.ContactsContract.Intents.Insert.TERTIARY_PHONE;

            /**
             * The extra field for an optional third contact phone number type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.PhonesColumns PhonesColumns},
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String TERTIARY_PHONE_TYPE = android.provider.ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE;

            /**
             * The extra field for the contact email address.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String EMAIL = android.provider.ContactsContract.Intents.Insert.EMAIL;

            /**
             * The extra field for the contact email type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.ContactMethodsColumns ContactMethodsColumns}
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String EMAIL_TYPE = android.provider.ContactsContract.Intents.Insert.EMAIL_TYPE;

            /**
             * The extra field for the email isprimary flag.
             * <P>Type: boolean</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String EMAIL_ISPRIMARY = android.provider.ContactsContract.Intents.Insert.EMAIL_ISPRIMARY;

            /**
             * The extra field for an optional second contact email address.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String SECONDARY_EMAIL = android.provider.ContactsContract.Intents.Insert.SECONDARY_EMAIL;

            /**
             * The extra field for an optional second contact email type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.ContactMethodsColumns ContactMethodsColumns}
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String SECONDARY_EMAIL_TYPE = android.provider.ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE;

            /**
             * The extra field for an optional third contact email address.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String TERTIARY_EMAIL = android.provider.ContactsContract.Intents.Insert.TERTIARY_EMAIL;

            /**
             * The extra field for an optional third contact email type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.ContactMethodsColumns ContactMethodsColumns}
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String TERTIARY_EMAIL_TYPE = android.provider.ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE;

            /**
             * The extra field for the contact postal address.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String POSTAL = android.provider.ContactsContract.Intents.Insert.POSTAL;

            /**
             * The extra field for the contact postal address type.
             * <P>Type: Either an integer value from {@link android.provider.Contacts.ContactMethodsColumns ContactMethodsColumns}
             *  or a string specifying a custom label.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String POSTAL_TYPE = android.provider.ContactsContract.Intents.Insert.POSTAL_TYPE;

            /**
             * The extra field for the postal isprimary flag.
             * <P>Type: boolean</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String POSTAL_ISPRIMARY = android.provider.ContactsContract.Intents.Insert.POSTAL_ISPRIMARY;

            /**
             * The extra field for an IM handle.
             * <P>Type: String</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String IM_HANDLE = android.provider.ContactsContract.Intents.Insert.IM_HANDLE;

            /**
             * The extra field for the IM protocol
             * <P>Type: the result of {@link Contacts.ContactMethods#encodePredefinedImProtocol}
             * or {@link Contacts.ContactMethods#encodeCustomImProtocol}.</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String IM_PROTOCOL = android.provider.ContactsContract.Intents.Insert.IM_PROTOCOL;

            /**
             * The extra field for the IM isprimary flag.
             * <P>Type: boolean</P>
             *
             * @deprecated see {@link android.provider.ContactsContract}
             */
            @java.lang.Deprecated
            public static final java.lang.String IM_ISPRIMARY = android.provider.ContactsContract.Intents.Insert.IM_ISPRIMARY;
        }
    }
}

