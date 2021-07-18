/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * An abstract class for editing and storing metadata that can be published by
 * {@link RemoteControlClient}. See the {@link RemoteControlClient#editMetadata(boolean)}
 * method to instantiate a {@link RemoteControlClient.MetadataEditor} object.
 *
 * @deprecated Use {@link MediaMetadata} instead together with {@link MediaSession}.
 */
@java.lang.Deprecated
public abstract class MediaMetadataEditor {
    private static final java.lang.String TAG = "MediaMetadataEditor";

    /**
     *
     *
     * @unknown 
     */
    protected MediaMetadataEditor() {
    }

    // Public keys for metadata used by RemoteControlClient and RemoteController.
    // Note that these keys are defined here, and not in MediaMetadataRetriever
    // because they are not supported by the MediaMetadataRetriever features.
    /**
     * The metadata key for the content artwork / album art.
     */
    public static final int BITMAP_KEY_ARTWORK = android.media.RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK;

    /**
     * The metadata key for the content's average rating, not the user's rating.
     * The value associated with this key is a {@link Rating} instance.
     *
     * @see #RATING_KEY_BY_USER
     */
    public static final int RATING_KEY_BY_OTHERS = 101;

    /**
     * The metadata key for the content's user rating.
     * The value associated with this key is a {@link Rating} instance.
     * This key can be flagged as "editable" (with {@link #addEditableKey(int)}) to enable
     * receiving user rating values through the
     * {@link android.media.RemoteControlClient.OnMetadataUpdateListener} interface.
     */
    public static final int RATING_KEY_BY_USER = 0x10000001;

    /**
     *
     *
     * @unknown Editable key mask
     */
    public static final int KEY_EDITABLE_MASK = 0x1fffffff;

    /**
     * Applies all of the metadata changes that have been set since the MediaMetadataEditor instance
     * was created or since {@link #clear()} was called. Subclasses should synchronize on
     * {@code this} for thread safety.
     */
    public abstract void apply();

    /**
     *
     *
     * @unknown Mask of editable keys.
     */
    protected long mEditableKeys;

    /**
     *
     *
     * @unknown 
     */
    protected boolean mMetadataChanged = false;

    /**
     *
     *
     * @unknown 
     */
    protected boolean mApplied = false;

    /**
     *
     *
     * @unknown 
     */
    protected boolean mArtworkChanged = false;

    /**
     *
     *
     * @unknown 
     */
    protected android.graphics.Bitmap mEditorArtwork;

    /**
     *
     *
     * @unknown 
     */
    protected android.os.Bundle mEditorMetadata;

    /**
     *
     *
     * @unknown 
     */
    protected android.media.MediaMetadata.Builder mMetadataBuilder;

    /**
     * Clears all the pending metadata changes set since the MediaMetadataEditor instance was
     * created or since this method was last called.
     * Note that clearing the metadata doesn't reset the editable keys
     * (use {@link #removeEditableKeys()} instead).
     */
    public synchronized void clear() {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't clear a previously applied MediaMetadataEditor");
            return;
        }
        mEditorMetadata.clear();
        mEditorArtwork = null;
        mMetadataBuilder = new android.media.MediaMetadata.Builder();
    }

    /**
     * Flags the given key as being editable.
     * This should only be used by metadata publishers, such as {@link RemoteControlClient},
     * which will declare the metadata field as eligible to be updated, with new values
     * received through the {@link RemoteControlClient.OnMetadataUpdateListener} interface.
     *
     * @param key
     * 		the type of metadata that can be edited. The supported key is
     * 		{@link #RATING_KEY_BY_USER}.
     */
    public synchronized void addEditableKey(int key) {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't change editable keys of a previously applied MetadataEditor");
            return;
        }
        // only one editable key at the moment, so we're not wasting memory on an array
        // of editable keys to check the validity of the key, just hardcode the supported key.
        if (key == android.media.MediaMetadataEditor.RATING_KEY_BY_USER) {
            mEditableKeys |= android.media.MediaMetadataEditor.KEY_EDITABLE_MASK & key;
            mMetadataChanged = true;
        } else {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, ("Metadata key " + key) + " cannot be edited");
        }
    }

    /**
     * Causes all metadata fields to be read-only.
     */
    public synchronized void removeEditableKeys() {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't remove all editable keys of a previously applied MetadataEditor");
            return;
        }
        if (mEditableKeys != 0) {
            mEditableKeys = 0;
            mMetadataChanged = true;
        }
    }

    /**
     * Retrieves the keys flagged as editable.
     *
     * @return null if there are no editable keys, or an array containing the keys.
     */
    public synchronized int[] getEditableKeys() {
        // only one editable key supported here
        if (mEditableKeys == android.media.MediaMetadataEditor.RATING_KEY_BY_USER) {
            int[] keys = new int[]{ android.media.MediaMetadataEditor.RATING_KEY_BY_USER };
            return keys;
        } else {
            return null;
        }
    }

    /**
     * Adds textual information.
     * Note that none of the information added after {@link #apply()} has been called,
     * will be available to consumers of metadata stored by the MediaMetadataEditor.
     *
     * @param key
     * 		The identifier of a the metadata field to set. Valid values are
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_ALBUM},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_ALBUMARTIST},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_TITLE},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_ARTIST},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_AUTHOR},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_COMPILATION},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_COMPOSER},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DATE},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_GENRE},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_WRITER}.
     * @param value
     * 		The text for the given key, or {@code null} to signify there is no valid
     * 		information for the field.
     * @return Returns a reference to the same MediaMetadataEditor object, so you can chain put
    calls together.
     */
    public synchronized android.media.MediaMetadataEditor putString(int key, java.lang.String value) throws java.lang.IllegalArgumentException {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        }
        if (android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.get(key, android.media.MediaMetadataEditor.METADATA_TYPE_INVALID) != android.media.MediaMetadataEditor.METADATA_TYPE_STRING) {
            throw new java.lang.IllegalArgumentException("Invalid type 'String' for key " + key);
        }
        mEditorMetadata.putString(java.lang.String.valueOf(key), value);
        mMetadataChanged = true;
        return this;
    }

    /**
     * Adds numerical information.
     * Note that none of the information added after {@link #apply()} has been called
     * will be available to consumers of metadata stored by the MediaMetadataEditor.
     *
     * @param key
     * 		the identifier of a the metadata field to set. Valid values are
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_CD_TRACK_NUMBER},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DISC_NUMBER},
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_DURATION} (with a value
     * 		expressed in milliseconds),
     * 		{@link android.media.MediaMetadataRetriever#METADATA_KEY_YEAR}.
     * @param value
     * 		The long value for the given key
     * @return Returns a reference to the same MediaMetadataEditor object, so you can chain put
    calls together.
     * @throws IllegalArgumentException
     * 		
     */
    public synchronized android.media.MediaMetadataEditor putLong(int key, long value) throws java.lang.IllegalArgumentException {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        }
        if (android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.get(key, android.media.MediaMetadataEditor.METADATA_TYPE_INVALID) != android.media.MediaMetadataEditor.METADATA_TYPE_LONG) {
            throw new java.lang.IllegalArgumentException("Invalid type 'long' for key " + key);
        }
        mEditorMetadata.putLong(java.lang.String.valueOf(key), value);
        mMetadataChanged = true;
        return this;
    }

    /**
     * Adds image.
     *
     * @param key
     * 		the identifier of the bitmap to set. The only valid value is
     * 		{@link #BITMAP_KEY_ARTWORK}
     * @param bitmap
     * 		The bitmap for the artwork, or null if there isn't any.
     * @return Returns a reference to the same MediaMetadataEditor object, so you can chain put
    calls together.
     * @throws IllegalArgumentException
     * 		
     * @see android.graphics.Bitmap
     */
    public synchronized android.media.MediaMetadataEditor putBitmap(int key, android.graphics.Bitmap bitmap) throws java.lang.IllegalArgumentException {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        }
        if (key != android.media.MediaMetadataEditor.BITMAP_KEY_ARTWORK) {
            throw new java.lang.IllegalArgumentException("Invalid type 'Bitmap' for key " + key);
        }
        mEditorArtwork = bitmap;
        mArtworkChanged = true;
        return this;
    }

    /**
     * Adds information stored as an instance.
     * Note that none of the information added after {@link #apply()} has been called
     * will be available to consumers of metadata stored by the MediaMetadataEditor.
     *
     * @param key
     * 		the identifier of a the metadata field to set. Valid keys for a:
     * 		<ul>
     * 		<li>{@link Bitmap} object are {@link #BITMAP_KEY_ARTWORK},</li>
     * 		<li>{@link String} object are the same as for {@link #putString(int, String)}</li>
     * 		<li>{@link Long} object are the same as for {@link #putLong(int, long)}</li>
     * 		<li>{@link Rating} object are {@link #RATING_KEY_BY_OTHERS}
     * 		and {@link #RATING_KEY_BY_USER}.</li>
     * 		</ul>
     * @param value
     * 		the metadata to add.
     * @return Returns a reference to the same MediaMetadataEditor object, so you can chain put
    calls together.
     * @throws IllegalArgumentException
     * 		
     */
    public synchronized android.media.MediaMetadataEditor putObject(int key, java.lang.Object value) throws java.lang.IllegalArgumentException {
        if (mApplied) {
            android.util.Log.e(android.media.MediaMetadataEditor.TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        }
        switch (android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.get(key, android.media.MediaMetadataEditor.METADATA_TYPE_INVALID)) {
            case android.media.MediaMetadataEditor.METADATA_TYPE_LONG :
                if (value instanceof java.lang.Long) {
                    return putLong(key, ((java.lang.Long) (value)).longValue());
                } else {
                    throw new java.lang.IllegalArgumentException("Not a non-null Long for key " + key);
                }
            case android.media.MediaMetadataEditor.METADATA_TYPE_STRING :
                if ((value == null) || (value instanceof java.lang.String)) {
                    return putString(key, ((java.lang.String) (value)));
                } else {
                    throw new java.lang.IllegalArgumentException("Not a String for key " + key);
                }
            case android.media.MediaMetadataEditor.METADATA_TYPE_RATING :
                mEditorMetadata.putParcelable(java.lang.String.valueOf(key), ((android.os.Parcelable) (value)));
                mMetadataChanged = true;
                break;
            case android.media.MediaMetadataEditor.METADATA_TYPE_BITMAP :
                if ((value == null) || (value instanceof android.graphics.Bitmap)) {
                    return putBitmap(key, ((android.graphics.Bitmap) (value)));
                } else {
                    throw new java.lang.IllegalArgumentException("Not a Bitmap for key " + key);
                }
            default :
                throw new java.lang.IllegalArgumentException("Invalid key " + key);
        }
        return this;
    }

    /**
     * Returns the long value for the key.
     *
     * @param key
     * 		one of the keys supported in {@link #putLong(int, long)}
     * @param defaultValue
     * 		the value returned if the key is not present
     * @return the long value for the key, or the supplied default value if the key is not present
     * @throws IllegalArgumentException
     * 		
     */
    public synchronized long getLong(int key, long defaultValue) throws java.lang.IllegalArgumentException {
        if (android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.get(key, android.media.MediaMetadataEditor.METADATA_TYPE_INVALID) != android.media.MediaMetadataEditor.METADATA_TYPE_LONG) {
            throw new java.lang.IllegalArgumentException("Invalid type 'long' for key " + key);
        }
        return mEditorMetadata.getLong(java.lang.String.valueOf(key), defaultValue);
    }

    /**
     * Returns the {@link String} value for the key.
     *
     * @param key
     * 		one of the keys supported in {@link #putString(int, String)}
     * @param defaultValue
     * 		the value returned if the key is not present
     * @return the {@link String} value for the key, or the supplied default value if the key is
    not present
     * @throws IllegalArgumentException
     * 		
     */
    public synchronized java.lang.String getString(int key, java.lang.String defaultValue) throws java.lang.IllegalArgumentException {
        if (android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.get(key, android.media.MediaMetadataEditor.METADATA_TYPE_INVALID) != android.media.MediaMetadataEditor.METADATA_TYPE_STRING) {
            throw new java.lang.IllegalArgumentException("Invalid type 'String' for key " + key);
        }
        return mEditorMetadata.getString(java.lang.String.valueOf(key), defaultValue);
    }

    /**
     * Returns the {@link Bitmap} value for the key.
     *
     * @param key
     * 		the {@link #BITMAP_KEY_ARTWORK} key
     * @param defaultValue
     * 		the value returned if the key is not present
     * @return the {@link Bitmap} value for the key, or the supplied default value if the key is
    not present
     * @throws IllegalArgumentException
     * 		
     */
    public synchronized android.graphics.Bitmap getBitmap(int key, android.graphics.Bitmap defaultValue) throws java.lang.IllegalArgumentException {
        if (key != android.media.MediaMetadataEditor.BITMAP_KEY_ARTWORK) {
            throw new java.lang.IllegalArgumentException("Invalid type 'Bitmap' for key " + key);
        }
        return mEditorArtwork != null ? mEditorArtwork : defaultValue;
    }

    /**
     * Returns an object representation of the value for the key
     *
     * @param key
     * 		one of the keys supported in {@link #putObject(int, Object)}
     * @param defaultValue
     * 		the value returned if the key is not present
     * @return the object for the key, as a {@link Long}, {@link Bitmap}, {@link String}, or
    {@link Rating} depending on the key value, or the supplied default value if the key is
    not present
     * @throws IllegalArgumentException
     * 		
     */
    public synchronized java.lang.Object getObject(int key, java.lang.Object defaultValue) throws java.lang.IllegalArgumentException {
        switch (android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.get(key, android.media.MediaMetadataEditor.METADATA_TYPE_INVALID)) {
            case android.media.MediaMetadataEditor.METADATA_TYPE_LONG :
                if (mEditorMetadata.containsKey(java.lang.String.valueOf(key))) {
                    return mEditorMetadata.getLong(java.lang.String.valueOf(key));
                } else {
                    return defaultValue;
                }
            case android.media.MediaMetadataEditor.METADATA_TYPE_STRING :
                if (mEditorMetadata.containsKey(java.lang.String.valueOf(key))) {
                    return mEditorMetadata.getString(java.lang.String.valueOf(key));
                } else {
                    return defaultValue;
                }
            case android.media.MediaMetadataEditor.METADATA_TYPE_RATING :
                if (mEditorMetadata.containsKey(java.lang.String.valueOf(key))) {
                    return mEditorMetadata.getParcelable(java.lang.String.valueOf(key));
                } else {
                    return defaultValue;
                }
            case android.media.MediaMetadataEditor.METADATA_TYPE_BITMAP :
                // only one key for Bitmap supported, value is not stored in mEditorMetadata Bundle
                if (key == android.media.MediaMetadataEditor.BITMAP_KEY_ARTWORK) {
                    return mEditorArtwork != null ? mEditorArtwork : defaultValue;
                }// else: fall through to invalid key handling

            default :
                throw new java.lang.IllegalArgumentException("Invalid key " + key);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected static final int METADATA_TYPE_INVALID = -1;

    /**
     *
     *
     * @unknown 
     */
    protected static final int METADATA_TYPE_LONG = 0;

    /**
     *
     *
     * @unknown 
     */
    protected static final int METADATA_TYPE_STRING = 1;

    /**
     *
     *
     * @unknown 
     */
    protected static final int METADATA_TYPE_BITMAP = 2;

    /**
     *
     *
     * @unknown 
     */
    protected static final int METADATA_TYPE_RATING = 3;

    /**
     *
     *
     * @unknown 
     */
    protected static final android.util.SparseIntArray METADATA_KEYS_TYPE;

    static {
        METADATA_KEYS_TYPE = new android.util.SparseIntArray(17);
        // NOTE: if adding to the list below, make sure you increment the array initialization size
        // keys with long values
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER, android.media.MediaMetadataEditor.METADATA_TYPE_LONG);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER, android.media.MediaMetadataEditor.METADATA_TYPE_LONG);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION, android.media.MediaMetadataEditor.METADATA_TYPE_LONG);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_YEAR, android.media.MediaMetadataEditor.METADATA_TYPE_LONG);
        // keys with String values
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_TITLE, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_AUTHOR, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_COMPILATION, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_COMPOSER, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_DATE, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_GENRE, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataRetriever.METADATA_KEY_WRITER, android.media.MediaMetadataEditor.METADATA_TYPE_STRING);
        // keys with Bitmap values
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataEditor.BITMAP_KEY_ARTWORK, android.media.MediaMetadataEditor.METADATA_TYPE_BITMAP);
        // keys with Rating values
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataEditor.RATING_KEY_BY_OTHERS, android.media.MediaMetadataEditor.METADATA_TYPE_RATING);
        android.media.MediaMetadataEditor.METADATA_KEYS_TYPE.put(android.media.MediaMetadataEditor.RATING_KEY_BY_USER, android.media.MediaMetadataEditor.METADATA_TYPE_RATING);
    }
}

