package android.media;


/**
 * A simple set of metadata for a media item suitable for display. This can be
 * created using the Builder or retrieved from existing metadata using
 * {@link MediaMetadata#getDescription()}.
 */
public class MediaDescription implements android.os.Parcelable {
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

    private MediaDescription(java.lang.String mediaId, java.lang.CharSequence title, java.lang.CharSequence subtitle, java.lang.CharSequence description, android.graphics.Bitmap icon, android.net.Uri iconUri, android.os.Bundle extras, android.net.Uri mediaUri) {
        mMediaId = mediaId;
        mTitle = title;
        mSubtitle = subtitle;
        mDescription = description;
        mIcon = icon;
        mIconUri = iconUri;
        mExtras = extras;
        mMediaUri = mediaUri;
    }

    private MediaDescription(android.os.Parcel in) {
        mMediaId = in.readString();
        mTitle = in.readCharSequence();
        mSubtitle = in.readCharSequence();
        mDescription = in.readCharSequence();
        mIcon = in.readParcelable(null);
        mIconUri = in.readParcelable(null);
        mExtras = in.readBundle();
        mMediaUri = in.readParcelable(null);
    }

    /**
     * Returns the media id or null. See
     * {@link MediaMetadata#METADATA_KEY_MEDIA_ID}.
     */
    @android.annotation.Nullable
    public java.lang.String getMediaId() {
        return mMediaId;
    }

    /**
     * Returns a title suitable for display or null.
     *
     * @return A title or null.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Returns a subtitle suitable for display or null.
     *
     * @return A subtitle or null.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Returns a description suitable for display or null.
     *
     * @return A description or null.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getDescription() {
        return mDescription;
    }

    /**
     * Returns a bitmap icon suitable for display or null.
     *
     * @return An icon or null.
     */
    @android.annotation.Nullable
    public android.graphics.Bitmap getIconBitmap() {
        return mIcon;
    }

    /**
     * Returns a Uri for an icon suitable for display or null.
     *
     * @return An icon uri or null.
     */
    @android.annotation.Nullable
    public android.net.Uri getIconUri() {
        return mIconUri;
    }

    /**
     * Returns any extras that were added to the description.
     *
     * @return A bundle of extras or null.
     */
    @android.annotation.Nullable
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Returns a Uri representing this content or null.
     *
     * @return A media Uri or null.
     */
    @android.annotation.Nullable
    public android.net.Uri getMediaUri() {
        return mMediaUri;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mMediaId);
        dest.writeCharSequence(mTitle);
        dest.writeCharSequence(mSubtitle);
        dest.writeCharSequence(mDescription);
        dest.writeParcelable(mIcon, flags);
        dest.writeParcelable(mIconUri, flags);
        dest.writeBundle(mExtras);
        dest.writeParcelable(mMediaUri, flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((mTitle + ", ") + mSubtitle) + ", ") + mDescription;
    }

    public static final android.os.Parcelable.Creator<android.media.MediaDescription> CREATOR = new android.os.Parcelable.Creator<android.media.MediaDescription>() {
        @java.lang.Override
        public android.media.MediaDescription createFromParcel(android.os.Parcel in) {
            return new android.media.MediaDescription(in);
        }

        @java.lang.Override
        public android.media.MediaDescription[] newArray(int size) {
            return new android.media.MediaDescription[size];
        }
    };

    /**
     * Builder for {@link MediaDescription} objects.
     */
    public static class Builder {
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
        public android.media.MediaDescription.Builder setMediaId(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setTitle(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setSubtitle(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setDescription(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setIconBitmap(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setIconUri(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setExtras(@android.annotation.Nullable
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
        public android.media.MediaDescription.Builder setMediaUri(@android.annotation.Nullable
        android.net.Uri mediaUri) {
            mMediaUri = mediaUri;
            return this;
        }

        public android.media.MediaDescription build() {
            return new android.media.MediaDescription(mMediaId, mTitle, mSubtitle, mDescription, mIcon, mIconUri, mExtras, mMediaUri);
        }
    }
}

