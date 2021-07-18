/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package android.app.usage;


/**
 * A result returned from {@link android.app.usage.UsageStatsManager#queryEvents(long, long)}
 * from which to read {@link android.app.usage.UsageEvents.Event} objects.
 */
public final class UsageEvents implements android.os.Parcelable {
    /**
     * An event representing a state change for a component.
     */
    public static final class Event {
        /**
         * No event type.
         */
        public static final int NONE = 0;

        /**
         * An event type denoting that a component moved to the foreground.
         */
        public static final int MOVE_TO_FOREGROUND = 1;

        /**
         * An event type denoting that a component moved to the background.
         */
        public static final int MOVE_TO_BACKGROUND = 2;

        /**
         * An event type denoting that a component was in the foreground when the stats
         * rolled-over. This is effectively treated as a {@link #MOVE_TO_BACKGROUND}.
         * {@hide }
         */
        public static final int END_OF_DAY = 3;

        /**
         * An event type denoting that a component was in the foreground the previous day.
         * This is effectively treated as a {@link #MOVE_TO_FOREGROUND}.
         * {@hide }
         */
        public static final int CONTINUE_PREVIOUS_DAY = 4;

        /**
         * An event type denoting that the device configuration has changed.
         */
        public static final int CONFIGURATION_CHANGE = 5;

        /**
         * An event type denoting that a package was interacted with in some way by the system.
         *
         * @unknown 
         */
        public static final int SYSTEM_INTERACTION = 6;

        /**
         * An event type denoting that a package was interacted with in some way by the user.
         */
        public static final int USER_INTERACTION = 7;

        /**
         * An event type denoting that an action equivalent to a ShortcutInfo is taken by the user.
         *
         * @see android.content.pm.ShortcutManager#reportShortcutUsed(String)
         */
        public static final int SHORTCUT_INVOCATION = 8;

        /**
         * {@hide }
         */
        public java.lang.String mPackage;

        /**
         * {@hide }
         */
        public java.lang.String mClass;

        /**
         * {@hide }
         */
        public long mTimeStamp;

        /**
         * {@hide }
         */
        public int mEventType;

        /**
         * Only present for {@link #CONFIGURATION_CHANGE} event types.
         * {@hide }
         */
        public android.content.res.Configuration mConfiguration;

        /**
         * ID of the shortcut.
         * Only present for {@link #SHORTCUT_INVOCATION} event types.
         * {@hide }
         */
        public java.lang.String mShortcutId;

        /**
         * The package name of the source of this event.
         */
        public java.lang.String getPackageName() {
            return mPackage;
        }

        /**
         * The class name of the source of this event. This may be null for
         * certain events.
         */
        public java.lang.String getClassName() {
            return mClass;
        }

        /**
         * The time at which this event occurred, measured in milliseconds since the epoch.
         * <p/>
         * See {@link System#currentTimeMillis()}.
         */
        public long getTimeStamp() {
            return mTimeStamp;
        }

        /**
         * The event type.
         *
         * See {@link #MOVE_TO_BACKGROUND}
         * See {@link #MOVE_TO_FOREGROUND}
         */
        public int getEventType() {
            return mEventType;
        }

        /**
         * Returns a {@link Configuration} for this event if the event is of type
         * {@link #CONFIGURATION_CHANGE}, otherwise it returns null.
         */
        public android.content.res.Configuration getConfiguration() {
            return mConfiguration;
        }

        /**
         * Returns the ID of a {@link android.content.pm.ShortcutInfo} for this event
         * if the event is of type {@link #SHORTCUT_INVOCATION}, otherwise it returns null.
         *
         * @see android.content.pm.ShortcutManager#reportShortcutUsed(String)
         */
        public java.lang.String getShortcutId() {
            return mShortcutId;
        }
    }

    // Only used when creating the resulting events. Not used for reading/unparceling.
    private java.util.List<android.app.usage.UsageEvents.Event> mEventsToWrite = null;

    // Only used for reading/unparceling events.
    private android.os.Parcel mParcel = null;

    private final int mEventCount;

    private int mIndex = 0;

    /* In order to save space, since ComponentNames will be duplicated everywhere,
    we use a map and index into it.
     */
    private java.lang.String[] mStringPool;

    /**
     * Construct the iterator from a parcel.
     * {@hide }
     */
    public UsageEvents(android.os.Parcel in) {
        mEventCount = in.readInt();
        mIndex = in.readInt();
        if (mEventCount > 0) {
            mStringPool = in.createStringArray();
            final int listByteLength = in.readInt();
            final int positionInParcel = in.readInt();
            mParcel = android.os.Parcel.obtain();
            mParcel.setDataPosition(0);
            mParcel.appendFrom(in, in.dataPosition(), listByteLength);
            mParcel.setDataSize(mParcel.dataPosition());
            mParcel.setDataPosition(positionInParcel);
        }
    }

    /**
     * Create an empty iterator.
     * {@hide }
     */
    UsageEvents() {
        mEventCount = 0;
    }

    /**
     * Construct the iterator in preparation for writing it to a parcel.
     * {@hide }
     */
    public UsageEvents(java.util.List<android.app.usage.UsageEvents.Event> events, java.lang.String[] stringPool) {
        mStringPool = stringPool;
        mEventCount = events.size();
        mEventsToWrite = events;
    }

