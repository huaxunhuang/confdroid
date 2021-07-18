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
package android.util;


/**
 * Access to the system diagnostic event record.  System diagnostic events are
 * used to record certain system-level events (such as garbage collection,
 * activity manager state, system watchdogs, and other low level activity),
 * which may be automatically collected and analyzed during system development.
 *
 * <p>This is <b>not</b> the main "logcat" debugging log ({@link android.util.Log})!
 * These diagnostic events are for system integrators, not application authors.
 *
 * <p>Events use integer tag codes corresponding to /system/etc/event-log-tags.
 * They carry a payload of one or more int, long, or String values.  The
 * event-log-tags file defines the payload contents for each type code.
 */
public class EventLog {
    /**
     *
     *
     * @unknown 
     */
    public EventLog() {
    }

    private static final java.lang.String TAG = "EventLog";

    private static final java.lang.String TAGS_FILE = "/system/etc/event-log-tags";

    private static final java.lang.String COMMENT_PATTERN = "^\\s*(#.*)?$";

    private static final java.lang.String TAG_PATTERN = "^\\s*(\\d+)\\s+(\\w+)\\s*(\\(.*\\))?\\s*$";

    private static java.util.HashMap<java.lang.String, java.lang.Integer> sTagCodes = null;

    private static java.util.HashMap<java.lang.Integer, java.lang.String> sTagNames = null;

    /**
     * A previously logged event read from the logs. Instances are thread safe.
     */
    public static final class Event {
        private final java.nio.ByteBuffer mBuffer;

        // Layout of event log entry received from Android logger.
        // see system/core/include/log/logger.h
        private static final int LENGTH_OFFSET = 0;

        private static final int HEADER_SIZE_OFFSET = 2;

        private static final int PROCESS_OFFSET = 4;

        private static final int THREAD_OFFSET = 8;

        private static final int SECONDS_OFFSET = 12;

        private static final int NANOSECONDS_OFFSET = 16;

        // Layout for event log v1 format, v2 and v3 use HEADER_SIZE_OFFSET
        private static final int V1_PAYLOAD_START = 20;

        private static final int DATA_OFFSET = 4;

        // Value types
        private static final byte INT_TYPE = 0;

        private static final byte LONG_TYPE = 1;

        private static final byte STRING_TYPE = 2;

        private static final byte LIST_TYPE = 3;

        private static final byte FLOAT_TYPE = 4;

        /**
         *
         *
         * @param data
         * 		containing event, read from the system
         */
        /* package */
        Event(byte[] data) {
            mBuffer = java.nio.ByteBuffer.wrap(data);
            mBuffer.order(java.nio.ByteOrder.nativeOrder());
        }

        /**
         *
         *
         * @return the process ID which wrote the log entry
         */
        public int getProcessId() {
            return mBuffer.getInt(android.util.EventLog.Event.PROCESS_OFFSET);
        }

        /**
         *
         *
         * @return the thread ID which wrote the log entry
         */
        public int getThreadId() {
            return mBuffer.getInt(android.util.EventLog.Event.THREAD_OFFSET);
        }

        /**
         *
         *
         * @return the wall clock time when the entry was written
         */
        public long getTimeNanos() {
            return (mBuffer.getInt(android.util.EventLog.Event.SECONDS_OFFSET) * 1000000000L) + mBuffer.getInt(android.util.EventLog.Event.NANOSECONDS_OFFSET);
        }

        /**
         *
         *
         * @return the type tag code of the entry
         */
        public int getTag() {
            int offset = mBuffer.getShort(android.util.EventLog.Event.HEADER_SIZE_OFFSET);
            if (offset == 0) {
                offset = android.util.EventLog.Event.V1_PAYLOAD_START;
            }
            return mBuffer.getInt(offset);
        }

        /**
         *
         *
         * @return one of Integer, Long, Float, String, null, or Object[] of same.
         */
        public synchronized java.lang.Object getData() {
            try {
                int offset = mBuffer.getShort(android.util.EventLog.Event.HEADER_SIZE_OFFSET);
                if (offset == 0) {
                    offset = android.util.EventLog.Event.V1_PAYLOAD_START;
                }
                mBuffer.limit(offset + mBuffer.getShort(android.util.EventLog.Event.LENGTH_OFFSET));
                mBuffer.position(offset + android.util.EventLog.Event.DATA_OFFSET);// Just after the tag.

                return decodeObject();
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.wtf(android.util.EventLog.TAG, "Illegal entry payload: tag=" + getTag(), e);
                return null;
            } catch (java.nio.BufferUnderflowException e) {
                android.util.Log.wtf(android.util.EventLog.TAG, "Truncated entry payload: tag=" + getTag(), e);
                return null;
            }
        }

