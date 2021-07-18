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
package android.media;


/**
 * Class to hold the media's metadata.  Metadata are used
 * for human consumption and can be embedded in the media (e.g
 * shoutcast) or available from an external source. The source can be
 * local (e.g thumbnail stored in the DB) or remote.
 *
 * Metadata is like a Bundle. It is sparse and each key can occur at
 * most once. The key is an integer and the value is the actual metadata.
 *
 * The caller is expected to know the type of the metadata and call
 * the right get* method to fetch its value.
 *
 * @unknown 
 * @deprecated Use {@link MediaMetadata}.
 */
@java.lang.Deprecated
public class Metadata {
    // The metadata are keyed using integers rather than more heavy
    // weight strings. We considered using Bundle to ship the metadata
    // between the native layer and the java layer but dropped that
    // option since keeping in sync a native implementation of Bundle
    // and the java one would be too burdensome. Besides Bundle uses
    // String for its keys.
    // The key range [0 8192) is reserved for the system.
    // 
    // We manually serialize the data in Parcels. For large memory
    // blob (bitmaps, raw pictures) we use MemoryFile which allow the
    // client to make the data purge-able once it is done with it.
    // 
    /**
     * {@hide }
     */
    public static final int ANY = 0;// Never used for metadata returned, only for filtering.


    // Keep in sync with kAny in MediaPlayerService.cpp
    // Playback capabilities.
    /**
     * Indicate whether the media can be paused
     */
    public static final int PAUSE_AVAILABLE = 1;// Boolean


    /**
     * Indicate whether the media can be backward seeked
     */
    public static final int SEEK_BACKWARD_AVAILABLE = 2;// Boolean


    /**
     * Indicate whether the media can be forward seeked
     */
    public static final int SEEK_FORWARD_AVAILABLE = 3;// Boolean


    /**
     * Indicate whether the media can be seeked
     */
    public static final int SEEK_AVAILABLE = 4;// Boolean


    // TODO: Should we use numbers compatible with the metadata retriever?
    /**
     * {@hide }
     */
    public static final int TITLE = 5;// String


    /**
     * {@hide }
     */
    public static final int COMMENT = 6;// String


    /**
     * {@hide }
     */
    public static final int COPYRIGHT = 7;// String


    /**
     * {@hide }
     */
    public static final int ALBUM = 8;// String


    /**
     * {@hide }
     */
    public static final int ARTIST = 9;// String


    /**
     * {@hide }
     */
    public static final int AUTHOR = 10;// String


    /**
     * {@hide }
     */
    public static final int COMPOSER = 11;// String


    /**
     * {@hide }
     */
    public static final int GENRE = 12;// String


    /**
     * {@hide }
     */
    public static final int DATE = 13;// Date


    /**
     * {@hide }
     */
    public static final int DURATION = 14;// Integer(millisec)


    /**
     * {@hide }
     */
    public static final int CD_TRACK_NUM = 15;// Integer 1-based


    /**
     * {@hide }
     */
    public static final int CD_TRACK_MAX = 16;// Integer


    /**
     * {@hide }
     */
    public static final int RATING = 17;// String


    /**
     * {@hide }
     */
    public static final int ALBUM_ART = 18;// byte[]


    /**
     * {@hide }
     */
    public static final int VIDEO_FRAME = 19;// Bitmap


    /**
     * {@hide }
     */
    public static final int BIT_RATE = 20;// Integer, Aggregate rate of


    // all the streams in bps.
    /**
     * {@hide }
     */
    public static final int AUDIO_BIT_RATE = 21;// Integer, bps


    /**
     * {@hide }
     */
    public static final int VIDEO_BIT_RATE = 22;// Integer, bps


    /**
     * {@hide }
     */
    public static final int AUDIO_SAMPLE_RATE = 23;// Integer, Hz


    /**
     * {@hide }
     */
    public static final int VIDEO_FRAME_RATE = 24;// Integer, Hz


