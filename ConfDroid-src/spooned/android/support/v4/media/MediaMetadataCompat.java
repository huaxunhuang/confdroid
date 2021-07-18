/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v4.media;


/**
 * Contains metadata about an item, such as the title, artist, etc.
 */
public final class MediaMetadataCompat implements android.os.Parcelable {
    private static final java.lang.String TAG = "MediaMetadata";

    /**
     * The title of the media.
     */
    public static final java.lang.String METADATA_KEY_TITLE = "android.media.metadata.TITLE";

    /**
     * The artist of the media.
     */
    public static final java.lang.String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";

    /**
     * The duration of the media in ms. A negative duration indicates that the
     * duration is unknown (or infinite).
     */
    public static final java.lang.String METADATA_KEY_DURATION = "android.media.metadata.DURATION";

    /**
     * The album title for the media.
     */
    public static final java.lang.String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";

    /**
     * The author of the media.
     */
    public static final java.lang.String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";

    /**
     * The writer of the media.
     */
    public static final java.lang.String METADATA_KEY_WRITER = "android.media.metadata.WRITER";

    /**
     * The composer of the media.
     */
    public static final java.lang.String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";

    /**
     * The compilation status of the media.
     */
    public static final java.lang.String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";

    /**
     * The date the media was created or published. The format is unspecified
     * but RFC 3339 is recommended.
     */
    public static final java.lang.String METADATA_KEY_DATE = "android.media.metadata.DATE";

    /**
     * The year the media was created or published as a long.
     */
    public static final java.lang.String METADATA_KEY_YEAR = "android.media.metadata.YEAR";

    /**
     * The genre of the media.
     */
    public static final java.lang.String METADATA_KEY_GENRE = "android.media.metadata.GENRE";

    /**
     * The track number for the media.
     */
    public static final java.lang.String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";

    /**
     * The number of tracks in the media's original source.
     */
    public static final java.lang.String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";

    /**
     * The disc number for the media's original source.
     */
    public static final java.lang.String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";

    /**
     * The artist for the album of the media's original source.
     */
    public static final java.lang.String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";

    /**
     * The artwork for the media as a {@link Bitmap}.
     *
     * The artwork should be relatively small and may be scaled down
     * if it is too large. For higher resolution artwork
     * {@link #METADATA_KEY_ART_URI} should be used instead.
     */
    public static final java.lang.String METADATA_KEY_ART = "android.media.metadata.ART";

    /**
     * The artwork for the media as a Uri style String.
     */
    public static final java.lang.String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";

    /**
     * The artwork for the album of the media's original source as a
     * {@link Bitmap}.
     * The artwork should be relatively small and may be scaled down
     * if it is too large. For higher resolution artwork
     * {@link #METADATA_KEY_ALBUM_ART_URI} should be used instead.
     */
    public static final java.lang.String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";

    /**
     * The artwork for the album of the media's original source as a Uri style
     * String.
     */
    public static final java.lang.String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";

    /**
     * The user's rating for the media.
     *
     * @see RatingCompat
     */
    public static final java.lang.String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";

    /**
     * The overall rating for the media.
     *
     * @see RatingCompat
     */
    public static final java.lang.String METADATA_KEY_RATING = "android.media.metadata.RATING";

    /**
     * A title that is suitable for display to the user. This will generally be
     * the same as {@link #METADATA_KEY_TITLE} but may differ for some formats.
     * When displaying media described by this metadata this should be preferred
     * if present.
     */
    public static final java.lang.String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";

    /**
     * A subtitle that is suitable for display to the user. When displaying a
     * second line for media described by this metadata this should be preferred
     * to other fields if present.
     */
    public static final java.lang.String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";

    /**
     * A description that is suitable for display to the user. When displaying
     * more information for media described by this metadata this should be
     * preferred to other fields if present.
     */
    public static final java.lang.String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";

    /**
     * An icon or thumbnail that is suitable for display to the user. When
     * displaying an icon for media described by this metadata this should be
     * preferred to other fields if present. This must be a {@link Bitmap}.
     *
     * The icon should be relatively small and may be scaled down
     * if it is too large. For higher resolution artwork
     * {@link #METADATA_KEY_DISPLAY_ICON_URI} should be used instead.
     */
    public static final java.lang.String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";

