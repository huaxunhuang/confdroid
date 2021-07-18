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
 * <p>
 * The contract between the calendar provider and applications. Contains
 * definitions for the supported URIs and data columns.
 * </p>
 * <h3>Overview</h3>
 * <p>
 * CalendarContract defines the data model of calendar and event related
 * information. This data is stored in a number of tables:
 * </p>
 * <ul>
 * <li>The {@link Calendars} table holds the calendar specific information. Each
 * row in this table contains the details for a single calendar, such as the
 * name, color, sync info, etc.</li>
 * <li>The {@link Events} table holds the event specific information. Each row
 * in this table has the info for a single event. It contains information such
 * as event title, location, start time, end time, etc. The event can occur
 * one-time or can recur multiple times. Attendees, reminders, and extended
 * properties are stored on separate tables and reference the {@link Events#_ID}
 * to link them with the event.</li>
 * <li>The {@link Instances} table holds the start and end time for occurrences
 * of an event. Each row in this table represents a single occurrence. For
 * one-time events there will be a 1:1 mapping of instances to events. For
 * recurring events, multiple rows will automatically be generated which
 * correspond to multiple occurrences of that event.</li>
 * <li>The {@link Attendees} table holds the event attendee or guest
 * information. Each row represents a single guest of an event. It specifies the
 * type of guest they are and their attendance response for the event.</li>
 * <li>The {@link Reminders} table holds the alert/notification data. Each row
 * represents a single alert for an event. An event can have multiple reminders.
 * The number of reminders per event is specified in
 * {@link Calendars#MAX_REMINDERS} which is set by the Sync Adapter that owns
 * the given calendar. Reminders are specified in minutes before the event and
 * have a type.</li>
 * <li>The {@link ExtendedProperties} table holds opaque data fields used by the
 * sync adapter. The provider takes no action with items in this table except to
 * delete them when their related events are deleted.</li>
 * </ul>
 * <p>
 * Other tables include:
 * </p>
 * <ul>
 * <li>
 * {@link SyncState}, which contains free-form data maintained by the sync
 * adapters</li>
 * </ul>
 */
public final class CalendarContract {
    private static final java.lang.String TAG = "Calendar";

    /**
     * Broadcast Action: This is the intent that gets fired when an alarm
     * notification needs to be posted for a reminder.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_EVENT_REMINDER = "android.intent.action.EVENT_REMINDER";

    /**
     * Activity Action: Display the event to the user in the custom app as
     * specified in {@link EventsColumns#CUSTOM_APP_PACKAGE}. The custom app
     * will be started via {@link Activity#startActivityForResult(Intent, int)}
     * and it should call {@link Activity#setResult(int)} with
     * {@link Activity#RESULT_OK} or {@link Activity#RESULT_CANCELED} to
     * acknowledge whether the action was handled or not.
     *
     * The custom app should have an intent filter like the following:
     * <pre>
     * &lt;intent-filter&gt;
     *    &lt;action android:name="android.provider.calendar.action.HANDLE_CUSTOM_EVENT" /&gt;
     *    &lt;category android:name="android.intent.category.DEFAULT" /&gt;
     *    &lt;data android:mimeType="vnd.android.cursor.item/event" /&gt;
     * &lt;/intent-filter&gt;</pre>
     * <p>
     * Input: {@link Intent#getData} has the event URI. The extra
     * {@link #EXTRA_EVENT_BEGIN_TIME} has the start time of the instance. The
     * extra {@link #EXTRA_CUSTOM_APP_URI} will have the
     * {@link EventsColumns#CUSTOM_APP_URI}.
     * <p>
     * Output: {@link Activity#RESULT_OK} if this was handled; otherwise
     * {@link Activity#RESULT_CANCELED}.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_HANDLE_CUSTOM_EVENT = "android.provider.calendar.action.HANDLE_CUSTOM_EVENT";

    /**
     * Intent Extras key: {@link EventsColumns#CUSTOM_APP_URI} for the event in
     * the {@link #ACTION_HANDLE_CUSTOM_EVENT} intent
     */
    public static final java.lang.String EXTRA_CUSTOM_APP_URI = "customAppUri";

    /**
     * Intent Extras key: The start time of an event or an instance of a
     * recurring event. (milliseconds since epoch)
     */
    public static final java.lang.String EXTRA_EVENT_BEGIN_TIME = "beginTime";

    /**
     * Intent Extras key: The end time of an event or an instance of a recurring
     * event. (milliseconds since epoch)
     */
    public static final java.lang.String EXTRA_EVENT_END_TIME = "endTime";

    /**
     * Intent Extras key: When creating an event, set this to true to create an
     * all-day event by default
     */
    public static final java.lang.String EXTRA_EVENT_ALL_DAY = "allDay";

    /**
     * This authority is used for writing to or querying from the calendar
     * provider. Note: This is set at first run and cannot be changed without
     * breaking apps that access the provider.
     */
    public static final java.lang.String AUTHORITY = "com.android.calendar";

    /**
     * The content:// style URL for the top-level calendar authority
     */
    public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://" + android.provider.CalendarContract.AUTHORITY);

    /**
     * An optional insert, update or delete URI parameter that allows the caller
     * to specify that it is a sync adapter. The default value is false. If set
     * to true, the modified row is not marked as "dirty" (needs to be synced)
     * and when the provider calls
     * {@link ContentResolver#notifyChange(android.net.Uri, android.database.ContentObserver, boolean)}
     * , the third parameter "syncToNetwork" is set to false. Furthermore, if
     * set to true, the caller must also include
     * {@link Calendars#ACCOUNT_NAME} and {@link Calendars#ACCOUNT_TYPE} as
     * query parameters.
     *
     * @see Uri.Builder#appendQueryParameter(java.lang.String, java.lang.String)
     */
    public static final java.lang.String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";

    /**
     * A special account type for calendars not associated with any account.
     * Normally calendars that do not match an account on the device will be
     * removed. Setting the account_type on a calendar to this will prevent it
     * from being wiped if it does not match an existing account.
     *
     * @see SyncColumns#ACCOUNT_TYPE
     */
    public static final java.lang.String ACCOUNT_TYPE_LOCAL = "LOCAL";

    /**
     * This utility class cannot be instantiated
     */
    private CalendarContract() {
    }

    /**
     * Generic columns for use by sync adapters. The specific functions of these
     * columns are private to the sync adapter. Other clients of the API should
     * not attempt to either read or write this column. These columns are
     * editable as part of the Calendars Uri, but can only be read if accessed
     * through any other Uri.
     */
    protected interface CalendarSyncColumns {
        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC1 = "cal_sync1";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC2 = "cal_sync2";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC3 = "cal_sync3";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC4 = "cal_sync4";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC5 = "cal_sync5";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC6 = "cal_sync6";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC7 = "cal_sync7";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC8 = "cal_sync8";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC9 = "cal_sync9";

        /**
         * Generic column for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CAL_SYNC10 = "cal_sync10";
    }

    /**
     * Columns for Sync information used by Calendars and Events tables. These
     * have specific uses which are expected to be consistent by the app and
     * sync adapter.
     */
    protected interface SyncColumns extends android.provider.CalendarContract.CalendarSyncColumns {
        /**
         * The account that was used to sync the entry to the device. If the
         * account_type is not {@link #ACCOUNT_TYPE_LOCAL} then the name and
         * type must match an account on the device or the calendar will be
         * deleted.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ACCOUNT_NAME = "account_name";

        /**
         * The type of the account that was used to sync the entry to the
         * device. A type of {@link #ACCOUNT_TYPE_LOCAL} will keep this event
         * form being deleted if there are no matching accounts on the device.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ACCOUNT_TYPE = "account_type";

        /**
         * The unique ID for a row assigned by the sync source. NULL if the row
         * has never been synced. This is used as a reference id for exceptions
         * along with {@link BaseColumns#_ID}.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String _SYNC_ID = "_sync_id";

        /**
         * Used to indicate that local, unsynced, changes are present.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String DIRTY = "dirty";

        /**
         * Used in conjunction with {@link #DIRTY} to indicate what packages wrote local changes.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String MUTATORS = "mutators";

        /**
         * Whether the row has been deleted but not synced to the server. A
         * deleted row should be ignored.
         * <P>
         * Type: INTEGER (boolean)
         * </P>
         */
        public static final java.lang.String DELETED = "deleted";

        /**
         * If set to 1 this causes events on this calendar to be duplicated with
         * {@link Events#LAST_SYNCED} set to 1 whenever the event
         * transitions from non-dirty to dirty. The duplicated event will not be
         * expanded in the instances table and will only show up in sync adapter
         * queries of the events table. It will also be deleted when the
         * originating event has its dirty flag cleared by the sync adapter.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String CAN_PARTIALLY_UPDATE = "canPartiallyUpdate";
    }

    /**
     * Columns specific to the Calendars Uri that other Uris can query.
     */
    protected interface CalendarColumns {
        /**
         * The color of the calendar. This should only be updated by the sync
         * adapter, not other apps, as changing a calendar's color can adversely
         * affect its display.
         * <P>Type: INTEGER (color value)</P>
         */
        public static final java.lang.String CALENDAR_COLOR = "calendar_color";

        /**
         * A key for looking up a color from the {@link Colors} table. NULL or
         * an empty string are reserved for indicating that the calendar does
         * not use a key for looking up the color. The provider will update
         * {@link #CALENDAR_COLOR} automatically when a valid key is written to
         * this column. The key must reference an existing row of the
         * {@link Colors} table. @see Colors
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final java.lang.String CALENDAR_COLOR_KEY = "calendar_color_index";

        /**
         * The display name of the calendar. Column name.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final java.lang.String CALENDAR_DISPLAY_NAME = "calendar_displayName";

        /**
         * The level of access that the user has for the calendar
         * <P>Type: INTEGER (one of the values below)</P>
         */
        public static final java.lang.String CALENDAR_ACCESS_LEVEL = "calendar_access_level";

        /**
         * Cannot access the calendar
         */
        public static final int CAL_ACCESS_NONE = 0;

        /**
         * Can only see free/busy information about the calendar
         */
        public static final int CAL_ACCESS_FREEBUSY = 100;

        /**
         * Can read all event details
         */
        public static final int CAL_ACCESS_READ = 200;

        /**
         * Can reply yes/no/maybe to an event
         */
        public static final int CAL_ACCESS_RESPOND = 300;

        /**
         * not used
         */
        public static final int CAL_ACCESS_OVERRIDE = 400;

        /**
         * Full access to modify the calendar, but not the access control
         * settings
         */
        public static final int CAL_ACCESS_CONTRIBUTOR = 500;

        /**
         * Full access to modify the calendar, but not the access control
         * settings
         */
        public static final int CAL_ACCESS_EDITOR = 600;

        /**
         * Full access to the calendar
         */
        public static final int CAL_ACCESS_OWNER = 700;

        /**
         * Domain admin
         */
        public static final int CAL_ACCESS_ROOT = 800;

        /**
         * Is the calendar selected to be displayed?
         * 0 - do not show events associated with this calendar.
         * 1 - show events associated with this calendar
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String VISIBLE = "visible";

        /**
         * The time zone the calendar is associated with.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CALENDAR_TIME_ZONE = "calendar_timezone";

        /**
         * Is this calendar synced and are its events stored on the device?
         * 0 - Do not sync this calendar or store events for this calendar.
         * 1 - Sync down events for this calendar.
         * <p>Type: INTEGER (boolean)</p>
         */
        public static final java.lang.String SYNC_EVENTS = "sync_events";

        /**
         * The owner account for this calendar, based on the calendar feed.
         * This will be different from the _SYNC_ACCOUNT for delegated calendars.
         * Column name.
         * <P>Type: String</P>
         */
        public static final java.lang.String OWNER_ACCOUNT = "ownerAccount";

        /**
         * Can the organizer respond to the event?  If no, the status of the
         * organizer should not be shown by the UI.  Defaults to 1. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String CAN_ORGANIZER_RESPOND = "canOrganizerRespond";

        /**
         * Can the organizer modify the time zone of the event? Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String CAN_MODIFY_TIME_ZONE = "canModifyTimeZone";

        /**
         * The maximum number of reminders allowed for an event. Column name.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MAX_REMINDERS = "maxReminders";

        /**
         * A comma separated list of reminder methods supported for this
         * calendar in the format "#,#,#". Valid types are
         * {@link Reminders#METHOD_DEFAULT}, {@link Reminders#METHOD_ALERT},
         * {@link Reminders#METHOD_EMAIL}, {@link Reminders#METHOD_SMS},
         * {@link Reminders#METHOD_ALARM}. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ALLOWED_REMINDERS = "allowedReminders";

        /**
         * A comma separated list of availability types supported for this
         * calendar in the format "#,#,#". Valid types are
         * {@link Events#AVAILABILITY_BUSY}, {@link Events#AVAILABILITY_FREE},
         * {@link Events#AVAILABILITY_TENTATIVE}. Setting this field to only
         * {@link Events#AVAILABILITY_BUSY} should be used to indicate that
         * changing the availability is not supported.
         */
        public static final java.lang.String ALLOWED_AVAILABILITY = "allowedAvailability";

        /**
         * A comma separated list of attendee types supported for this calendar
         * in the format "#,#,#". Valid types are {@link Attendees#TYPE_NONE},
         * {@link Attendees#TYPE_OPTIONAL}, {@link Attendees#TYPE_REQUIRED},
         * {@link Attendees#TYPE_RESOURCE}. Setting this field to only
         * {@link Attendees#TYPE_NONE} should be used to indicate that changing
         * the attendee type is not supported.
         */
        public static final java.lang.String ALLOWED_ATTENDEE_TYPES = "allowedAttendeeTypes";

        /**
         * Is this the primary calendar for this account. If this column is not explicitly set, the
         * provider will return 1 if {@link Calendars#ACCOUNT_NAME} is equal to
         * {@link Calendars#OWNER_ACCOUNT}.
         */
        public static final java.lang.String IS_PRIMARY = "isPrimary";
    }

    /**
     * Class that represents a Calendar Entity. There is one entry per calendar.
     * This is a helper class to make batch operations easier.
     */
    public static final class CalendarEntity implements android.provider.BaseColumns , android.provider.CalendarContract.CalendarColumns , android.provider.CalendarContract.SyncColumns {
        /**
         * The default Uri used when creating a new calendar EntityIterator.
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/calendar_entities");

        /**
         * This utility class cannot be instantiated
         */
        private CalendarEntity() {
        }

        /**
         * Creates an entity iterator for the given cursor. It assumes the
         * cursor contains a calendars query.
         *
         * @param cursor
         * 		query on {@link #CONTENT_URI}
         * @return an EntityIterator of calendars
         */
        public static android.content.EntityIterator newEntityIterator(android.database.Cursor cursor) {
            return new android.provider.CalendarContract.CalendarEntity.EntityIteratorImpl(cursor);
        }

        private static class EntityIteratorImpl extends android.content.CursorEntityIterator {
            public EntityIteratorImpl(android.database.Cursor cursor) {
                super(cursor);
            }

            @java.lang.Override
            public android.content.Entity getEntityAndIncrementCursor(android.database.Cursor cursor) throws android.os.RemoteException {
                // we expect the cursor is already at the row we need to read from
                final long calendarId = cursor.getLong(cursor.getColumnIndexOrThrow(android.provider.BaseColumns._ID));
                // Create the content value
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put(android.provider.BaseColumns._ID, calendarId);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.ACCOUNT_NAME);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.ACCOUNT_TYPE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns._SYNC_ID);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.DIRTY);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.MUTATORS);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC1);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC2);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC3);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC4);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC5);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC6);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC7);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC8);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC9);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC10);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.NAME);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CALENDAR_DISPLAY_NAME);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CALENDAR_COLOR);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CALENDAR_COLOR_KEY);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarColumns.CALENDAR_ACCESS_LEVEL);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarColumns.VISIBLE);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarColumns.SYNC_EVENTS);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CALENDAR_LOCATION);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarColumns.CALENDAR_TIME_ZONE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.OWNER_ACCOUNT);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CAN_ORGANIZER_RESPOND);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.MAX_REMINDERS);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.CAN_PARTIALLY_UPDATE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.Calendars.ALLOWED_REMINDERS);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.DELETED);
                // Create the Entity from the ContentValue
                android.content.Entity entity = new android.content.Entity(cv);
                // Set cursor to next row
                cursor.moveToNext();
                // Return the created Entity
                return entity;
            }
        }
    }

    /**
     * Constants and helpers for the Calendars table, which contains details for
     * individual calendars. <h3>Operations</h3> All operations can be done
     * either as an app or as a sync adapter. To perform an operation as a sync
     * adapter {@link #CALLER_IS_SYNCADAPTER} should be set to true and
     * {@link #ACCOUNT_NAME} and {@link #ACCOUNT_TYPE} must be set in the Uri
     * parameters. See
     * {@link Uri.Builder#appendQueryParameter(java.lang.String, java.lang.String)}
     * for details on adding parameters. Sync adapters have write access to more
     * columns but are restricted to a single account at a time. Calendars are
     * designed to be primarily managed by a sync adapter and inserting new
     * calendars should be done as a sync adapter. For the most part, apps
     * should only update calendars (such as changing the color or display
     * name). If a local calendar is required an app can do so by inserting as a
     * sync adapter and using an {@link #ACCOUNT_TYPE} of
     * {@link #ACCOUNT_TYPE_LOCAL} .
     * <dl>
     * <dt><b>Insert</b></dt>
     * <dd>When inserting a new calendar the following fields must be included:
     * <ul>
     * <li>{@link #ACCOUNT_NAME}</li>
     * <li>{@link #ACCOUNT_TYPE}</li>
     * <li>{@link #NAME}</li>
     * <li>{@link #CALENDAR_DISPLAY_NAME}</li>
     * <li>{@link #CALENDAR_COLOR}</li>
     * <li>{@link #CALENDAR_ACCESS_LEVEL}</li>
     * <li>{@link #OWNER_ACCOUNT}</li>
     * </ul>
     * The following fields are not required when inserting a Calendar but are
     * generally a good idea to include:
     * <ul>
     * <li>{@link #SYNC_EVENTS} set to 1</li>
     * <li>{@link #CALENDAR_TIME_ZONE}</li>
     * <li>{@link #ALLOWED_REMINDERS}</li>
     * <li>{@link #ALLOWED_AVAILABILITY}</li>
     * <li>{@link #ALLOWED_ATTENDEE_TYPES}</li>
     * </ul>
     * <dt><b>Update</b></dt>
     * <dd>To perform an update on a calendar the {@link #_ID} of the calendar
     * should be provided either as an appended id to the Uri (
     * {@link ContentUris#withAppendedId}) or as the first selection item--the
     * selection should start with "_id=?" and the first selectionArg should be
     * the _id of the calendar. Calendars may also be updated using a selection
     * without the id. In general, the {@link #ACCOUNT_NAME} and
     * {@link #ACCOUNT_TYPE} should not be changed after a calendar is created
     * as this can cause issues for sync adapters.
     * <dt><b>Delete</b></dt>
     * <dd>Calendars can be deleted either by the {@link #_ID} as an appended id
     * on the Uri or using any standard selection. Deleting a calendar should
     * generally be handled by a sync adapter as it will remove the calendar
     * from the database and all associated data (aka events).</dd>
     * <dt><b>Query</b></dt>
     * <dd>Querying the Calendars table will get you all information about a set
     * of calendars. There will be one row returned for each calendar that
     * matches the query selection, or at most a single row if the {@link #_ID}
     * is appended to the Uri.</dd>
     * </dl>
     * <h3>Calendar Columns</h3> The following Calendar columns are writable by
     * both an app and a sync adapter.
     * <ul>
     * <li>{@link #NAME}</li>
     * <li>{@link #CALENDAR_DISPLAY_NAME}</li>
     * <li>{@link #VISIBLE}</li>
     * <li>{@link #SYNC_EVENTS}</li>
     * </ul>
     * The following Calendars columns are writable only by a sync adapter
     * <ul>
     * <li>{@link #ACCOUNT_NAME}</li>
     * <li>{@link #ACCOUNT_TYPE}</li>
     * <li>{@link #CALENDAR_COLOR}</li>
     * <li>{@link #_SYNC_ID}</li>
     * <li>{@link #DIRTY}</li>
     * <li>{@link #MUTATORS}</li>
     * <li>{@link #OWNER_ACCOUNT}</li>
     * <li>{@link #MAX_REMINDERS}</li>
     * <li>{@link #ALLOWED_REMINDERS}</li>
     * <li>{@link #ALLOWED_AVAILABILITY}</li>
     * <li>{@link #ALLOWED_ATTENDEE_TYPES}</li>
     * <li>{@link #CAN_MODIFY_TIME_ZONE}</li>
     * <li>{@link #CAN_ORGANIZER_RESPOND}</li>
     * <li>{@link #CAN_PARTIALLY_UPDATE}</li>
     * <li>{@link #CALENDAR_LOCATION}</li>
     * <li>{@link #CALENDAR_TIME_ZONE}</li>
     * <li>{@link #CALENDAR_ACCESS_LEVEL}</li>
     * <li>{@link #DELETED}</li>
     * <li>{@link #CAL_SYNC1}</li>
     * <li>{@link #CAL_SYNC2}</li>
     * <li>{@link #CAL_SYNC3}</li>
     * <li>{@link #CAL_SYNC4}</li>
     * <li>{@link #CAL_SYNC5}</li>
     * <li>{@link #CAL_SYNC6}</li>
     * <li>{@link #CAL_SYNC7}</li>
     * <li>{@link #CAL_SYNC8}</li>
     * <li>{@link #CAL_SYNC9}</li>
     * <li>{@link #CAL_SYNC10}</li>
     * </ul>
     */
    public static final class Calendars implements android.provider.BaseColumns , android.provider.CalendarContract.CalendarColumns , android.provider.CalendarContract.SyncColumns {
        /**
         * This utility class cannot be instantiated
         */
        private Calendars() {
        }

        /**
         * The content:// style URL for accessing Calendars
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/calendars");

        /**
         * The default sort order for this table
         */
        public static final java.lang.String DEFAULT_SORT_ORDER = android.provider.CalendarContract.CalendarColumns.CALENDAR_DISPLAY_NAME;

        /**
         * The name of the calendar. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String NAME = "name";

        /**
         * The default location for the calendar. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CALENDAR_LOCATION = "calendar_location";

        /**
         * These fields are only writable by a sync adapter. To modify them the
         * caller must include {@link #CALLER_IS_SYNCADAPTER},
         * {@link #ACCOUNT_NAME}, and {@link #ACCOUNT_TYPE} in the Uri's query
         * parameters. TODO move to provider
         *
         * @unknown 
         */
        public static final java.lang.String[] SYNC_WRITABLE_COLUMNS = new java.lang.String[]{ android.provider.CalendarContract.SyncColumns.ACCOUNT_NAME, android.provider.CalendarContract.SyncColumns.ACCOUNT_TYPE, android.provider.CalendarContract.SyncColumns._SYNC_ID, android.provider.CalendarContract.SyncColumns.DIRTY, android.provider.CalendarContract.SyncColumns.MUTATORS, android.provider.CalendarContract.CalendarColumns.OWNER_ACCOUNT, android.provider.CalendarContract.CalendarColumns.MAX_REMINDERS, android.provider.CalendarContract.CalendarColumns.ALLOWED_REMINDERS, android.provider.CalendarContract.CalendarColumns.CAN_MODIFY_TIME_ZONE, android.provider.CalendarContract.CalendarColumns.CAN_ORGANIZER_RESPOND, android.provider.CalendarContract.SyncColumns.CAN_PARTIALLY_UPDATE, android.provider.CalendarContract.Calendars.CALENDAR_LOCATION, android.provider.CalendarContract.CalendarColumns.CALENDAR_TIME_ZONE, android.provider.CalendarContract.CalendarColumns.CALENDAR_ACCESS_LEVEL, android.provider.CalendarContract.SyncColumns.DELETED, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC1, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC2, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC3, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC4, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC5, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC6, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC7, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC8, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC9, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC10 };
    }

    /**
     * Columns from the Attendees table that other tables join into themselves.
     */
    protected interface AttendeesColumns {
        /**
         * The id of the event. Column name.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String EVENT_ID = "event_id";

        /**
         * The name of the attendee. Column name.
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ATTENDEE_NAME = "attendeeName";

        /**
         * The email address of the attendee. Column name.
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ATTENDEE_EMAIL = "attendeeEmail";

        /**
         * The relationship of the attendee to the user. Column name.
         * <P>Type: INTEGER (one of {@link #RELATIONSHIP_ATTENDEE}, ...}.</P>
         */
        public static final java.lang.String ATTENDEE_RELATIONSHIP = "attendeeRelationship";

        public static final int RELATIONSHIP_NONE = 0;

        public static final int RELATIONSHIP_ATTENDEE = 1;

        public static final int RELATIONSHIP_ORGANIZER = 2;

        public static final int RELATIONSHIP_PERFORMER = 3;

        public static final int RELATIONSHIP_SPEAKER = 4;

        /**
         * The type of attendee. Column name.
         * <P>
         * Type: Integer (one of {@link #TYPE_NONE}, {@link #TYPE_REQUIRED},
         * {@link #TYPE_OPTIONAL}, {@link #TYPE_RESOURCE})
         * </P>
         */
        public static final java.lang.String ATTENDEE_TYPE = "attendeeType";

        public static final int TYPE_NONE = 0;

        public static final int TYPE_REQUIRED = 1;

        public static final int TYPE_OPTIONAL = 2;

        /**
         * This specifies that an attendee is a resource, like a room, a
         * cabbage, or something and not an actual person.
         */
        public static final int TYPE_RESOURCE = 3;

        /**
         * The attendance status of the attendee. Column name.
         * <P>Type: Integer (one of {@link #ATTENDEE_STATUS_ACCEPTED}, ...).</P>
         */
        public static final java.lang.String ATTENDEE_STATUS = "attendeeStatus";

        public static final int ATTENDEE_STATUS_NONE = 0;

        public static final int ATTENDEE_STATUS_ACCEPTED = 1;

        public static final int ATTENDEE_STATUS_DECLINED = 2;

        public static final int ATTENDEE_STATUS_INVITED = 3;

        public static final int ATTENDEE_STATUS_TENTATIVE = 4;

        /**
         * The identity of the attendee as referenced in
         * {@link ContactsContract.CommonDataKinds.Identity#IDENTITY}.
         * This is required only if {@link #ATTENDEE_ID_NAMESPACE} is present. Column name.
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ATTENDEE_IDENTITY = "attendeeIdentity";

        /**
         * The identity name space of the attendee as referenced in
         * {@link ContactsContract.CommonDataKinds.Identity#NAMESPACE}.
         * This is required only if {@link #ATTENDEE_IDENTITY} is present. Column name.
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ATTENDEE_ID_NAMESPACE = "attendeeIdNamespace";
    }

    /**
     * Fields and helpers for interacting with Attendees. Each row of this table
     * represents a single attendee or guest of an event. Calling
     * {@link #query(ContentResolver, long, String[])} will return a list of attendees for
     * the event with the given eventId. Both apps and sync adapters may write
     * to this table. There are six writable fields and all of them except
     * {@link #ATTENDEE_NAME} must be included when inserting a new attendee.
     * They are:
     * <ul>
     * <li>{@link #EVENT_ID}</li>
     * <li>{@link #ATTENDEE_NAME}</li>
     * <li>{@link #ATTENDEE_EMAIL}</li>
     * <li>{@link #ATTENDEE_RELATIONSHIP}</li>
     * <li>{@link #ATTENDEE_TYPE}</li>
     * <li>{@link #ATTENDEE_STATUS}</li>
     * <li>{@link #ATTENDEE_IDENTITY}</li>
     * <li>{@link #ATTENDEE_ID_NAMESPACE}</li>
     * </ul>
     */
    public static final class Attendees implements android.provider.BaseColumns , android.provider.CalendarContract.AttendeesColumns , android.provider.CalendarContract.EventsColumns {
        /**
         * The content:// style URL for accessing Attendees data
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/attendees");

        private static final java.lang.String ATTENDEES_WHERE = android.provider.CalendarContract.Attendees.EVENT_ID + "=?";

        /**
         * This utility class cannot be instantiated
         */
        private Attendees() {
        }

        /**
         * Queries all attendees associated with the given event. This is a
         * blocking call and should not be done on the UI thread.
         *
         * @param cr
         * 		The content resolver to use for the query
         * @param eventId
         * 		The id of the event to retrieve attendees for
         * @param projection
         * 		the columns to return in the cursor
         * @return A Cursor containing all attendees for the event
         */
        public static final android.database.Cursor query(android.content.ContentResolver cr, long eventId, java.lang.String[] projection) {
            java.lang.String[] attArgs = new java.lang.String[]{ java.lang.Long.toString(eventId) };
            return /* selection args */
            /* sort order */
            cr.query(android.provider.CalendarContract.Attendees.CONTENT_URI, projection, android.provider.CalendarContract.Attendees.ATTENDEES_WHERE, attArgs, null);
        }
    }

    /**
     * Columns from the Events table that other tables join into themselves.
     */
    protected interface EventsColumns {
        /**
         * The {@link Calendars#_ID} of the calendar the event belongs to.
         * Column name.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String CALENDAR_ID = "calendar_id";

        /**
         * The title of the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String TITLE = "title";

        /**
         * The description of the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String DESCRIPTION = "description";

        /**
         * Where the event takes place. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String EVENT_LOCATION = "eventLocation";

        /**
         * A secondary color for the individual event. This should only be
         * updated by the sync adapter for a given account.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String EVENT_COLOR = "eventColor";

        /**
         * A secondary color key for the individual event. NULL or an empty
         * string are reserved for indicating that the event does not use a key
         * for looking up the color. The provider will update
         * {@link #EVENT_COLOR} automatically when a valid key is written to
         * this column. The key must reference an existing row of the
         * {@link Colors} table. @see Colors
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final java.lang.String EVENT_COLOR_KEY = "eventColor_index";

        /**
         * This will be {@link #EVENT_COLOR} if it is not null; otherwise, this will be
         * {@link Calendars#CALENDAR_COLOR}.
         * Read-only value. To modify, write to {@link #EVENT_COLOR} or
         * {@link Calendars#CALENDAR_COLOR} directly.
         * <P>
         *     Type: INTEGER
         * </P>
         */
        public static final java.lang.String DISPLAY_COLOR = "displayColor";

        /**
         * The event status. Column name.
         * <P>Type: INTEGER (one of {@link #STATUS_TENTATIVE}...)</P>
         */
        public static final java.lang.String STATUS = "eventStatus";

        public static final int STATUS_TENTATIVE = 0;

        public static final int STATUS_CONFIRMED = 1;

        public static final int STATUS_CANCELED = 2;

        /**
         * This is a copy of the attendee status for the owner of this event.
         * This field is copied here so that we can efficiently filter out
         * events that are declined without having to look in the Attendees
         * table. Column name.
         *
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String SELF_ATTENDEE_STATUS = "selfAttendeeStatus";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA1 = "sync_data1";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA2 = "sync_data2";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA3 = "sync_data3";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA4 = "sync_data4";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA5 = "sync_data5";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA6 = "sync_data6";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA7 = "sync_data7";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA8 = "sync_data8";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA9 = "sync_data9";

        /**
         * This column is available for use by sync adapters. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SYNC_DATA10 = "sync_data10";

        /**
         * Used to indicate that a row is not a real event but an original copy of a locally
         * modified event. A copy is made when an event changes from non-dirty to dirty and the
         * event is on a calendar with {@link Calendars#CAN_PARTIALLY_UPDATE} set to 1. This copy
         * does not get expanded in the instances table and is only visible in queries made by a
         * sync adapter. The copy gets removed when the event is changed back to non-dirty by a
         * sync adapter.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String LAST_SYNCED = "lastSynced";

        /**
         * The time the event starts in UTC millis since epoch. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String DTSTART = "dtstart";

        /**
         * The time the event ends in UTC millis since epoch. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String DTEND = "dtend";

        /**
         * The duration of the event in RFC2445 format. Column name.
         * <P>Type: TEXT (duration in RFC2445 format)</P>
         */
        public static final java.lang.String DURATION = "duration";

        /**
         * The timezone for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String EVENT_TIMEZONE = "eventTimezone";

        /**
         * The timezone for the end time of the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String EVENT_END_TIMEZONE = "eventEndTimezone";

        /**
         * Is the event all day (time zone independent). Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String ALL_DAY = "allDay";

        /**
         * Defines how the event shows up for others when the calendar is
         * shared. Column name.
         * <P>Type: INTEGER (One of {@link #ACCESS_DEFAULT}, ...)</P>
         */
        public static final java.lang.String ACCESS_LEVEL = "accessLevel";

        /**
         * Default access is controlled by the server and will be treated as
         * public on the device.
         */
        public static final int ACCESS_DEFAULT = 0;

        /**
         * Confidential is not used by the app.
         */
        public static final int ACCESS_CONFIDENTIAL = 1;

        /**
         * Private shares the event as a free/busy slot with no details.
         */
        public static final int ACCESS_PRIVATE = 2;

        /**
         * Public makes the contents visible to anyone with access to the
         * calendar.
         */
        public static final int ACCESS_PUBLIC = 3;

        /**
         * If this event counts as busy time or is still free time that can be
         * scheduled over. Column name.
         * <P>
         * Type: INTEGER (One of {@link #AVAILABILITY_BUSY},
         * {@link #AVAILABILITY_FREE}, {@link #AVAILABILITY_TENTATIVE})
         * </P>
         */
        public static final java.lang.String AVAILABILITY = "availability";

        /**
         * Indicates that this event takes up time and will conflict with other
         * events.
         */
        public static final int AVAILABILITY_BUSY = 0;

        /**
         * Indicates that this event is free time and will not conflict with
         * other events.
         */
        public static final int AVAILABILITY_FREE = 1;

        /**
         * Indicates that the owner's availability may change, but should be
         * considered busy time that will conflict.
         */
        public static final int AVAILABILITY_TENTATIVE = 2;

        /**
         * Whether the event has an alarm or not. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String HAS_ALARM = "hasAlarm";

        /**
         * Whether the event has extended properties or not. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String HAS_EXTENDED_PROPERTIES = "hasExtendedProperties";

        /**
         * The recurrence rule for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String RRULE = "rrule";

        /**
         * The recurrence dates for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String RDATE = "rdate";

        /**
         * The recurrence exception rule for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String EXRULE = "exrule";

        /**
         * The recurrence exception dates for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String EXDATE = "exdate";

        /**
         * The {@link Events#_ID} of the original recurring event for which this
         * event is an exception. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ORIGINAL_ID = "original_id";

        /**
         * The _sync_id of the original recurring event for which this event is
         * an exception. The provider should keep the original_id in sync when
         * this is updated. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ORIGINAL_SYNC_ID = "original_sync_id";

        /**
         * The original instance time of the recurring event for which this
         * event is an exception. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String ORIGINAL_INSTANCE_TIME = "originalInstanceTime";

        /**
         * The allDay status (true or false) of the original recurring event
         * for which this event is an exception. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String ORIGINAL_ALL_DAY = "originalAllDay";

        /**
         * The last date this event repeats on, or NULL if it never ends. Column
         * name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String LAST_DATE = "lastDate";

        /**
         * Whether the event has attendee information.  True if the event
         * has full attendee data, false if the event has information about
         * self only. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String HAS_ATTENDEE_DATA = "hasAttendeeData";

        /**
         * Whether guests can modify the event. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String GUESTS_CAN_MODIFY = "guestsCanModify";

        /**
         * Whether guests can invite other guests. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";

        /**
         * Whether guests can see the list of attendees. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String GUESTS_CAN_SEE_GUESTS = "guestsCanSeeGuests";

        /**
         * Email of the organizer (owner) of the event. Column name.
         * <P>Type: STRING</P>
         */
        public static final java.lang.String ORGANIZER = "organizer";

        /**
         * Are we the organizer of this event. If this column is not explicitly set, the provider
         * will return 1 if {@link #ORGANIZER} is equal to {@link Calendars#OWNER_ACCOUNT}.
         * Column name.
         * <P>Type: STRING</P>
         */
        public static final java.lang.String IS_ORGANIZER = "isOrganizer";

        /**
         * Whether the user can invite others to the event. The
         * GUESTS_CAN_INVITE_OTHERS is a setting that applies to an arbitrary
         * guest, while CAN_INVITE_OTHERS indicates if the user can invite
         * others (either through GUESTS_CAN_INVITE_OTHERS or because the user
         * has modify access to the event). Column name.
         * <P>Type: INTEGER (boolean, readonly)</P>
         */
        public static final java.lang.String CAN_INVITE_OTHERS = "canInviteOthers";

        /**
         * The package name of the custom app that can provide a richer
         * experience for the event. See the ACTION TYPE
         * {@link CalendarContract#ACTION_HANDLE_CUSTOM_EVENT} for details.
         * Column name.
         * <P> Type: TEXT </P>
         */
        public static final java.lang.String CUSTOM_APP_PACKAGE = "customAppPackage";

        /**
         * The URI used by the custom app for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CUSTOM_APP_URI = "customAppUri";

        /**
         * The UID for events added from the RFC 2445 iCalendar format.
         * Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String UID_2445 = "uid2445";
    }

    /**
     * Class that represents an Event Entity. There is one entry per event.
     * Recurring events show up as a single entry. This is a helper class to
     * make batch operations easier. A {@link ContentResolver} or
     * {@link ContentProviderClient} is required as the helper does additional
     * queries to add reminders and attendees to each entry.
     */
    public static final class EventsEntity implements android.provider.BaseColumns , android.provider.CalendarContract.EventsColumns , android.provider.CalendarContract.SyncColumns {
        /**
         * The content:// style URL for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/event_entities");

        /**
         * This utility class cannot be instantiated
         */
        private EventsEntity() {
        }

        /**
         * Creates a new iterator for events
         *
         * @param cursor
         * 		An event query
         * @param resolver
         * 		For performing additional queries
         * @return an EntityIterator containing one entity per event in the
        cursor
         */
        public static android.content.EntityIterator newEntityIterator(android.database.Cursor cursor, android.content.ContentResolver resolver) {
            return new android.provider.CalendarContract.EventsEntity.EntityIteratorImpl(cursor, resolver);
        }

        /**
         * Creates a new iterator for events
         *
         * @param cursor
         * 		An event query
         * @param provider
         * 		For performing additional queries
         * @return an EntityIterator containing one entity per event in the
        cursor
         */
        public static android.content.EntityIterator newEntityIterator(android.database.Cursor cursor, android.content.ContentProviderClient provider) {
            return new android.provider.CalendarContract.EventsEntity.EntityIteratorImpl(cursor, provider);
        }

        private static class EntityIteratorImpl extends android.content.CursorEntityIterator {
            private final android.content.ContentResolver mResolver;

            private final android.content.ContentProviderClient mProvider;

            private static final java.lang.String[] REMINDERS_PROJECTION = new java.lang.String[]{ android.provider.CalendarContract.Reminders.MINUTES, android.provider.CalendarContract.Reminders.METHOD };

            private static final int COLUMN_MINUTES = 0;

            private static final int COLUMN_METHOD = 1;

            private static final java.lang.String[] ATTENDEES_PROJECTION = new java.lang.String[]{ android.provider.CalendarContract.Attendees.ATTENDEE_NAME, android.provider.CalendarContract.Attendees.ATTENDEE_EMAIL, android.provider.CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, android.provider.CalendarContract.Attendees.ATTENDEE_TYPE, android.provider.CalendarContract.Attendees.ATTENDEE_STATUS, android.provider.CalendarContract.Attendees.ATTENDEE_IDENTITY, android.provider.CalendarContract.Attendees.ATTENDEE_ID_NAMESPACE };

            private static final int COLUMN_ATTENDEE_NAME = 0;

            private static final int COLUMN_ATTENDEE_EMAIL = 1;

            private static final int COLUMN_ATTENDEE_RELATIONSHIP = 2;

            private static final int COLUMN_ATTENDEE_TYPE = 3;

            private static final int COLUMN_ATTENDEE_STATUS = 4;

            private static final int COLUMN_ATTENDEE_IDENTITY = 5;

            private static final int COLUMN_ATTENDEE_ID_NAMESPACE = 6;

            private static final java.lang.String[] EXTENDED_PROJECTION = new java.lang.String[]{ android.provider.CalendarContract.ExtendedProperties._ID, android.provider.CalendarContract.ExtendedProperties.NAME, android.provider.CalendarContract.ExtendedProperties.VALUE };

            private static final int COLUMN_ID = 0;

            private static final int COLUMN_NAME = 1;

            private static final int COLUMN_VALUE = 2;

            private static final java.lang.String WHERE_EVENT_ID = "event_id=?";

            public EntityIteratorImpl(android.database.Cursor cursor, android.content.ContentResolver resolver) {
                super(cursor);
                mResolver = resolver;
                mProvider = null;
            }

            public EntityIteratorImpl(android.database.Cursor cursor, android.content.ContentProviderClient provider) {
                super(cursor);
                mResolver = null;
                mProvider = provider;
            }

            @java.lang.Override
            public android.content.Entity getEntityAndIncrementCursor(android.database.Cursor cursor) throws android.os.RemoteException {
                // we expect the cursor is already at the row we need to read from
                final long eventId = cursor.getLong(cursor.getColumnIndexOrThrow(android.provider.CalendarContract.Events._ID));
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put(android.provider.CalendarContract.Events._ID, eventId);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.CALENDAR_ID);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.TITLE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.DESCRIPTION);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EVENT_LOCATION);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.STATUS);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SELF_ATTENDEE_STATUS);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.DTSTART);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.DTEND);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.DURATION);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EVENT_TIMEZONE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EVENT_END_TIMEZONE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ALL_DAY);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ACCESS_LEVEL);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.AVAILABILITY);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EVENT_COLOR);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EVENT_COLOR_KEY);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.HAS_ALARM);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.HAS_EXTENDED_PROPERTIES);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.RRULE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.RDATE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EXRULE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.EXDATE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ORIGINAL_SYNC_ID);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ORIGINAL_ID);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ORIGINAL_INSTANCE_TIME);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ORIGINAL_ALL_DAY);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.LAST_DATE);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.HAS_ATTENDEE_DATA);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.GUESTS_CAN_INVITE_OTHERS);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.GUESTS_CAN_MODIFY);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.GUESTS_CAN_SEE_GUESTS);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.CUSTOM_APP_PACKAGE);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.CUSTOM_APP_URI);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.UID_2445);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.ORGANIZER);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.IS_ORGANIZER);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns._SYNC_ID);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.DIRTY);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.MUTATORS);
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.LAST_SYNCED);
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.SyncColumns.DELETED);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA1);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA2);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA3);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA4);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA5);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA6);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA7);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA8);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA9);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.EventsColumns.SYNC_DATA10);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC1);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC2);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC3);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC4);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC5);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC6);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC7);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC8);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC9);
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC10);
                android.content.Entity entity = new android.content.Entity(cv);
                android.database.Cursor subCursor;
                if (mResolver != null) {
                    subCursor = /* selectionArgs */
                    /* sortOrder */
                    mResolver.query(android.provider.CalendarContract.Reminders.CONTENT_URI, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.REMINDERS_PROJECTION, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.WHERE_EVENT_ID, new java.lang.String[]{ java.lang.Long.toString(eventId) }, null);
                } else {
                    subCursor = /* selectionArgs */
                    /* sortOrder */
                    mProvider.query(android.provider.CalendarContract.Reminders.CONTENT_URI, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.REMINDERS_PROJECTION, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.WHERE_EVENT_ID, new java.lang.String[]{ java.lang.Long.toString(eventId) }, null);
                }
                try {
                    while (subCursor.moveToNext()) {
                        android.content.ContentValues reminderValues = new android.content.ContentValues();
                        reminderValues.put(android.provider.CalendarContract.Reminders.MINUTES, subCursor.getInt(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_MINUTES));
                        reminderValues.put(android.provider.CalendarContract.Reminders.METHOD, subCursor.getInt(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_METHOD));
                        entity.addSubValue(android.provider.CalendarContract.Reminders.CONTENT_URI, reminderValues);
                    } 
                } finally {
                    subCursor.close();
                }
                if (mResolver != null) {
                    subCursor = /* selectionArgs */
                    /* sortOrder */
                    mResolver.query(android.provider.CalendarContract.Attendees.CONTENT_URI, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.ATTENDEES_PROJECTION, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.WHERE_EVENT_ID, new java.lang.String[]{ java.lang.Long.toString(eventId) }, null);
                } else {
                    subCursor = /* selectionArgs */
                    /* sortOrder */
                    mProvider.query(android.provider.CalendarContract.Attendees.CONTENT_URI, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.ATTENDEES_PROJECTION, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.WHERE_EVENT_ID, new java.lang.String[]{ java.lang.Long.toString(eventId) }, null);
                }
                try {
                    while (subCursor.moveToNext()) {
                        android.content.ContentValues attendeeValues = new android.content.ContentValues();
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_NAME, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_NAME));
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_EMAIL, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_EMAIL));
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, subCursor.getInt(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_RELATIONSHIP));
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_TYPE, subCursor.getInt(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_TYPE));
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_STATUS, subCursor.getInt(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_STATUS));
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_IDENTITY, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_IDENTITY));
                        attendeeValues.put(android.provider.CalendarContract.Attendees.ATTENDEE_ID_NAMESPACE, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ATTENDEE_ID_NAMESPACE));
                        entity.addSubValue(android.provider.CalendarContract.Attendees.CONTENT_URI, attendeeValues);
                    } 
                } finally {
                    subCursor.close();
                }
                if (mResolver != null) {
                    subCursor = /* selectionArgs */
                    /* sortOrder */
                    mResolver.query(android.provider.CalendarContract.ExtendedProperties.CONTENT_URI, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.EXTENDED_PROJECTION, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.WHERE_EVENT_ID, new java.lang.String[]{ java.lang.Long.toString(eventId) }, null);
                } else {
                    subCursor = /* selectionArgs */
                    /* sortOrder */
                    mProvider.query(android.provider.CalendarContract.ExtendedProperties.CONTENT_URI, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.EXTENDED_PROJECTION, android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.WHERE_EVENT_ID, new java.lang.String[]{ java.lang.Long.toString(eventId) }, null);
                }
                try {
                    while (subCursor.moveToNext()) {
                        android.content.ContentValues extendedValues = new android.content.ContentValues();
                        extendedValues.put(android.provider.CalendarContract.ExtendedProperties._ID, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_ID));
                        extendedValues.put(android.provider.CalendarContract.ExtendedProperties.NAME, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_NAME));
                        extendedValues.put(android.provider.CalendarContract.ExtendedProperties.VALUE, subCursor.getString(android.provider.CalendarContract.EventsEntity.EntityIteratorImpl.COLUMN_VALUE));
                        entity.addSubValue(android.provider.CalendarContract.ExtendedProperties.CONTENT_URI, extendedValues);
                    } 
                } finally {
                    subCursor.close();
                }
                cursor.moveToNext();
                return entity;
            }
        }
    }

    /**
     * Constants and helpers for the Events table, which contains details for
     * individual events. <h3>Operations</h3> All operations can be done either
     * as an app or as a sync adapter. To perform an operation as a sync adapter
     * {@link #CALLER_IS_SYNCADAPTER} should be set to true and
     * {@link #ACCOUNT_NAME} and {@link #ACCOUNT_TYPE} must be set in the Uri
     * parameters. See
     * {@link Uri.Builder#appendQueryParameter(java.lang.String, java.lang.String)}
     * for details on adding parameters. Sync adapters have write access to more
     * columns but are restricted to a single account at a time.
     * <dl>
     * <dt><b>Insert</b></dt>
     * <dd>When inserting a new event the following fields must be included:
     * <ul>
     * <li>dtstart</li>
     * <li>dtend if the event is non-recurring</li>
     * <li>duration if the event is recurring</li>
     * <li>rrule or rdate if the event is recurring</li>
     * <li>eventTimezone</li>
     * <li>a calendar_id</li>
     * </ul>
     * There are also further requirements when inserting or updating an event.
     * See the section on Writing to Events.</dd>
     * <dt><b>Update</b></dt>
     * <dd>To perform an update of an Event the {@link Events#_ID} of the event
     * should be provided either as an appended id to the Uri (
     * {@link ContentUris#withAppendedId}) or as the first selection item--the
     * selection should start with "_id=?" and the first selectionArg should be
     * the _id of the event. Updates may also be done using a selection and no
     * id. Updating an event must respect the same rules as inserting and is
     * further restricted in the fields that can be written. See the section on
     * Writing to Events.</dd>
     * <dt><b>Delete</b></dt>
     * <dd>Events can be deleted either by the {@link Events#_ID} as an appended
     * id on the Uri or using any standard selection. If an appended id is used
     * a selection is not allowed. There are two versions of delete: as an app
     * and as a sync adapter. An app delete will set the deleted column on an
     * event and remove all instances of that event. A sync adapter delete will
     * remove the event from the database and all associated data.</dd>
     * <dt><b>Query</b></dt>
     * <dd>Querying the Events table will get you all information about a set of
     * events except their reminders, attendees, and extended properties. There
     * will be one row returned for each event that matches the query selection,
     * or at most a single row if the {@link Events#_ID} is appended to the Uri.
     * Recurring events will only return a single row regardless of the number
     * of times that event repeats.</dd>
     * </dl>
     * <h3>Writing to Events</h3> There are further restrictions on all Updates
     * and Inserts in the Events table:
     * <ul>
     * <li>If allDay is set to 1 eventTimezone must be {@link Time#TIMEZONE_UTC}
     * and the time must correspond to a midnight boundary.</li>
     * <li>Exceptions are not allowed to recur. If rrule or rdate is not empty,
     * original_id and original_sync_id must be empty.</li>
     * <li>In general a calendar_id should not be modified after insertion. This
     * is not explicitly forbidden but many sync adapters will not behave in an
     * expected way if the calendar_id is modified.</li>
     * </ul>
     * The following Events columns are writable by both an app and a sync
     * adapter.
     * <ul>
     * <li>{@link #CALENDAR_ID}</li>
     * <li>{@link #ORGANIZER}</li>
     * <li>{@link #TITLE}</li>
     * <li>{@link #EVENT_LOCATION}</li>
     * <li>{@link #DESCRIPTION}</li>
     * <li>{@link #EVENT_COLOR}</li>
     * <li>{@link #DTSTART}</li>
     * <li>{@link #DTEND}</li>
     * <li>{@link #EVENT_TIMEZONE}</li>
     * <li>{@link #EVENT_END_TIMEZONE}</li>
     * <li>{@link #DURATION}</li>
     * <li>{@link #ALL_DAY}</li>
     * <li>{@link #RRULE}</li>
     * <li>{@link #RDATE}</li>
     * <li>{@link #EXRULE}</li>
     * <li>{@link #EXDATE}</li>
     * <li>{@link #ORIGINAL_ID}</li>
     * <li>{@link #ORIGINAL_SYNC_ID}</li>
     * <li>{@link #ORIGINAL_INSTANCE_TIME}</li>
     * <li>{@link #ORIGINAL_ALL_DAY}</li>
     * <li>{@link #ACCESS_LEVEL}</li>
     * <li>{@link #AVAILABILITY}</li>
     * <li>{@link #GUESTS_CAN_MODIFY}</li>
     * <li>{@link #GUESTS_CAN_INVITE_OTHERS}</li>
     * <li>{@link #GUESTS_CAN_SEE_GUESTS}</li>
     * <li>{@link #CUSTOM_APP_PACKAGE}</li>
     * <li>{@link #CUSTOM_APP_URI}</li>
     * <li>{@link #UID_2445}</li>
     * </ul>
     * The following Events columns are writable only by a sync adapter
     * <ul>
     * <li>{@link #DIRTY}</li>
     * <li>{@link #MUTATORS}</li>
     * <li>{@link #_SYNC_ID}</li>
     * <li>{@link #SYNC_DATA1}</li>
     * <li>{@link #SYNC_DATA2}</li>
     * <li>{@link #SYNC_DATA3}</li>
     * <li>{@link #SYNC_DATA4}</li>
     * <li>{@link #SYNC_DATA5}</li>
     * <li>{@link #SYNC_DATA6}</li>
     * <li>{@link #SYNC_DATA7}</li>
     * <li>{@link #SYNC_DATA8}</li>
     * <li>{@link #SYNC_DATA9}</li>
     * <li>{@link #SYNC_DATA10}</li>
     * </ul>
     * The remaining columns are either updated by the provider only or are
     * views into other tables and cannot be changed through the Events table.
     */
    public static final class Events implements android.provider.BaseColumns , android.provider.CalendarContract.CalendarColumns , android.provider.CalendarContract.EventsColumns , android.provider.CalendarContract.SyncColumns {
        /**
         * The content:// style URL for interacting with events. Appending an
         * event id using {@link ContentUris#withAppendedId(Uri, long)} will
         * specify a single event.
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/events");

        /**
         * The content:// style URI for recurring event exceptions.  Insertions require an
         * appended event ID.  Deletion of exceptions requires both the original event ID and
         * the exception event ID (see {@link Uri.Builder#appendPath}).
         */
        public static final android.net.Uri CONTENT_EXCEPTION_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/exception");

        /**
         * This utility class cannot be instantiated
         */
        private Events() {
        }

        /**
         * The default sort order for this table
         */
        private static final java.lang.String DEFAULT_SORT_ORDER = "";

        /**
         * These are columns that should only ever be updated by the provider,
         * either because they are views mapped to another table or because they
         * are used for provider only functionality. TODO move to provider
         *
         * @unknown 
         */
        public static java.lang.String[] PROVIDER_WRITABLE_COLUMNS = new java.lang.String[]{ android.provider.CalendarContract.SyncColumns.ACCOUNT_NAME, android.provider.CalendarContract.SyncColumns.ACCOUNT_TYPE, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC1, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC2, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC3, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC4, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC5, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC6, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC7, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC8, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC9, android.provider.CalendarContract.CalendarSyncColumns.CAL_SYNC10, android.provider.CalendarContract.CalendarColumns.ALLOWED_REMINDERS, android.provider.CalendarContract.CalendarColumns.ALLOWED_ATTENDEE_TYPES, android.provider.CalendarContract.CalendarColumns.ALLOWED_AVAILABILITY, android.provider.CalendarContract.CalendarColumns.CALENDAR_ACCESS_LEVEL, android.provider.CalendarContract.CalendarColumns.CALENDAR_COLOR, android.provider.CalendarContract.CalendarColumns.CALENDAR_TIME_ZONE, android.provider.CalendarContract.CalendarColumns.CAN_MODIFY_TIME_ZONE, android.provider.CalendarContract.CalendarColumns.CAN_ORGANIZER_RESPOND, android.provider.CalendarContract.CalendarColumns.CALENDAR_DISPLAY_NAME, android.provider.CalendarContract.SyncColumns.CAN_PARTIALLY_UPDATE, android.provider.CalendarContract.CalendarColumns.SYNC_EVENTS, android.provider.CalendarContract.CalendarColumns.VISIBLE };

        /**
         * These fields are only writable by a sync adapter. To modify them the
         * caller must include CALLER_IS_SYNCADAPTER, _SYNC_ACCOUNT, and
         * _SYNC_ACCOUNT_TYPE in the query parameters. TODO move to provider.
         *
         * @unknown 
         */
        public static final java.lang.String[] SYNC_WRITABLE_COLUMNS = new java.lang.String[]{ android.provider.CalendarContract.SyncColumns._SYNC_ID, android.provider.CalendarContract.SyncColumns.DIRTY, android.provider.CalendarContract.SyncColumns.MUTATORS, android.provider.CalendarContract.EventsColumns.SYNC_DATA1, android.provider.CalendarContract.EventsColumns.SYNC_DATA2, android.provider.CalendarContract.EventsColumns.SYNC_DATA3, android.provider.CalendarContract.EventsColumns.SYNC_DATA4, android.provider.CalendarContract.EventsColumns.SYNC_DATA5, android.provider.CalendarContract.EventsColumns.SYNC_DATA6, android.provider.CalendarContract.EventsColumns.SYNC_DATA7, android.provider.CalendarContract.EventsColumns.SYNC_DATA8, android.provider.CalendarContract.EventsColumns.SYNC_DATA9, android.provider.CalendarContract.EventsColumns.SYNC_DATA10 };
    }

    /**
     * Fields and helpers for interacting with Instances. An instance is a
     * single occurrence of an event including time zone specific start and end
     * days and minutes. The instances table is not writable and only provides a
     * way to query event occurrences.
     */
    public static final class Instances implements android.provider.BaseColumns , android.provider.CalendarContract.CalendarColumns , android.provider.CalendarContract.EventsColumns {
        private static final java.lang.String WHERE_CALENDARS_SELECTED = android.provider.CalendarContract.Calendars.VISIBLE + "=?";

        private static final java.lang.String[] WHERE_CALENDARS_ARGS = new java.lang.String[]{ "1" };

        /**
         * This utility class cannot be instantiated
         */
        private Instances() {
        }

        /**
         * Performs a query to return all visible instances in the given range.
         * This is a blocking function and should not be done on the UI thread.
         * This will cause an expansion of recurring events to fill this time
         * range if they are not already expanded and will slow down for larger
         * time ranges with many recurring events.
         *
         * @param cr
         * 		The ContentResolver to use for the query
         * @param projection
         * 		The columns to return
         * @param begin
         * 		The start of the time range to query in UTC millis since
         * 		epoch
         * @param end
         * 		The end of the time range to query in UTC millis since
         * 		epoch
         * @return A Cursor containing all instances in the given range
         */
        public static final android.database.Cursor query(android.content.ContentResolver cr, java.lang.String[] projection, long begin, long end) {
            android.net.Uri.Builder builder = android.provider.CalendarContract.Instances.CONTENT_URI.buildUpon();
            android.content.ContentUris.appendId(builder, begin);
            android.content.ContentUris.appendId(builder, end);
            return cr.query(builder.build(), projection, android.provider.CalendarContract.Instances.WHERE_CALENDARS_SELECTED, android.provider.CalendarContract.Instances.WHERE_CALENDARS_ARGS, android.provider.CalendarContract.Instances.DEFAULT_SORT_ORDER);
        }

        /**
         * Performs a query to return all visible instances in the given range
         * that match the given query. This is a blocking function and should
         * not be done on the UI thread. This will cause an expansion of
         * recurring events to fill this time range if they are not already
         * expanded and will slow down for larger time ranges with many
         * recurring events.
         *
         * @param cr
         * 		The ContentResolver to use for the query
         * @param projection
         * 		The columns to return
         * @param begin
         * 		The start of the time range to query in UTC millis since
         * 		epoch
         * @param end
         * 		The end of the time range to query in UTC millis since
         * 		epoch
         * @param searchQuery
         * 		A string of space separated search terms. Segments
         * 		enclosed by double quotes will be treated as a single
         * 		term.
         * @return A Cursor of instances matching the search terms in the given
        time range
         */
        public static final android.database.Cursor query(android.content.ContentResolver cr, java.lang.String[] projection, long begin, long end, java.lang.String searchQuery) {
            android.net.Uri.Builder builder = android.provider.CalendarContract.Instances.CONTENT_SEARCH_URI.buildUpon();
            android.content.ContentUris.appendId(builder, begin);
            android.content.ContentUris.appendId(builder, end);
            builder = builder.appendPath(searchQuery);
            return cr.query(builder.build(), projection, android.provider.CalendarContract.Instances.WHERE_CALENDARS_SELECTED, android.provider.CalendarContract.Instances.WHERE_CALENDARS_ARGS, android.provider.CalendarContract.Instances.DEFAULT_SORT_ORDER);
        }

        /**
         * The content:// style URL for querying an instance range. The begin
         * and end of the range to query should be added as path segments if
         * this is used directly.
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/instances/when");

        /**
         * The content:// style URL for querying an instance range by Julian
         * Day. The start and end day should be added as path segments if this
         * is used directly.
         */
        public static final android.net.Uri CONTENT_BY_DAY_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/instances/whenbyday");

        /**
         * The content:// style URL for querying an instance range with a search
         * term. The begin, end, and search string should be appended as path
         * segments if this is used directly.
         */
        public static final android.net.Uri CONTENT_SEARCH_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/instances/search");

        /**
         * The content:// style URL for querying an instance range with a search
         * term. The start day, end day, and search string should be appended as
         * path segments if this is used directly.
         */
        public static final android.net.Uri CONTENT_SEARCH_BY_DAY_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/instances/searchbyday");

        /**
         * The default sort order for this table.
         */
        private static final java.lang.String DEFAULT_SORT_ORDER = "begin ASC";

        /**
         * The beginning time of the instance, in UTC milliseconds. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String BEGIN = "begin";

        /**
         * The ending time of the instance, in UTC milliseconds. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String END = "end";

        /**
         * The _id of the event for this instance. Column name.
         * <P>Type: INTEGER (long, foreign key to the Events table)</P>
         */
        public static final java.lang.String EVENT_ID = "event_id";

        /**
         * The Julian start day of the instance, relative to the local time
         * zone. Column name.
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String START_DAY = "startDay";

        /**
         * The Julian end day of the instance, relative to the local time
         * zone. Column name.
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String END_DAY = "endDay";

        /**
         * The start minute of the instance measured from midnight in the
         * local time zone. Column name.
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String START_MINUTE = "startMinute";

        /**
         * The end minute of the instance measured from midnight in the
         * local time zone. Column name.
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String END_MINUTE = "endMinute";
    }

    protected interface CalendarCacheColumns {
        /**
         * The key for the setting. Keys are defined in {@link CalendarCache}.
         */
        public static final java.lang.String KEY = "key";

        /**
         * The value of the given setting.
         */
        public static final java.lang.String VALUE = "value";
    }

    /**
     * CalendarCache stores some settings for calendar including the current
     * time zone for the instances. These settings are stored using a key/value
     * scheme. A {@link #KEY} must be specified when updating these values.
     */
    public static final class CalendarCache implements android.provider.CalendarContract.CalendarCacheColumns {
        /**
         * The URI to use for retrieving the properties from the Calendar db.
         */
        public static final android.net.Uri URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/properties");

        /**
         * This utility class cannot be instantiated
         */
        private CalendarCache() {
        }

        /**
         * They key for updating the use of auto/home time zones in Calendar.
         * Valid values are {@link #TIMEZONE_TYPE_AUTO} or
         * {@link #TIMEZONE_TYPE_HOME}.
         */
        public static final java.lang.String KEY_TIMEZONE_TYPE = "timezoneType";

        /**
         * The key for updating the time zone used by the provider when it
         * generates the instances table. This should only be written if the
         * type is set to {@link #TIMEZONE_TYPE_HOME}. A valid time zone id
         * should be written to this field.
         */
        public static final java.lang.String KEY_TIMEZONE_INSTANCES = "timezoneInstances";

        /**
         * The key for reading the last time zone set by the user. This should
         * only be read by apps and it will be automatically updated whenever
         * {@link #KEY_TIMEZONE_INSTANCES} is updated with
         * {@link #TIMEZONE_TYPE_HOME} set.
         */
        public static final java.lang.String KEY_TIMEZONE_INSTANCES_PREVIOUS = "timezoneInstancesPrevious";

        /**
         * The value to write to {@link #KEY_TIMEZONE_TYPE} if the provider
         * should stay in sync with the device's time zone.
         */
        public static final java.lang.String TIMEZONE_TYPE_AUTO = "auto";

        /**
         * The value to write to {@link #KEY_TIMEZONE_TYPE} if the provider
         * should use a fixed time zone set by the user.
         */
        public static final java.lang.String TIMEZONE_TYPE_HOME = "home";
    }

    /**
     * A few Calendar globals are needed in the CalendarProvider for expanding
     * the Instances table and these are all stored in the first (and only) row
     * of the CalendarMetaData table.
     *
     * @unknown 
     */
    protected interface CalendarMetaDataColumns {
        /**
         * The local timezone that was used for precomputing the fields
         * in the Instances table.
         */
        public static final java.lang.String LOCAL_TIMEZONE = "localTimezone";

        /**
         * The minimum time used in expanding the Instances table,
         * in UTC milliseconds.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MIN_INSTANCE = "minInstance";

        /**
         * The maximum time used in expanding the Instances table,
         * in UTC milliseconds.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MAX_INSTANCE = "maxInstance";

        /**
         * The minimum Julian day in the EventDays table.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MIN_EVENTDAYS = "minEventDays";

        /**
         * The maximum Julian day in the EventDays table.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MAX_EVENTDAYS = "maxEventDays";
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class CalendarMetaData implements android.provider.BaseColumns , android.provider.CalendarContract.CalendarMetaDataColumns {
        /**
         * This utility class cannot be instantiated
         */
        private CalendarMetaData() {
        }
    }

    protected interface EventDaysColumns {
        /**
         * The Julian starting day number. Column name.
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String STARTDAY = "startDay";

        /**
         * The Julian ending day number. Column name.
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String ENDDAY = "endDay";
    }

    /**
     * Fields and helpers for querying for a list of days that contain events.
     */
    public static final class EventDays implements android.provider.CalendarContract.EventDaysColumns {
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/instances/groupbyday");

        private static final java.lang.String SELECTION = "selected=1";

        /**
         * This utility class cannot be instantiated
         */
        private EventDays() {
        }

        /**
         * Retrieves the days with events for the Julian days starting at
         * "startDay" for "numDays". It returns a cursor containing startday and
         * endday representing the max range of days for all events beginning on
         * each startday.This is a blocking function and should not be done on
         * the UI thread.
         *
         * @param cr
         * 		the ContentResolver
         * @param startDay
         * 		the first Julian day in the range
         * @param numDays
         * 		the number of days to load (must be at least 1)
         * @param projection
         * 		the columns to return in the cursor
         * @return a database cursor containing a list of start and end days for
        events
         */
        public static final android.database.Cursor query(android.content.ContentResolver cr, int startDay, int numDays, java.lang.String[] projection) {
            if (numDays < 1) {
                return null;
            }
            int endDay = (startDay + numDays) - 1;
            android.net.Uri.Builder builder = android.provider.CalendarContract.EventDays.CONTENT_URI.buildUpon();
            android.content.ContentUris.appendId(builder, startDay);
            android.content.ContentUris.appendId(builder, endDay);
            return /* selection args */
            cr.query(builder.build(), projection, android.provider.CalendarContract.EventDays.SELECTION, null, android.provider.CalendarContract.EventDaysColumns.STARTDAY);
        }
    }

    protected interface RemindersColumns {
        /**
         * The event the reminder belongs to. Column name.
         * <P>Type: INTEGER (foreign key to the Events table)</P>
         */
        public static final java.lang.String EVENT_ID = "event_id";

        /**
         * The minutes prior to the event that the alarm should ring.  -1
         * specifies that we should use the default value for the system.
         * Column name.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MINUTES = "minutes";

        /**
         * Passing this as a minutes value will use the default reminder
         * minutes.
         */
        public static final int MINUTES_DEFAULT = -1;

        /**
         * The alarm method, as set on the server. {@link #METHOD_DEFAULT},
         * {@link #METHOD_ALERT}, {@link #METHOD_EMAIL}, {@link #METHOD_SMS} and
         * {@link #METHOD_ALARM} are possible values; the device will only
         * process {@link #METHOD_DEFAULT} and {@link #METHOD_ALERT} reminders
         * (the other types are simply stored so we can send the same reminder
         * info back to the server when we make changes).
         */
        public static final java.lang.String METHOD = "method";

        public static final int METHOD_DEFAULT = 0;

        public static final int METHOD_ALERT = 1;

        public static final int METHOD_EMAIL = 2;

        public static final int METHOD_SMS = 3;

        public static final int METHOD_ALARM = 4;
    }

    /**
     * Fields and helpers for accessing reminders for an event. Each row of this
     * table represents a single reminder for an event. Calling
     * {@link #query(ContentResolver, long, String[])} will return a list of reminders for
     * the event with the given eventId. Both apps and sync adapters may write
     * to this table. There are three writable fields and all of them must be
     * included when inserting a new reminder. They are:
     * <ul>
     * <li>{@link #EVENT_ID}</li>
     * <li>{@link #MINUTES}</li>
     * <li>{@link #METHOD}</li>
     * </ul>
     */
    public static final class Reminders implements android.provider.BaseColumns , android.provider.CalendarContract.EventsColumns , android.provider.CalendarContract.RemindersColumns {
        private static final java.lang.String REMINDERS_WHERE = android.provider.CalendarContract.Reminders.EVENT_ID + "=?";

        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/reminders");

        /**
         * This utility class cannot be instantiated
         */
        private Reminders() {
        }

        /**
         * Queries all reminders associated with the given event. This is a
         * blocking call and should not be done on the UI thread.
         *
         * @param cr
         * 		The content resolver to use for the query
         * @param eventId
         * 		The id of the event to retrieve reminders for
         * @param projection
         * 		the columns to return in the cursor
         * @return A Cursor containing all reminders for the event
         */
        public static final android.database.Cursor query(android.content.ContentResolver cr, long eventId, java.lang.String[] projection) {
            java.lang.String[] remArgs = new java.lang.String[]{ java.lang.Long.toString(eventId) };
            return /* selection args */
            /* sort order */
            cr.query(android.provider.CalendarContract.Reminders.CONTENT_URI, projection, android.provider.CalendarContract.Reminders.REMINDERS_WHERE, remArgs, null);
        }
    }

    protected interface CalendarAlertsColumns {
        /**
         * The event that the alert belongs to. Column name.
         * <P>Type: INTEGER (foreign key to the Events table)</P>
         */
        public static final java.lang.String EVENT_ID = "event_id";

        /**
         * The start time of the event, in UTC. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String BEGIN = "begin";

        /**
         * The end time of the event, in UTC. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String END = "end";

        /**
         * The alarm time of the event, in UTC. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String ALARM_TIME = "alarmTime";

        /**
         * The creation time of this database entry, in UTC.
         * Useful for debugging missed reminders. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String CREATION_TIME = "creationTime";

        /**
         * The time that the alarm broadcast was received by the Calendar app,
         * in UTC. Useful for debugging missed reminders. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String RECEIVED_TIME = "receivedTime";

        /**
         * The time that the notification was created by the Calendar app,
         * in UTC. Useful for debugging missed reminders. Column name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final java.lang.String NOTIFY_TIME = "notifyTime";

        /**
         * The state of this alert. It starts out as {@link #STATE_SCHEDULED}, then
         * when the alarm goes off, it changes to {@link #STATE_FIRED}, and then when
         * the user dismisses the alarm it changes to {@link #STATE_DISMISSED}. Column
         * name.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String STATE = "state";

        /**
         * An alert begins in this state when it is first created.
         */
        public static final int STATE_SCHEDULED = 0;

        /**
         * After a notification for an alert has been created it should be
         * updated to fired.
         */
        public static final int STATE_FIRED = 1;

        /**
         * Once the user has dismissed the notification the alert's state should
         * be set to dismissed so it is not fired again.
         */
        public static final int STATE_DISMISSED = 2;

        /**
         * The number of minutes that this alarm precedes the start time. Column
         * name.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String MINUTES = "minutes";

        /**
         * The default sort order for this alerts queries
         */
        public static final java.lang.String DEFAULT_SORT_ORDER = "begin ASC,title ASC";
    }

    /**
     * Fields and helpers for accessing calendar alerts information. These
     * fields are for tracking which alerts have been fired. Scheduled alarms
     * will generate an intent using {@link #ACTION_EVENT_REMINDER}. Apps that
     * receive this action may update the {@link #STATE} for the reminder when
     * they have finished handling it. Apps that have their notifications
     * disabled should not modify the table to ensure that they do not conflict
     * with another app that is generating a notification. In general, apps
     * should not need to write to this table directly except to update the
     * state of a reminder.
     */
    public static final class CalendarAlerts implements android.provider.BaseColumns , android.provider.CalendarContract.CalendarAlertsColumns , android.provider.CalendarContract.CalendarColumns , android.provider.CalendarContract.EventsColumns {
        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String TABLE_NAME = "CalendarAlerts";

        /**
         * The Uri for querying calendar alert information
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/calendar_alerts");

        /**
         * This utility class cannot be instantiated
         */
        private CalendarAlerts() {
        }

        private static final java.lang.String WHERE_ALARM_EXISTS = ((((((android.provider.CalendarContract.CalendarAlertsColumns.EVENT_ID + "=?") + " AND ") + android.provider.CalendarContract.CalendarAlertsColumns.BEGIN) + "=?") + " AND ") + android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME) + "=?";

        private static final java.lang.String WHERE_FINDNEXTALARMTIME = android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME + ">=?";

        private static final java.lang.String SORT_ORDER_ALARMTIME_ASC = android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME + " ASC";

        private static final java.lang.String WHERE_RESCHEDULE_MISSED_ALARMS = ((((((((((android.provider.CalendarContract.CalendarAlertsColumns.STATE + "=") + android.provider.CalendarContract.CalendarAlertsColumns.STATE_SCHEDULED) + " AND ") + android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME) + "<?") + " AND ") + android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME) + ">?") + " AND ") + android.provider.CalendarContract.CalendarAlertsColumns.END) + ">=?";

        /**
         * This URI is for grouping the query results by event_id and begin
         * time.  This will return one result per instance of an event.  So
         * events with multiple alarms will appear just once, but multiple
         * instances of a repeating event will show up multiple times.
         */
        public static final android.net.Uri CONTENT_URI_BY_INSTANCE = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/calendar_alerts/by_instance");

        private static final boolean DEBUG = false;

        /**
         * Helper for inserting an alarm time associated with an event TODO move
         * to Provider
         *
         * @unknown 
         */
        public static final android.net.Uri insert(android.content.ContentResolver cr, long eventId, long begin, long end, long alarmTime, int minutes) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.CalendarContract.CalendarAlerts.EVENT_ID, eventId);
            values.put(android.provider.CalendarContract.CalendarAlerts.BEGIN, begin);
            values.put(android.provider.CalendarContract.CalendarAlerts.END, end);
            values.put(android.provider.CalendarContract.CalendarAlerts.ALARM_TIME, alarmTime);
            long currentTime = java.lang.System.currentTimeMillis();
            values.put(android.provider.CalendarContract.CalendarAlerts.CREATION_TIME, currentTime);
            values.put(android.provider.CalendarContract.CalendarAlerts.RECEIVED_TIME, 0);
            values.put(android.provider.CalendarContract.CalendarAlerts.NOTIFY_TIME, 0);
            values.put(android.provider.CalendarContract.CalendarAlerts.STATE, android.provider.CalendarContract.CalendarAlertsColumns.STATE_SCHEDULED);
            values.put(android.provider.CalendarContract.CalendarAlerts.MINUTES, minutes);
            return cr.insert(android.provider.CalendarContract.CalendarAlerts.CONTENT_URI, values);
        }

        /**
         * Finds the next alarm after (or equal to) the given time and returns
         * the time of that alarm or -1 if no such alarm exists. This is a
         * blocking call and should not be done on the UI thread. TODO move to
         * provider
         *
         * @param cr
         * 		the ContentResolver
         * @param millis
         * 		the time in UTC milliseconds
         * @return the next alarm time greater than or equal to "millis", or -1
        if no such alarm exists.
         * @unknown 
         */
        public static final long findNextAlarmTime(android.content.ContentResolver cr, long millis) {
            java.lang.String selection = (android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME + ">=") + millis;
            // TODO: construct an explicit SQL query so that we can add
            // "LIMIT 1" to the end and get just one result.
            java.lang.String[] projection = new java.lang.String[]{ android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME };
            android.database.Cursor cursor = cr.query(android.provider.CalendarContract.CalendarAlerts.CONTENT_URI, projection, android.provider.CalendarContract.CalendarAlerts.WHERE_FINDNEXTALARMTIME, new java.lang.String[]{ java.lang.Long.toString(millis) }, android.provider.CalendarContract.CalendarAlerts.SORT_ORDER_ALARMTIME_ASC);
            long alarmTime = -1;
            try {
                if ((cursor != null) && cursor.moveToFirst()) {
                    alarmTime = cursor.getLong(0);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return alarmTime;
        }

        /**
         * Searches the CalendarAlerts table for alarms that should have fired
         * but have not and then reschedules them. This method can be called at
         * boot time to restore alarms that may have been lost due to a phone
         * reboot. TODO move to provider
         *
         * @param cr
         * 		the ContentResolver
         * @param context
         * 		the Context
         * @param manager
         * 		the AlarmManager
         * @unknown 
         */
        public static final void rescheduleMissedAlarms(android.content.ContentResolver cr, android.content.Context context, android.app.AlarmManager manager) {
            // Get all the alerts that have been scheduled but have not fired
            // and should have fired by now and are not too old.
            long now = java.lang.System.currentTimeMillis();
            long ancient = now - android.text.format.DateUtils.DAY_IN_MILLIS;
            java.lang.String[] projection = new java.lang.String[]{ android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME };
            // TODO: construct an explicit SQL query so that we can add
            // "GROUPBY" instead of doing a sort and de-dup
            android.database.Cursor cursor = cr.query(android.provider.CalendarContract.CalendarAlerts.CONTENT_URI, projection, android.provider.CalendarContract.CalendarAlerts.WHERE_RESCHEDULE_MISSED_ALARMS, new java.lang.String[]{ java.lang.Long.toString(now), java.lang.Long.toString(ancient), java.lang.Long.toString(now) }, android.provider.CalendarContract.CalendarAlerts.SORT_ORDER_ALARMTIME_ASC);
            if (cursor == null) {
                return;
            }
            if (android.provider.CalendarContract.CalendarAlerts.DEBUG) {
                android.util.Log.d(android.provider.CalendarContract.TAG, "missed alarms found: " + cursor.getCount());
            }
            try {
                long alarmTime = -1;
                while (cursor.moveToNext()) {
                    long newAlarmTime = cursor.getLong(0);
                    if (alarmTime != newAlarmTime) {
                        if (android.provider.CalendarContract.CalendarAlerts.DEBUG) {
                            android.util.Log.w(android.provider.CalendarContract.TAG, "rescheduling missed alarm. alarmTime: " + newAlarmTime);
                        }
                        android.provider.CalendarContract.CalendarAlerts.scheduleAlarm(context, manager, newAlarmTime);
                        alarmTime = newAlarmTime;
                    }
                } 
            } finally {
                cursor.close();
            }
        }

        /**
         * Schedules an alarm intent with the system AlarmManager that will
         * notify listeners when a reminder should be fired. The provider will
         * keep scheduled reminders up to date but apps may use this to
         * implement snooze functionality without modifying the reminders table.
         * Scheduled alarms will generate an intent using
         * {@link #ACTION_EVENT_REMINDER}. TODO Move to provider
         *
         * @param context
         * 		A context for referencing system resources
         * @param manager
         * 		The AlarmManager to use or null
         * @param alarmTime
         * 		The time to fire the intent in UTC millis since
         * 		epoch
         * @unknown 
         */
        public static void scheduleAlarm(android.content.Context context, android.app.AlarmManager manager, long alarmTime) {
            if (android.provider.CalendarContract.CalendarAlerts.DEBUG) {
                android.text.format.Time time = new android.text.format.Time();
                time.set(alarmTime);
                java.lang.String schedTime = time.format(" %a, %b %d, %Y %I:%M%P");
                android.util.Log.d(android.provider.CalendarContract.TAG, (("Schedule alarm at " + alarmTime) + " ") + schedTime);
            }
            if (manager == null) {
                manager = ((android.app.AlarmManager) (context.getSystemService(android.content.Context.ALARM_SERVICE)));
            }
            android.content.Intent intent = new android.content.Intent(android.provider.CalendarContract.ACTION_EVENT_REMINDER);
            intent.setData(android.content.ContentUris.withAppendedId(android.provider.CalendarContract.CONTENT_URI, alarmTime));
            intent.putExtra(android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME, alarmTime);
            android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(context, 0, intent, 0);
            manager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, alarmTime, pi);
        }

        /**
         * Searches for an entry in the CalendarAlerts table that matches the
         * given event id, begin time and alarm time. If one is found then this
         * alarm already exists and this method returns true. TODO Move to
         * provider
         *
         * @param cr
         * 		the ContentResolver
         * @param eventId
         * 		the event id to match
         * @param begin
         * 		the start time of the event in UTC millis
         * @param alarmTime
         * 		the alarm time of the event in UTC millis
         * @return true if there is already an alarm for the given event with
        the same start time and alarm time.
         * @unknown 
         */
        public static final boolean alarmExists(android.content.ContentResolver cr, long eventId, long begin, long alarmTime) {
            // TODO: construct an explicit SQL query so that we can add
            // "LIMIT 1" to the end and get just one result.
            java.lang.String[] projection = new java.lang.String[]{ android.provider.CalendarContract.CalendarAlertsColumns.ALARM_TIME };
            android.database.Cursor cursor = cr.query(android.provider.CalendarContract.CalendarAlerts.CONTENT_URI, projection, android.provider.CalendarContract.CalendarAlerts.WHERE_ALARM_EXISTS, new java.lang.String[]{ java.lang.Long.toString(eventId), java.lang.Long.toString(begin), java.lang.Long.toString(alarmTime) }, null);
            boolean found = false;
            try {
                if ((cursor != null) && (cursor.getCount() > 0)) {
                    found = true;
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return found;
        }
    }

    protected interface ColorsColumns extends android.provider.SyncStateContract.Columns {
        /**
         * The type of color, which describes how it should be used. Valid types
         * are {@link #TYPE_CALENDAR} and {@link #TYPE_EVENT}. Column name.
         * <P>
         * Type: INTEGER (NOT NULL)
         * </P>
         */
        public static final java.lang.String COLOR_TYPE = "color_type";

        /**
         * This indicateds a color that can be used for calendars.
         */
        public static final int TYPE_CALENDAR = 0;

        /**
         * This indicates a color that can be used for events.
         */
        public static final int TYPE_EVENT = 1;

        /**
         * The key used to reference this color. This can be any non-empty
         * string, but must be unique for a given {@link #ACCOUNT_TYPE} and
         * {@link #ACCOUNT_NAME}. Column name.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final java.lang.String COLOR_KEY = "color_index";

        /**
         * The color as an 8-bit ARGB integer value. Colors should specify alpha
         * as fully opaque (eg 0xFF993322) as the alpha may be ignored or
         * modified for display. It is reccomended that colors be usable with
         * light (near white) text. Apps should not depend on that assumption,
         * however. Column name.
         * <P>
         * Type: INTEGER (NOT NULL)
         * </P>
         */
        public static final java.lang.String COLOR = "color";
    }

    /**
     * Fields for accessing colors available for a given account. Colors are
     * referenced by {@link #COLOR_KEY} which must be unique for a given
     * account name/type. These values can only be updated by the sync
     * adapter. Only {@link #COLOR} may be updated after the initial insert. In
     * addition, a row can only be deleted once all references to that color
     * have been removed from the {@link Calendars} or {@link Events} tables.
     */
    public static final class Colors implements android.provider.CalendarContract.ColorsColumns {
        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String TABLE_NAME = "Colors";

        /**
         * The Uri for querying color information
         */
        @java.lang.SuppressWarnings("hiding")
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/colors");

        /**
         * This utility class cannot be instantiated
         */
        private Colors() {
        }
    }

    protected interface ExtendedPropertiesColumns {
        /**
         * The event the extended property belongs to. Column name.
         * <P>Type: INTEGER (foreign key to the Events table)</P>
         */
        public static final java.lang.String EVENT_ID = "event_id";

        /**
         * The name of the extended property.  This is a uri of the form
         * {scheme}#{local-name} convention. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String NAME = "name";

        /**
         * The value of the extended property. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String VALUE = "value";
    }

    /**
     * Fields for accessing the Extended Properties. This is a generic set of
     * name/value pairs for use by sync adapters to add extra
     * information to events. There are three writable columns and all three
     * must be present when inserting a new value. They are:
     * <ul>
     * <li>{@link #EVENT_ID}</li>
     * <li>{@link #NAME}</li>
     * <li>{@link #VALUE}</li>
     * </ul>
     */
    // TODO: fill out this class when we actually start utilizing extendedproperties
    // in the calendar application.
    public static final class ExtendedProperties implements android.provider.BaseColumns , android.provider.CalendarContract.EventsColumns , android.provider.CalendarContract.ExtendedPropertiesColumns {
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.CalendarContract.AUTHORITY) + "/extendedproperties");

        /**
         * This utility class cannot be instantiated
         */
        private ExtendedProperties() {
        }
    }

    /**
     * A table provided for sync adapters to use for storing private sync state data.
     *
     * @see SyncStateContract
     */
    public static final class SyncState implements android.provider.SyncStateContract.Columns {
        /**
         * This utility class cannot be instantiated
         */
        private SyncState() {
        }

        private static final java.lang.String CONTENT_DIRECTORY = android.provider.SyncStateContract.Constants.CONTENT_DIRECTORY;

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.CalendarContract.CONTENT_URI, android.provider.CalendarContract.SyncState.CONTENT_DIRECTORY);
    }

    /**
     * Columns from the EventsRawTimes table
     *
     * @unknown 
     */
    protected interface EventsRawTimesColumns {
        /**
         * The corresponding event id. Column name.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String EVENT_ID = "event_id";

        /**
         * The RFC2445 compliant time the event starts. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String DTSTART_2445 = "dtstart2445";

        /**
         * The RFC2445 compliant time the event ends. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String DTEND_2445 = "dtend2445";

        /**
         * The RFC2445 compliant original instance time of the recurring event
         * for which this event is an exception. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ORIGINAL_INSTANCE_TIME_2445 = "originalInstanceTime2445";

        /**
         * The RFC2445 compliant last date this event repeats on, or NULL if it
         * never ends. Column name.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String LAST_DATE_2445 = "lastDate2445";
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class EventsRawTimes implements android.provider.BaseColumns , android.provider.CalendarContract.EventsRawTimesColumns {
        /**
         * This utility class cannot be instantiated
         */
        private EventsRawTimes() {
        }
    }
}