    // See RFC2046 and RFC4281.
    /**
     * {@hide }
     */
    public static final int MIME_TYPE = 25;// String


    /**
     * {@hide }
     */
    public static final int AUDIO_CODEC = 26;// String


    /**
     * {@hide }
     */
    public static final int VIDEO_CODEC = 27;// String


    /**
     * {@hide }
     */
    public static final int VIDEO_HEIGHT = 28;// Integer


    /**
     * {@hide }
     */
    public static final int VIDEO_WIDTH = 29;// Integer


    /**
     * {@hide }
     */
    public static final int NUM_TRACKS = 30;// Integer


    /**
     * {@hide }
     */
    public static final int DRM_CRIPPLED = 31;// Boolean


    private static final int LAST_SYSTEM = 31;

    private static final int FIRST_CUSTOM = 8192;

    // Shorthands to set the MediaPlayer's metadata filter.
    /**
     * {@hide }
     */
    public static final java.util.Set<java.lang.Integer> MATCH_NONE = java.util.Collections.EMPTY_SET;

    /**
     * {@hide }
     */
    public static final java.util.Set<java.lang.Integer> MATCH_ALL = java.util.Collections.singleton(android.media.Metadata.ANY);

    /**
     * {@hide }
     */
    public static final int STRING_VAL = 1;

    /**
     * {@hide }
     */
    public static final int INTEGER_VAL = 2;

    /**
     * {@hide }
     */
    public static final int BOOLEAN_VAL = 3;

    /**
     * {@hide }
     */
    public static final int LONG_VAL = 4;

    /**
     * {@hide }
     */
    public static final int DOUBLE_VAL = 5;

    /**
     * {@hide }
     */
    public static final int DATE_VAL = 6;

    /**
     * {@hide }
     */
    public static final int BYTE_ARRAY_VAL = 7;

    // FIXME: misses a type for shared heap is missing (MemoryFile).
    // FIXME: misses a type for bitmaps.
    private static final int LAST_TYPE = 7;

    private static final java.lang.String TAG = "media.Metadata";

    private static final int kInt32Size = 4;

    private static final int kMetaHeaderSize = 2 * android.media.Metadata.kInt32Size;// size + marker


    private static final int kRecordHeaderSize = 3 * android.media.Metadata.kInt32Size;// size + id + type


    private static final int kMetaMarker = 0x4d455441;// 'M' 'E' 'T' 'A'


    // After a successful parsing, set the parcel with the serialized metadata.
    private android.os.Parcel mParcel;

    // Map to associate a Metadata key (e.g TITLE) with the offset of
    // the record's payload in the parcel.
    // Used to look up if a key was present too.
    // Key: Metadata ID
    // Value: Offset of the metadata type field in the record.
    private final java.util.HashMap<java.lang.Integer, java.lang.Integer> mKeyToPosMap = new java.util.HashMap<java.lang.Integer, java.lang.Integer>();

    /**
     * {@hide }
     */
    public Metadata() {
    }