    /**
     * An icon or thumbnail that is suitable for display to the user. When
     * displaying more information for media described by this metadata the
     * display description should be preferred to other fields when present.
     * This must be a Uri style String.
     */
    public static final java.lang.String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";

    /**
     * A String key for identifying the content. This value is specific to the
     * service providing the content. If used, this should be a persistent
     * unique key for the underlying content.
     */
    public static final java.lang.String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";

    /**
     * A Uri formatted String representing the content. This value is specific to the
     * service providing the content. It may be used with
     * {@link MediaControllerCompat.TransportControls#playFromUri(Uri, Bundle)}
     * to initiate playback when provided by a {@link MediaBrowserCompat} connected to
     * the same app.
     */
    public static final java.lang.String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";

    /**
     * The bluetooth folder type of the media specified in the section 6.10.2.2 of the Bluetooth
     * AVRCP 1.5. It should be one of the following:
     * <ul>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_MIXED}</li>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_TITLES}</li>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_ALBUMS}</li>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_ARTISTS}</li>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_GENRES}</li>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_PLAYLISTS}</li>
     * <li>{@link MediaDescriptionCompat#BT_FOLDER_TYPE_YEARS}</li>
     * </ul>
     */
    public static final java.lang.String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.StringDef({ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_AUTHOR, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_WRITER, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_COMPOSER, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_COMPILATION, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DATE, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_GENRE, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART_URI, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface TextKey {}

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.StringDef({ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_YEAR, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface LongKey {}

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.StringDef({ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BitmapKey {}

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.StringDef({ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_USER_RATING, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_RATING })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface RatingKey {}

    static final int METADATA_TYPE_LONG = 0;

    static final int METADATA_TYPE_TEXT = 1;

    static final int METADATA_TYPE_BITMAP = 2;

    static final int METADATA_TYPE_RATING = 3;

    static final android.support.v4.util.ArrayMap<java.lang.String, java.lang.Integer> METADATA_KEYS_TYPE;

    static {
        METADATA_KEYS_TYPE = new android.support.v4.util.ArrayMap<java.lang.String, java.lang.Integer>();
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_AUTHOR, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_WRITER, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_COMPOSER, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_COMPILATION, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DATE, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_YEAR, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_GENRE, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_BITMAP);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART_URI, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_BITMAP);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_USER_RATING, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_RATING);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_RATING, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_RATING);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_BITMAP);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG);
        android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.put(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI, android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT);
    }

    @android.support.v4.media.MediaMetadataCompat.TextKey
    private static final java.lang.String[] PREFERRED_DESCRIPTION_ORDER = new java.lang.String[]{ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_WRITER, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_AUTHOR, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_COMPOSER };

    @android.support.v4.media.MediaMetadataCompat.BitmapKey
    private static final java.lang.String[] PREFERRED_BITMAP_ORDER = new java.lang.String[]{ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART };

    @android.support.v4.media.MediaMetadataCompat.TextKey
    private static final java.lang.String[] PREFERRED_URI_ORDER = new java.lang.String[]{ android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART_URI, android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI };

    final android.os.Bundle mBundle;

    private java.lang.Object mMetadataObj;

    private android.support.v4.media.MediaDescriptionCompat mDescription;

    MediaMetadataCompat(android.os.Bundle bundle) {
        mBundle = new android.os.Bundle(bundle);
    }

    MediaMetadataCompat(android.os.Parcel in) {
        mBundle = in.readBundle();
    }

    /**
     * Returns true if the given key is contained in the metadata
     *
     * @param key
     * 		a String key
     * @return true if the key exists in this metadata, false otherwise
     */
    public boolean containsKey(java.lang.String key) {
        return mBundle.containsKey(key);
    }

    /**
     * Returns the value associated with the given key, or null if no mapping of
     * the desired type exists for the given key or a null value is explicitly
     * associated with the key.
     *
     * @param key
     * 		The key the value is stored under
     * @return a CharSequence value, or null
     */
    public java.lang.CharSequence getText(@android.support.v4.media.MediaMetadataCompat.TextKey
    java.lang.String key) {
        return mBundle.getCharSequence(key);
    }