        /**
         *
         *
         * @return the loggable item at the current position in mBuffer.
         */
        private java.lang.Object decodeObject() {
            byte type = mBuffer.get();
            switch (type) {
                case android.util.EventLog.Event.INT_TYPE :
                    return mBuffer.getInt();
                case android.util.EventLog.Event.LONG_TYPE :
                    return mBuffer.getLong();
                case android.util.EventLog.Event.FLOAT_TYPE :
                    return mBuffer.getFloat();
                case android.util.EventLog.Event.STRING_TYPE :
                    try {
                        int length = mBuffer.getInt();
                        int start = mBuffer.position();
                        mBuffer.position(start + length);
                        return new java.lang.String(mBuffer.array(), start, length, "UTF-8");
                    } catch (java.io.UnsupportedEncodingException e) {
                        android.util.Log.wtf(android.util.EventLog.TAG, "UTF-8 is not supported", e);
                        return null;
                    }
                case android.util.EventLog.Event.LIST_TYPE :
                    int length = mBuffer.get();
                    if (length < 0)
                        length += 256;
                    // treat as signed byte

                    java.lang.Object[] array = new java.lang.Object[length];
                    for (int i = 0; i < length; ++i)
                        array[i] = decodeObject();

                    return array;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown entry type: " + type);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public static android.util.EventLog.Event fromBytes(byte[] data) {
            return new android.util.EventLog.Event(data);
        }

        /**
         *
         *
         * @unknown 
         */
        public byte[] getBytes() {
            byte[] bytes = mBuffer.array();
            return java.util.Arrays.copyOf(bytes, bytes.length);
        }
    }

    // We assume that the native methods deal with any concurrency issues.
    /**
     * Record an event log message.
     *
     * @param tag
     * 		The event type tag code
     * @param value
     * 		A value to log
     * @return The number of bytes written
     */
    public static native int writeEvent(int tag, int value);

    /**
     * Record an event log message.
     *
     * @param tag
     * 		The event type tag code
     * @param value
     * 		A value to log
     * @return The number of bytes written
     */
    public static native int writeEvent(int tag, long value);

    /**
     * Record an event log message.
     *
     * @param tag
     * 		The event type tag code
     * @param value
     * 		A value to log
     * @return The number of bytes written
     */
    public static native int writeEvent(int tag, float value);

    /**
     * Record an event log message.
     *
     * @param tag
     * 		The event type tag code
     * @param str
     * 		A value to log
     * @return The number of bytes written
     */
    public static native int writeEvent(int tag, java.lang.String str);

    /**
     * Record an event log message.
     *
     * @param tag
     * 		The event type tag code
     * @param list
     * 		A list of values to log
     * @return The number of bytes written
     */
    public static native int writeEvent(int tag, java.lang.Object... list);

    /**
     * Read events from the log, filtered by type.
     *
     * @param tags
     * 		to search for
     * @param output
     * 		container to add events into
     * @throws IOException
     * 		if something goes wrong reading events
     */
    public static native void readEvents(int[] tags, java.util.Collection<android.util.EventLog.Event> output) throws java.io.IOException;

    /**
     * Get the name associated with an event type tag code.
     *
     * @param tag
     * 		code to look up
     * @return the name of the tag, or null if no tag has that number
     */
    public static java.lang.String getTagName(int tag) {
        android.util.EventLog.readTagsFile();
        return android.util.EventLog.sTagNames.get(tag);
    }

    /**
     * Get the event type tag code associated with an event name.
     *
     * @param name
     * 		of event to look up
     * @return the tag code, or -1 if no tag has that name
     */
    public static int getTagCode(java.lang.String name) {
        android.util.EventLog.readTagsFile();
        java.lang.Integer code = android.util.EventLog.sTagCodes.get(name);
        return code != null ? code : -1;
    }

    /**
     * Read TAGS_FILE, populating sTagCodes and sTagNames, if not already done.
     */
    private static synchronized void readTagsFile() {
        if ((android.util.EventLog.sTagCodes != null) && (android.util.EventLog.sTagNames != null))
            return;

        android.util.EventLog.sTagCodes = new java.util.HashMap<java.lang.String, java.lang.Integer>();
        android.util.EventLog.sTagNames = new java.util.HashMap<java.lang.Integer, java.lang.String>();
        java.util.regex.Pattern comment = java.util.regex.Pattern.compile(android.util.EventLog.COMMENT_PATTERN);
        java.util.regex.Pattern tag = java.util.regex.Pattern.compile(android.util.EventLog.TAG_PATTERN);
        java.io.BufferedReader reader = null;
        java.lang.String line;
        try {
            reader = new java.io.BufferedReader(new java.io.FileReader(android.util.EventLog.TAGS_FILE), 256);
            while ((line = reader.readLine()) != null) {
                if (comment.matcher(line).matches())
                    continue;

                java.util.regex.Matcher m = tag.matcher(line);
                if (!m.matches()) {
                    android.util.Log.wtf(android.util.EventLog.TAG, (("Bad entry in " + android.util.EventLog.TAGS_FILE) + ": ") + line);
                    continue;
                }
                try {
                    int num = java.lang.Integer.parseInt(m.group(1));
                    java.lang.String name = m.group(2);
                    android.util.EventLog.sTagCodes.put(name, num);
                    android.util.EventLog.sTagNames.put(num, name);
                } catch (java.lang.NumberFormatException e) {
                    android.util.Log.wtf(android.util.EventLog.TAG, (("Error in " + android.util.EventLog.TAGS_FILE) + ": ") + line, e);
                }
            } 
        } catch (java.io.IOException e) {
            android.util.Log.wtf(android.util.EventLog.TAG, "Error reading " + android.util.EventLog.TAGS_FILE, e);
            // Leave the maps existing but unpopulated
        } finally {
            try {
                if (reader != null)
                    reader.close();

            } catch (java.io.IOException e) {
            }
        }
    }
}