    /**
     * Returns whether or not there are more events to read using
     * {@link #getNextEvent(android.app.usage.UsageEvents.Event)}.
     *
     * @return true if there are more events, false otherwise.
     */
    public boolean hasNextEvent() {
        return mIndex < mEventCount;
    }

    /**
     * Retrieve the next {@link android.app.usage.UsageEvents.Event} from the collection and put the
     * resulting data into {@code eventOut}.
     *
     * @param eventOut
     * 		The {@link android.app.usage.UsageEvents.Event} object that will receive the
     * 		next event data.
     * @return true if an event was available, false if there are no more events.
     */
    public boolean getNextEvent(android.app.usage.UsageEvents.Event eventOut) {
        if (mIndex >= mEventCount) {
            return false;
        }
        readEventFromParcel(mParcel, eventOut);
        mIndex++;
        if (mIndex >= mEventCount) {
            mParcel.recycle();
            mParcel = null;
        }
        return true;
    }

    /**
     * Resets the collection so that it can be iterated over from the beginning.
     *
     * @unknown When this object is iterated to completion, the parcel is destroyed and
    so resetToStart doesn't work.
     */
    public void resetToStart() {
        mIndex = 0;
        if (mParcel != null) {
            mParcel.setDataPosition(0);
        }
    }

    private int findStringIndex(java.lang.String str) {
        final int index = java.util.Arrays.binarySearch(mStringPool, str);
        if (index < 0) {
            throw new java.lang.IllegalStateException(("String '" + str) + "' is not in the string pool");
        }
        return index;
    }

    /**
     * Writes a single event to the parcel. Modify this when updating {@link Event}.
     */
    private void writeEventToParcel(android.app.usage.UsageEvents.Event event, android.os.Parcel p, int flags) {
        final int packageIndex;
        if (event.mPackage != null) {
            packageIndex = findStringIndex(event.mPackage);
        } else {
            packageIndex = -1;
        }
        final int classIndex;
        if (event.mClass != null) {
            classIndex = findStringIndex(event.mClass);
        } else {
            classIndex = -1;
        }
        p.writeInt(packageIndex);
        p.writeInt(classIndex);
        p.writeInt(event.mEventType);
        p.writeLong(event.mTimeStamp);
        switch (event.mEventType) {
            case android.app.usage.UsageEvents.Event.CONFIGURATION_CHANGE :
                event.mConfiguration.writeToParcel(p, flags);
                break;
            case android.app.usage.UsageEvents.Event.SHORTCUT_INVOCATION :
                p.writeString(event.mShortcutId);
                break;
        }
    }

    /**
     * Reads a single event from the parcel. Modify this when updating {@link Event}.
     */
    private void readEventFromParcel(android.os.Parcel p, android.app.usage.UsageEvents.Event eventOut) {
        final int packageIndex = p.readInt();
        if (packageIndex >= 0) {
            eventOut.mPackage = mStringPool[packageIndex];
        } else {
            eventOut.mPackage = null;
        }
        final int classIndex = p.readInt();
        if (classIndex >= 0) {
            eventOut.mClass = mStringPool[classIndex];
        } else {
            eventOut.mClass = null;
        }
        eventOut.mEventType = p.readInt();
        eventOut.mTimeStamp = p.readLong();
        // Fill out the event-dependant fields.
        eventOut.mConfiguration = null;
        eventOut.mShortcutId = null;
        switch (eventOut.mEventType) {
            case android.app.usage.UsageEvents.Event.CONFIGURATION_CHANGE :
                // Extract the configuration for configuration change events.
                eventOut.mConfiguration = android.content.res.Configuration.CREATOR.createFromParcel(p);
                break;
            case android.app.usage.UsageEvents.Event.SHORTCUT_INVOCATION :
                eventOut.mShortcutId = p.readString();
                break;
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mEventCount);
        dest.writeInt(mIndex);
        if (mEventCount > 0) {
            dest.writeStringArray(mStringPool);
            if (mEventsToWrite != null) {
                // Write out the events
                android.os.Parcel p = android.os.Parcel.obtain();
                try {
                    p.setDataPosition(0);
                    for (int i = 0; i < mEventCount; i++) {
                        final android.app.usage.UsageEvents.Event event = mEventsToWrite.get(i);
                        writeEventToParcel(event, p, flags);
                    }
                    final int listByteLength = p.dataPosition();
                    // Write the total length of the data.
                    dest.writeInt(listByteLength);
                    // Write our current position into the data.
                    dest.writeInt(0);
                    // Write the data.
                    dest.appendFrom(p, 0, listByteLength);
                } finally {
                    p.recycle();
                }
            } else
                if (mParcel != null) {
                    // Write the total length of the data.
                    dest.writeInt(mParcel.dataSize());
                    // Write out current position into the data.
                    dest.writeInt(mParcel.dataPosition());
                    // Write the data.
                    dest.appendFrom(mParcel, 0, mParcel.dataSize());
                } else {
                    throw new java.lang.IllegalStateException("Either mParcel or mEventsToWrite must not be null");
                }

        }
    }

    public static final android.os.Parcelable.Creator<android.app.usage.UsageEvents> CREATOR = new android.os.Parcelable.Creator<android.app.usage.UsageEvents>() {
        @java.lang.Override
        public android.app.usage.UsageEvents createFromParcel(android.os.Parcel source) {
            return new android.app.usage.UsageEvents(source);
        }

        @java.lang.Override
        public android.app.usage.UsageEvents[] newArray(int size) {
            return new android.app.usage.UsageEvents[size];
        }
    };
}

