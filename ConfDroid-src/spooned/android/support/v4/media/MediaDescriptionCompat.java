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
 * A simple set of metadata for a media item suitable for display. This can be
 * created using the Builder or retrieved from existing metadata using
 * {@link MediaMetadataCompat#getDescription()}.
 */
public final class MediaDescriptionCompat implements android.os.Parcelable {
    /**
     * Used as a long extra field to indicate the bluetooth folder type of the media item as
     * specified in the section 6.10.2.2 of the Bluetooth AVRCP 1.5. This is valid only for
     * {@link MediaBrowserCompat.MediaItem} with
     * {@link MediaBrowserCompat.MediaItem#FLAG_BROWSABLE}. The value should be one of the
     * following:
     * <ul>
     * <li>{@link #BT_FOLDER_TYPE_MIXED}</li>
     * <li>{@link #BT_FOLDER_TYPE_TITLES}</li>
     * <li>{@link #BT_FOLDER_TYPE_ALBUMS}</li>
     * <li>{@link #BT_FOLDER_TYPE_ARTISTS}</li>
     * <li>{@link #BT_FOLDER_TYPE_GENRES}</li>
     * <li>{@link #BT_FOLDER_TYPE_PLAYLISTS}</li>
     * <li>{@link #BT_FOLDER_TYPE_YEARS}</li>
     * </ul>
     *
     * @see #getExtras()
     */
    public static final java.lang.String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";

    /**
     * The type of folder that is unknown or contains media elements of mixed types as specified in
     * the section 6.10.2.2 of the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_MIXED = 0;

    /**
     * The type of folder that contains media elements only as specified in the section 6.10.2.2 of
     * the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_TITLES = 1;

    /**
     * The type of folder that contains folders categorized by album as specified in the section
     * 6.10.2.2 of the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_ALBUMS = 2;

    /**
     * The type of folder that contains folders categorized by artist as specified in the section
     * 6.10.2.2 of the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_ARTISTS = 3;

    /**
     * The type of folder that contains folders categorized by genre as specified in the section
     * 6.10.2.2 of the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_GENRES = 4;

    /**
     * The type of folder that contains folders categorized by playlist as specified in the section
     * 6.10.2.2 of the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_PLAYLISTS = 5;

    /**
     * The type of folder that contains folders categorized by year as specified in the section
     * 6.10.2.2 of the Bluetooth AVRCP 1.5.
     */
    public static final long BT_FOLDER_TYPE_YEARS = 6;

    /**
     * Custom key to store a media URI on API 21-22 devices (before it became part of the
     * framework class) when parceling/converting to and from framework objects.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static final java.lang.String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";

    /**
     * Custom key to store whether the original Bundle provided by the developer was null
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static final java.lang.String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";

    /**
     * A unique persistent id for the content or null.
     */
    private final java.lang.String mMediaId;

    /**
     * A primary title suitable for display or null.
     */
    private final java.lang.CharSequence mTitle;

    /**
     * A subtitle suitable for display or null.
     */
    private final java.lang.CharSequence mSubtitle;

    /**
     * A description suitable for display or null.
     */
    private final java.lang.CharSequence mDescription;

    /**
     * A bitmap icon suitable for display or null.
     */
    private final android.graphics.Bitmap mIcon;

    /**
     * A Uri for an icon suitable for display or null.
     */
    private final android.net.Uri mIconUri;

    /**
     * Extras for opaque use by apps/system.
     */
    private final android.os.Bundle mExtras;

    /**
     * A Uri to identify this content.
     */
    private final android.net.Uri mMediaUri;

    /**
     * A cached copy of the equivalent framework object.
     */
    private java.lang.Object mDescriptionObj;

    MediaDescriptionCompat(java.lang.String mediaId, java.lang.CharSequence title, java.lang.CharSequence subtitle, java.lang.CharSequence description, android.graphics.Bitmap icon, android.net.Uri iconUri, android.os.Bundle extras, android.net.Uri mediaUri) {
        mMediaId = mediaId;
        mTitle = title;
        mSubtitle = subtitle;
        mDescription = description;
        mIcon = icon;
        mIconUri = iconUri;
        mExtras = extras;
        mMediaUri = mediaUri;
    }

    MediaDescriptionCompat(android.os.Parcel in) {
        mMediaId = in.readString();
        mTitle = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mSubtitle = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mDescription = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mIcon = in.readParcelable(null);
        mIconUri = in.readParcelable(null);
        mExtras = in.readBundle();
        mMediaUri = in.readParcelable(null);
    }

    /**
     * Returns the media id or null. See
     * {@link MediaMetadataCompat#METADATA_KEY_MEDIA_ID}.
     */
    @android.support.annotation.Nullable
    public java.lang.String getMediaId() {
        return mMediaId;
    }

    /**
     * Returns a title suitable for display or null.
     *
     * @return A title or null.
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Returns a subtitle suitable for display or null.
     *
     * @return A subtitle or null.
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Returns a description suitable for display or null.
     *
     * @return A description or null.
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getDescription() {
        return mDescription;
    }

    /**
     * Returns a bitmap icon suitable for display or null.
     *
     * @return An icon or null.
     */
    @android.support.annotation.Nullable
    public android.graphics.Bitmap getIconBitmap() {
        return mIcon;
    }

    /**
     * Returns a Uri for an icon suitable for display or null.
     *
     * @return An icon uri or null.
     */
    @android.support.annotation.Nullable
    public android.net.Uri getIconUri() {
        return mIconUri;
    }

    /**
     * Returns any extras that were added to the description.
     *
     * @return A bundle of extras or null.
     */
    @android.support.annotation.Nullable
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Returns a Uri representing this content or null.
     *
     * @return A media Uri or null.
     */
    @android.support.annotation.Nullable
    public android.net.Uri getMediaUri() {
        return mMediaUri;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (android.os.Build.VERSION.SDK_INT < 21) {
            dest.writeString(mMediaId);
            android.text.TextUtils.writeToParcel(mTitle, dest, flags);
            android.text.TextUtils.writeToParcel(mSubtitle, dest, flags);
            android.text.TextUtils.writeToParcel(mDescription, dest, flags);
            dest.writeParcelable(mIcon, flags);
            dest.writeParcelable(mIconUri, flags);
            dest.writeBundle(mExtras);
            dest.writeParcelable(mMediaUri, flags);
        } else {
            android.support.v4.media.MediaDescriptionCompatApi21.writeToParcel(getMediaDescription(), dest, flags);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((mTitle + ", ") + mSubtitle) + ", ") + mDescription;
    }

    /**
     * Gets the underlying framework {@link android.media.MediaDescription}
     * object.
     * <p>
     * This method is only supported on
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP} and later.
     * </p>
     *
     * @return An equivalent {@link android.media.MediaDescription} object, or
    null if none.
     */
    public java.lang.Object getMediaDescription() {
        if ((mDescriptionObj != null) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return mDescriptionObj;
        }
        java.lang.Object bob = android.support.v4.media.MediaDescriptionCompatApi21.Builder.newInstance();
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setMediaId(bob, mMediaId);
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setTitle(bob, mTitle);
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setSubtitle(bob, mSubtitle);
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setDescription(bob, mDescription);
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setIconBitmap(bob, mIcon);
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setIconUri(bob, mIconUri);
        // Media URI was not added until API 23, so add it to the Bundle of extras to
        // ensure the data is not lost - this ensures that
        // fromMediaDescription(getMediaDescription(mediaDescriptionCompat)) returns
        // an equivalent MediaDescriptionCompat on all API levels
        android.os.Bundle extras = mExtras;
        if ((android.os.Build.VERSION.SDK_INT < 23) && (mMediaUri != null)) {
            if (extras == null) {
                extras = new android.os.Bundle();
                extras.putBoolean(android.support.v4.media.MediaDescriptionCompat.DESCRIPTION_KEY_NULL_BUNDLE_FLAG, true);
            }
            extras.putParcelable(android.support.v4.media.MediaDescriptionCompat.DESCRIPTION_KEY_MEDIA_URI, mMediaUri);
        }
        android.support.v4.media.MediaDescriptionCompatApi21.Builder.setExtras(bob, extras);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            android.support.v4.media.MediaDescriptionCompatApi23.Builder.setMediaUri(bob, mMediaUri);
        }
        mDescriptionObj = android.support.v4.media.MediaDescriptionCompatApi21.Builder.build(bob);
        return mDescriptionObj;
    }

    /**
     * Creates an instance from a framework
     * {@link android.media.MediaDescription} object.
     * <p>
     * This method is only supported on API 21+.
     * </p>
     *
     * @param descriptionObj
     * 		A {@link android.media.MediaDescription} object, or
     * 		null if none.
     * @return An equivalent {@link MediaMetadataCompat} object, or null if
    none.
     */
    public static android.support.v4.media.MediaDescriptionCompat fromMediaDescription(java.lang.Object descriptionObj) {
        if ((descriptionObj == null) || (android.os.Build.VERSION.SDK_INT < 21)) {
            return null;
        }
        android.support.v4.media.MediaDescriptionCompat.Builder bob = new android.support.v4.media.MediaDescriptionCompat.Builder();
        bob.setMediaId(android.support.v4.media.MediaDescriptionCompatApi21.getMediaId(descriptionObj));
        bob.setTitle(android.support.v4.media.MediaDescriptionCompatApi21.getTitle(descriptionObj));
        bob.setSubtitle(android.support.v4.media.MediaDescriptionCompatApi21.getSubtitle(descriptionObj));
        bob.setDescription(android.support.v4.media.MediaDescriptionCompatApi21.getDescription(descriptionObj));
        bob.setIconBitmap(android.support.v4.media.MediaDescriptionCompatApi21.getIconBitmap(descriptionObj));
        bob.setIconUri(android.support.v4.media.MediaDescriptionCompatApi21.getIconUri(descriptionObj));
        android.os.Bundle extras = android.support.v4.media.MediaDescriptionCompatApi21.getExtras(descriptionObj);
        android.net.Uri mediaUri = (extras == null) ? null : ((android.net.Uri) (extras.getParcelable(android.support.v4.media.MediaDescriptionCompat.DESCRIPTION_KEY_MEDIA_URI)));
        if (mediaUri != null) {
            if (extras.containsKey(android.support.v4.media.MediaDescriptionCompat.DESCRIPTION_KEY_NULL_BUNDLE_FLAG) && (extras.size() == 2)) {
                // The extras were only created for the media URI, so we set it back to null to
                // ensure mediaDescriptionCompat.getExtras() equals
                // fromMediaDescription(getMediaDescription(mediaDescriptionCompat)).getExtras()
                extras = null;
            } else {
                // Remove media URI keys to ensure mediaDescriptionCompat.getExtras().keySet()
                // equals fromMediaDescription(getMediaDescription(mediaDescriptionCompat))
                // .getExtras().keySet()
                extras.remove(android.support.v4.media.MediaDescriptionCompat.DESCRIPTION_KEY_MEDIA_URI);
                extras.remove(android.support.v4.media.MediaDescriptionCompat.DESCRIPTION_KEY_NULL_BUNDLE_FLAG);
            }
        }
        bob.setExtras(extras);
        if (mediaUri != null) {
            bob.setMediaUri(mediaUri);
        } else
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                bob.setMediaUri(android.support.v4.media.MediaDescriptionCompatApi23.getMediaUri(descriptionObj));
            }

        android.support.v4.media.MediaDescriptionCompat descriptionCompat = bob.build();
        descriptionCompat.mDescriptionObj = descriptionObj;
        return descriptionCompat;
    }

    public static final android.os.Parcelable.Creator<android.support.v4.media.MediaDescriptionCompat> CREATOR = new android.os.Parcelable.Creator<android.support.v4.media.MediaDescriptionCompat>() {
        @java.lang.Override
        public android.support.v4.media.MediaDescriptionCompat createFromParcel(android.os.Parcel in) {
            if (android.os.Build.VERSION.SDK_INT < 21) {
                return new android.support.v4.media.MediaDescriptionCompat(in);
            } else {
                return android.support.v4.media.MediaDescriptionCompat.fromMediaDescription(android.support.v4.media.MediaDescriptionCompatApi21.fromParcel(in));
            }
        }

        @java.lang.Override
        public android.support.v4.media.MediaDescriptionCompat[] newArray(int size) {
            return new android.support.v4.media.MediaDescriptionCompat[size];
        }
    };

    /**
     * Builder for {@link MediaDescriptionCompat} objects.
     */
    public static final class Builder {
        private java.lang.String mMediaId;

        private java.lang.CharSequence mTitle;

        private java.lang.CharSequence mSubtitle;

        private java.lang.CharSequence mDescription;

        private android.graphics.Bitmap mIcon;

        private android.net.Uri mIconUri;

        private android.os.Bundle mExtras;

        private android.net.Uri mMediaUri;

        /**
         * Creates an initially empty builder.
         */
        public Builder() {
        }

        /**
         * Sets the media id.
         *
         * @param mediaId
         * 		The unique id for the item or null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setMediaId(@android.support.annotation.Nullable
        java.lang.String mediaId) {
            mMediaId = mediaId;
            return this;
        }

        /**
         * Sets the title.
         *
         * @param title
         * 		A title suitable for display to the user or null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setTitle(@android.support.annotation.Nullable
        java.lang.CharSequence title) {
            mTitle = title;
            return this;
        }

        /**
         * Sets the subtitle.
         *
         * @param subtitle
         * 		A subtitle suitable for display to the user or null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setSubtitle(@android.support.annotation.Nullable
        java.lang.CharSequence subtitle) {
            mSubtitle = subtitle;
            return this;
        }

        /**
         * Sets the description.
         *
         * @param description
         * 		A description suitable for display to the user or
         * 		null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setDescription(@android.support.annotation.Nullable
        java.lang.CharSequence description) {
            mDescription = description;
            return this;
        }

        /**
         * Sets the icon.
         *
         * @param icon
         * 		A {@link Bitmap} icon suitable for display to the user or
         * 		null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setIconBitmap(@android.support.annotation.Nullable
        android.graphics.Bitmap icon) {
            mIcon = icon;
            return this;
        }

        /**
         * Sets the icon uri.
         *
         * @param iconUri
         * 		A {@link Uri} for an icon suitable for display to the
         * 		user or null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setIconUri(@android.support.annotation.Nullable
        android.net.Uri iconUri) {
            mIconUri = iconUri;
            return this;
        }

        /**
         * Sets a bundle of extras.
         *
         * @param extras
         * 		The extras to include with this description or null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setExtras(@android.support.annotation.Nullable
        android.os.Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Sets the media uri.
         *
         * @param mediaUri
         * 		The content's {@link Uri} for the item or null.
         * @return this
         */
        public android.support.v4.media.MediaDescriptionCompat.Builder setMediaUri(@android.support.annotation.Nullable
        android.net.Uri mediaUri) {
            mMediaUri = mediaUri;
            return this;
        }

        /**
         * Creates a {@link MediaDescriptionCompat} instance with the specified
         * fields.
         *
         * @return A MediaDescriptionCompat instance.
         */
        public android.support.v4.media.MediaDescriptionCompat build() {
            return new android.support.v4.media.MediaDescriptionCompat(mMediaId, mTitle, mSubtitle, mDescription, mIcon, mIconUri, mExtras, mMediaUri);
        }
    }
}