    /**
     * Go over all the records, collecting metadata keys and records'
     * type field offset in the Parcel. These are stored in
     * mKeyToPosMap for latter retrieval.
     * Format of a metadata record:
     * <pre>
     * 1                   2                   3
     * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     record size                               |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     metadata key                              |  // TITLE
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     metadata type                             |  // STRING_VAL
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                                                               |
     * |                .... metadata payload ....                     |
     * |                                                               |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * </pre>
     *
     * @param parcel
     * 		With the serialized records.
     * @param bytesLeft
     * 		How many bytes in the parcel should be processed.
     * @return false if an error occurred during parsing.
     */
    private boolean scanAllRecords(android.os.Parcel parcel, int bytesLeft) {
        int recCount = 0;
        boolean error = false;
        mKeyToPosMap.clear();
        while (bytesLeft > android.media.Metadata.kRecordHeaderSize) {
            final int start = parcel.dataPosition();
            // Check the size.
            final int size = parcel.readInt();
            if (size <= android.media.Metadata.kRecordHeaderSize) {
                // at least 1 byte should be present.
                android.util.Log.e(android.media.Metadata.TAG, "Record is too short");
                error = true;
                break;
            }
            // Check the metadata key.
            final int metadataId = parcel.readInt();
            if (!checkMetadataId(metadataId)) {
                error = true;
                break;
            }
            // Store the record offset which points to the type
            // field so we can later on read/unmarshall the record
            // payload.
            if (mKeyToPosMap.containsKey(metadataId)) {
                android.util.Log.e(android.media.Metadata.TAG, "Duplicate metadata ID found");
                error = true;
                break;
            }
            mKeyToPosMap.put(metadataId, parcel.dataPosition());
            // Check the metadata type.
            final int metadataType = parcel.readInt();
            if ((metadataType <= 0) || (metadataType > android.media.Metadata.LAST_TYPE)) {
                android.util.Log.e(android.media.Metadata.TAG, "Invalid metadata type " + metadataType);
                error = true;
                break;
            }
            // Skip to the next one.
            try {
                parcel.setDataPosition(android.util.MathUtils.addOrThrow(start, size));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(android.media.Metadata.TAG, "Invalid size: " + e.getMessage());
                error = true;
                break;
            }
            bytesLeft -= size;
            ++recCount;
        } 
        if ((0 != bytesLeft) || error) {
            android.util.Log.e(android.media.Metadata.TAG, "Ran out of data or error on record " + recCount);
            mKeyToPosMap.clear();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check a parcel containing metadata is well formed. The header
     * is checked as well as the individual records format. However, the
     * data inside the record is not checked because we do lazy access
     * (we check/unmarshall only data the user asks for.)
     *
     * Format of a metadata parcel:
     * <pre>
     * 1                   2                   3
     * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                     metadata total size                       |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |     'M'       |     'E'       |     'T'       |     'A'       |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                                                               |
     * |                .... metadata records ....                     |
     * |                                                               |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * </pre>
     *
     * @param parcel
     * 		With the serialized data. Metadata keeps a
     * 		reference on it to access it later on. The caller
     * 		should not modify the parcel after this call (and
     * 		not call recycle on it.)
     * @return false if an error occurred.
    {@hide }
     */
    public boolean parse(android.os.Parcel parcel) {
        if (parcel.dataAvail() < android.media.Metadata.kMetaHeaderSize) {
            android.util.Log.e(android.media.Metadata.TAG, "Not enough data " + parcel.dataAvail());
            return false;
        }
        final int pin = parcel.dataPosition();// to roll back in case of errors.

        final int size = parcel.readInt();
        // The extra kInt32Size below is to account for the int32 'size' just read.
        if (((parcel.dataAvail() + android.media.Metadata.kInt32Size) < size) || (size < android.media.Metadata.kMetaHeaderSize)) {
            android.util.Log.e(android.media.Metadata.TAG, (((("Bad size " + size) + " avail ") + parcel.dataAvail()) + " position ") + pin);
            parcel.setDataPosition(pin);
            return false;
        }
        // Checks if the 'M' 'E' 'T' 'A' marker is present.
        final int kShouldBeMetaMarker = parcel.readInt();
        if (kShouldBeMetaMarker != android.media.Metadata.kMetaMarker) {
            android.util.Log.e(android.media.Metadata.TAG, "Marker missing " + java.lang.Integer.toHexString(kShouldBeMetaMarker));
            parcel.setDataPosition(pin);
            return false;
        }
        // Scan the records to collect metadata ids and offsets.
        if (!scanAllRecords(parcel, size - android.media.Metadata.kMetaHeaderSize)) {
            parcel.setDataPosition(pin);
            return false;
        }
        mParcel = parcel;
        return true;
    }

    /**
     *
     *
     * @return The set of metadata ID found.
     */
    public java.util.Set<java.lang.Integer> keySet() {
        return mKeyToPosMap.keySet();
    }

    /**
     *
     *
     * @return true if a value is present for the given key.
     */
    public boolean has(final int metadataId) {
        if (!checkMetadataId(metadataId)) {
            throw new java.lang.IllegalArgumentException("Invalid key: " + metadataId);
        }
        return mKeyToPosMap.containsKey(metadataId);
    }

    // Accessors.
    // Caller must make sure the key is present using the {@code has}
    // method otherwise a RuntimeException will occur.
    /**
     * {@hide }
     */
    public java.lang.String getString(final int key) {
        checkType(key, android.media.Metadata.STRING_VAL);
        return mParcel.readString();
    }

    /**
     * {@hide }
     */
    public int getInt(final int key) {
        checkType(key, android.media.Metadata.INTEGER_VAL);
        return mParcel.readInt();
    }

    /**
     * Get the boolean value indicated by key
     */
    public boolean getBoolean(final int key) {
        checkType(key, android.media.Metadata.BOOLEAN_VAL);
        return mParcel.readInt() == 1;
    }

    /**
     * {@hide }
     */
    public long getLong(final int key) {
        checkType(key, android.media.Metadata.LONG_VAL);/**
         * {@hide }
         */

        return mParcel.readLong();
    }

    /**
     * {@hide }
     */
    public double getDouble(final int key) {
        checkType(key, android.media.Metadata.DOUBLE_VAL);
        return mParcel.readDouble();
    }

    /**
     * {@hide }
     */
    public byte[] getByteArray(final int key) {
        checkType(key, android.media.Metadata.BYTE_ARRAY_VAL);
        return mParcel.createByteArray();
    }

    /**
     * {@hide }
     */
    public java.util.Date getDate(final int key) {
        checkType(key, android.media.Metadata.DATE_VAL);
        final long timeSinceEpoch = mParcel.readLong();
        final java.lang.String timeZone = mParcel.readString();
        if (timeZone.length() == 0) {
            return new java.util.Date(timeSinceEpoch);
        } else {
            java.util.TimeZone tz = java.util.TimeZone.getTimeZone(timeZone);
            java.util.Calendar cal = java.util.Calendar.getInstance(tz);
            cal.setTimeInMillis(timeSinceEpoch);
            return cal.getTime();
        }
    }

    /**
     *
     *
     * @return the last available system metadata id. Ids are
    1-indexed.
    {@hide }
     */
    public static int lastSytemId() {
        return android.media.Metadata.LAST_SYSTEM;
    }

    /**
     *
     *
     * @return the first available cutom metadata id.
    {@hide }
     */
    public static int firstCustomId() {
        return android.media.Metadata.FIRST_CUSTOM;
    }

    /**
     *
     *
     * @return the last value of known type. Types are 1-indexed.
    {@hide }
     */
    public static int lastType() {
        return android.media.Metadata.LAST_TYPE;
    }

    /**
     * Check val is either a system id or a custom one.
     *
     * @param val
     * 		Metadata key to test.
     * @return true if it is in a valid range.
     */
    private boolean checkMetadataId(final int val) {
        if ((val <= android.media.Metadata.ANY) || ((android.media.Metadata.LAST_SYSTEM < val) && (val < android.media.Metadata.FIRST_CUSTOM))) {
            android.util.Log.e(android.media.Metadata.TAG, "Invalid metadata ID " + val);
            return false;
        }
        return true;
    }

    /**
     * Check the type of the data match what is expected.
     */
    private void checkType(final int key, final int expectedType) {
        final int pos = mKeyToPosMap.get(key);
        mParcel.setDataPosition(pos);
        final int type = mParcel.readInt();
        if (type != expectedType) {
            throw new java.lang.IllegalStateException((("Wrong type " + expectedType) + " but got ") + type);
        }
    }
}