    /**
     * Returns the value associated with the given key, or null if no mapping of
     * the desired type exists for the given key or a null value is explicitly
     * associated with the key.
     *
     * @param key
     * 		The key the value is stored under
     * @return a String value, or null
     */
    public java.lang.String getString(@android.support.v4.media.MediaMetadataCompat.TextKey
    java.lang.String key) {
        java.lang.CharSequence text = mBundle.getCharSequence(key);
        if (text != null) {
            return text.toString();
        }
        return null;
    }

    /**
     * Returns the value associated with the given key, or 0L if no long exists
     * for the given key.
     *
     * @param key
     * 		The key the value is stored under
     * @return a long value
     */
    public long getLong(@android.support.v4.media.MediaMetadataCompat.LongKey
    java.lang.String key) {
        return mBundle.getLong(key, 0);
    }

    /**
     * Return a {@link RatingCompat} for the given key or null if no rating exists for
     * the given key.
     *
     * @param key
     * 		The key the value is stored under
     * @return A {@link RatingCompat} or null
     */
    public android.support.v4.media.RatingCompat getRating(@android.support.v4.media.MediaMetadataCompat.RatingKey
    java.lang.String key) {
        android.support.v4.media.RatingCompat rating = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                // On platform version 19 or higher, mBundle stores a Rating object. Convert it to
                // RatingCompat.
                rating = android.support.v4.media.RatingCompat.fromRating(mBundle.getParcelable(key));
            } else {
                rating = mBundle.getParcelable(key);
            }
        } catch (java.lang.Exception e) {
            // ignore, value was not a bitmap
            android.util.Log.w(android.support.v4.media.MediaMetadataCompat.TAG, "Failed to retrieve a key as Rating.", e);
        }
        return rating;
    }

    /**
     * Return a {@link Bitmap} for the given key or null if no bitmap exists for
     * the given key.
     *
     * @param key
     * 		The key the value is stored under
     * @return A {@link Bitmap} or null
     */
    public android.graphics.Bitmap getBitmap(@android.support.v4.media.MediaMetadataCompat.BitmapKey
    java.lang.String key) {
        android.graphics.Bitmap bmp = null;
        try {
            bmp = mBundle.getParcelable(key);
        } catch (java.lang.Exception e) {
            // ignore, value was not a bitmap
            android.util.Log.w(android.support.v4.media.MediaMetadataCompat.TAG, "Failed to retrieve a key as Bitmap.", e);
        }
        return bmp;
    }

    /**
     * Returns a simple description of this metadata for display purposes.
     *
     * @return A simple description of this metadata.
     */
    public android.support.v4.media.MediaDescriptionCompat getDescription() {
        if (mDescription != null) {
            return mDescription;
        }
        java.lang.String mediaId = getString(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
        java.lang.CharSequence[] text = new java.lang.CharSequence[3];
        android.graphics.Bitmap icon = null;
        android.net.Uri iconUri = null;
        // First handle the case where display data is set already
        java.lang.CharSequence displayText = getText(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE);
        if (!android.text.TextUtils.isEmpty(displayText)) {
            // If they have a display title use only display data, otherwise use
            // our best bets
            text[0] = displayText;
            text[1] = getText(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE);
            text[2] = getText(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION);
        } else {
            // Use whatever fields we can
            int textIndex = 0;
            int keyIndex = 0;
            while ((textIndex < text.length) && (keyIndex < android.support.v4.media.MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER.length)) {
                java.lang.CharSequence next = getText(android.support.v4.media.MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER[keyIndex++]);
                if (!android.text.TextUtils.isEmpty(next)) {
                    // Fill in the next empty bit of text
                    text[textIndex++] = next;
                }
            } 
        }
        // Get the best art bitmap we can find
        for (int i = 0; i < android.support.v4.media.MediaMetadataCompat.PREFERRED_BITMAP_ORDER.length; i++) {
            android.graphics.Bitmap next = getBitmap(android.support.v4.media.MediaMetadataCompat.PREFERRED_BITMAP_ORDER[i]);
            if (next != null) {
                icon = next;
                break;
            }
        }
        // Get the best Uri we can find
        for (int i = 0; i < android.support.v4.media.MediaMetadataCompat.PREFERRED_URI_ORDER.length; i++) {
            java.lang.String next = getString(android.support.v4.media.MediaMetadataCompat.PREFERRED_URI_ORDER[i]);
            if (!android.text.TextUtils.isEmpty(next)) {
                iconUri = android.net.Uri.parse(next);
                break;
            }
        }
        android.net.Uri mediaUri = null;
        java.lang.String mediaUriStr = getString(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI);
        if (!android.text.TextUtils.isEmpty(mediaUriStr)) {
            mediaUri = android.net.Uri.parse(mediaUriStr);
        }
        android.support.v4.media.MediaDescriptionCompat.Builder bob = new android.support.v4.media.MediaDescriptionCompat.Builder();
        bob.setMediaId(mediaId);
        bob.setTitle(text[0]);
        bob.setSubtitle(text[1]);
        bob.setDescription(text[2]);
        bob.setIconBitmap(icon);
        bob.setIconUri(iconUri);
        bob.setMediaUri(mediaUri);
        if (mBundle.containsKey(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE)) {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putLong(android.support.v4.media.MediaDescriptionCompat.EXTRA_BT_FOLDER_TYPE, getLong(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE));
            bob.setExtras(bundle);
        }
        mDescription = bob.build();
        return mDescription;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeBundle(mBundle);
    }

    /**
     * Get the number of fields in this metadata.
     *
     * @return The number of fields in the metadata.
     */
    public int size() {
        return mBundle.size();
    }

    /**
     * Returns a Set containing the Strings used as keys in this metadata.
     *
     * @return a Set of String keys
     */
    public java.util.Set<java.lang.String> keySet() {
        return mBundle.keySet();
    }

    /**
     * Gets the bundle backing the metadata object. This is available to support
     * backwards compatibility. Apps should not modify the bundle directly.
     *
     * @return The Bundle backing this metadata.
     */
    public android.os.Bundle getBundle() {
        return mBundle;
    }

    /**
     * Creates an instance from a framework {@link android.media.MediaMetadata}
     * object.
     * <p>
     * This method is only supported on
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP} and later.
     * </p>
     *
     * @param metadataObj
     * 		A {@link android.media.MediaMetadata} object, or null
     * 		if none.
     * @return An equivalent {@link MediaMetadataCompat} object, or null if
    none.
     */
    public static android.support.v4.media.MediaMetadataCompat fromMediaMetadata(java.lang.Object metadataObj) {
        if ((metadataObj == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return null;
        }
        android.os.Parcel p = android.os.Parcel.obtain();
        android.support.v4.media.MediaMetadataCompatApi21.writeToParcel(metadataObj, p, 0);
        p.setDataPosition(0);
        android.support.v4.media.MediaMetadataCompat metadata = android.support.v4.media.MediaMetadataCompat.CREATOR.createFromParcel(p);
        p.recycle();
        metadata.mMetadataObj = metadataObj;
        return metadata;
    }

    /**
     * Gets the underlying framework {@link android.media.MediaMetadata} object.
     * <p>
     * This method is only supported on
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP} and later.
     * </p>
     *
     * @return An equivalent {@link android.media.MediaMetadata} object, or null
    if none.
     */
    public java.lang.Object getMediaMetadata() {
        if ((mMetadataObj != null) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return mMetadataObj;
        }
        android.os.Parcel p = android.os.Parcel.obtain();
        writeToParcel(p, 0);
        p.setDataPosition(0);
        mMetadataObj = android.support.v4.media.MediaMetadataCompatApi21.createFromParcel(p);
        p.recycle();
        return mMetadataObj;
    }

    public static final android.os.Parcelable.Creator<android.support.v4.media.MediaMetadataCompat> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.MediaMetadataCompat>() {
        @java.lang.Override
        public android.support.v4.media.MediaMetadataCompat createFromParcel(android.os.Parcel in) {
            return new android.support.v4.media.MediaMetadataCompat(in);
        }

        @java.lang.Override
        public android.support.v4.media.MediaMetadataCompat[] newArray(int size) {
            return new android.support.v4.media.MediaMetadataCompat[size];
        }
    };

    /**
     * Use to build MediaMetadata objects. The system defined metadata keys must
     * use the appropriate data type.
     */
    public static final class Builder {
        private final android.os.Bundle mBundle;

        /**
         * Create an empty Builder. Any field that should be included in the
         * {@link MediaMetadataCompat} must be added.
         */
        public Builder() {
            mBundle = new android.os.Bundle();
        }

        /**
         * Create a Builder using a {@link MediaMetadataCompat} instance to set the
         * initial values. All fields in the source metadata will be included in
         * the new metadata. Fields can be overwritten by adding the same key.
         *
         * @param source
         * 		
         */
        public Builder(android.support.v4.media.MediaMetadataCompat source) {
            mBundle = new android.os.Bundle(source.mBundle);
        }

        /**
         * Create a Builder using a {@link MediaMetadataCompat} instance to set
         * initial values, but replace bitmaps with a scaled down copy if they
         * are larger than maxBitmapSize.
         * <p>
         * This also deep-copies the bitmaps for {@link #METADATA_KEY_ART} and
         * {@link #METADATA_KEY_ALBUM_ART} on
         * {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWITCH} and later
         * to prevent bitmaps from being recycled by RCC.
         *
         * @param source
         * 		The original metadata to copy.
         * @param maxBitmapSize
         * 		The maximum height/width for bitmaps contained
         * 		in the metadata.
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public Builder(android.support.v4.media.MediaMetadataCompat source, int maxBitmapSize) {
            this(source);
            for (java.lang.String key : mBundle.keySet()) {
                java.lang.Object value = mBundle.get(key);
                if ((value != null) && (value instanceof android.graphics.Bitmap)) {
                    android.graphics.Bitmap bmp = ((android.graphics.Bitmap) (value));
                    if ((bmp.getHeight() > maxBitmapSize) || (bmp.getWidth() > maxBitmapSize)) {
                        putBitmap(key, scaleBitmap(bmp, maxBitmapSize));
                    } else
                        if ((android.os.Build.VERSION.SDK_INT >= 14) && (key.equals(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ART) || key.equals(android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART))) {
                            putBitmap(key, bmp.copy(bmp.getConfig(), false));
                        }

                }
            }
        }

        /**
         * Put a CharSequence value into the metadata. Custom keys may be used,
         * but if the METADATA_KEYs defined in this class are used they may only
         * be one of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_TITLE}</li>
         * <li>{@link #METADATA_KEY_ARTIST}</li>
         * <li>{@link #METADATA_KEY_ALBUM}</li>
         * <li>{@link #METADATA_KEY_AUTHOR}</li>
         * <li>{@link #METADATA_KEY_WRITER}</li>
         * <li>{@link #METADATA_KEY_COMPOSER}</li>
         * <li>{@link #METADATA_KEY_DATE}</li>
         * <li>{@link #METADATA_KEY_GENRE}</li>
         * <li>{@link #METADATA_KEY_ALBUM_ARTIST}</li>
         * <li>{@link #METADATA_KEY_ART_URI}</li>
         * <li>{@link #METADATA_KEY_ALBUM_ART_URI}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_TITLE}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_SUBTITLE}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_DESCRIPTION}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_ICON_URI}</li>
         * </ul>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The CharSequence value to store
         * @return The Builder to allow chaining
         */
        public android.support.v4.media.MediaMetadataCompat.Builder putText(@android.support.v4.media.MediaMetadataCompat.TextKey
        java.lang.String key, java.lang.CharSequence value) {
            if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.get(key) != android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT) {
                    throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a CharSequence");
                }
            }
            mBundle.putCharSequence(key, value);
            return this;
        }

        /**
         * Put a String value into the metadata. Custom keys may be used, but if
         * the METADATA_KEYs defined in this class are used they may only be one
         * of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_TITLE}</li>
         * <li>{@link #METADATA_KEY_ARTIST}</li>
         * <li>{@link #METADATA_KEY_ALBUM}</li>
         * <li>{@link #METADATA_KEY_AUTHOR}</li>
         * <li>{@link #METADATA_KEY_WRITER}</li>
         * <li>{@link #METADATA_KEY_COMPOSER}</li>
         * <li>{@link #METADATA_KEY_DATE}</li>
         * <li>{@link #METADATA_KEY_GENRE}</li>
         * <li>{@link #METADATA_KEY_ALBUM_ARTIST}</li>
         * <li>{@link #METADATA_KEY_ART_URI}</li>
         * <li>{@link #METADATA_KEY_ALBUM_ART_URI}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_TITLE}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_SUBTITLE}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_DESCRIPTION}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_ICON_URI}</li>
         * </ul>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The String value to store
         * @return The Builder to allow chaining
         */
        public android.support.v4.media.MediaMetadataCompat.Builder putString(@android.support.v4.media.MediaMetadataCompat.TextKey
        java.lang.String key, java.lang.String value) {
            if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.get(key) != android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_TEXT) {
                    throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a String");
                }
            }
            mBundle.putCharSequence(key, value);
            return this;
        }

        /**
         * Put a long value into the metadata. Custom keys may be used, but if
         * the METADATA_KEYs defined in this class are used they may only be one
         * of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_DURATION}</li>
         * <li>{@link #METADATA_KEY_TRACK_NUMBER}</li>
         * <li>{@link #METADATA_KEY_NUM_TRACKS}</li>
         * <li>{@link #METADATA_KEY_DISC_NUMBER}</li>
         * <li>{@link #METADATA_KEY_YEAR}</li>
         * </ul>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The String value to store
         * @return The Builder to allow chaining
         */
        public android.support.v4.media.MediaMetadataCompat.Builder putLong(@android.support.v4.media.MediaMetadataCompat.LongKey
        java.lang.String key, long value) {
            if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.get(key) != android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_LONG) {
                    throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a long");
                }
            }
            mBundle.putLong(key, value);
            return this;
        }

        /**
         * Put a {@link RatingCompat} into the metadata. Custom keys may be used, but
         * if the METADATA_KEYs defined in this class are used they may only be
         * one of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_RATING}</li>
         * <li>{@link #METADATA_KEY_USER_RATING}</li>
         * </ul>
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The String value to store
         * @return The Builder to allow chaining
         */
        public android.support.v4.media.MediaMetadataCompat.Builder putRating(@android.support.v4.media.MediaMetadataCompat.RatingKey
        java.lang.String key, android.support.v4.media.RatingCompat value) {
            if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.get(key) != android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_RATING) {
                    throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a Rating");
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                // On platform version 19 or higher, use Rating instead of RatingCompat so mBundle
                // can be unmarshalled.
                mBundle.putParcelable(key, ((android.os.Parcelable) (value.getRating())));
            } else {
                mBundle.putParcelable(key, value);
            }
            return this;
        }

        /**
         * Put a {@link Bitmap} into the metadata. Custom keys may be used, but
         * if the METADATA_KEYs defined in this class are used they may only be
         * one of the following:
         * <ul>
         * <li>{@link #METADATA_KEY_ART}</li>
         * <li>{@link #METADATA_KEY_ALBUM_ART}</li>
         * <li>{@link #METADATA_KEY_DISPLAY_ICON}</li>
         * </ul>
         * Large bitmaps may be scaled down when
         * {@link android.support.v4.media.session.MediaSessionCompat#setMetadata} is called.
         * To pass full resolution images {@link Uri Uris} should be used with
         * {@link #putString}.
         *
         * @param key
         * 		The key for referencing this value
         * @param value
         * 		The Bitmap to store
         * @return The Builder to allow chaining
         */
        public android.support.v4.media.MediaMetadataCompat.Builder putBitmap(@android.support.v4.media.MediaMetadataCompat.BitmapKey
        java.lang.String key, android.graphics.Bitmap value) {
            if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(key)) {
                if (android.support.v4.media.MediaMetadataCompat.METADATA_KEYS_TYPE.get(key) != android.support.v4.media.MediaMetadataCompat.METADATA_TYPE_BITMAP) {
                    throw new java.lang.IllegalArgumentException(("The " + key) + " key cannot be used to put a Bitmap");
                }
            }
            mBundle.putParcelable(key, value);
            return this;
        }

        /**
         * Creates a {@link MediaMetadataCompat} instance with the specified fields.
         *
         * @return The new MediaMetadata instance
         */
        public android.support.v4.media.MediaMetadataCompat build() {
            return new android.support.v4.media.MediaMetadataCompat(mBundle);
        }

        private android.graphics.Bitmap scaleBitmap(android.graphics.Bitmap bmp, int maxSize) {
            float maxSizeF = maxSize;
            float widthScale = maxSizeF / bmp.getWidth();
            float heightScale = maxSizeF / bmp.getHeight();
            float scale = java.lang.Math.min(widthScale, heightScale);
            int height = ((int) (bmp.getHeight() * scale));
            int width = ((int) (bmp.getWidth() * scale));
            return android.graphics.Bitmap.createScaledBitmap(bmp, width, height, true);
        }
    }
}

