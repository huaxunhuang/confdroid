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
 * limitations under the License.
 */
package android.support.app.recommendation;


/**
 * The ContentRecommendation object encapsulates all application provided data for a single content
 * recommendation item.
 */
public final class ContentRecommendation {
    @android.support.annotation.StringDef({ android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_VIDEO, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_MOVIE, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_TRAILER, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_SERIAL, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_MUSIC, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_RADIO, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_PODCAST, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_NEWS, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_SPORTS, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_APP, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_GAME, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_BOOK, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_COMIC, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_MAGAZINE, android.support.app.recommendation.ContentRecommendation.CONTENT_TYPE_WEBSITE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ContentType {}

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a video clip.
     */
    public static final java.lang.String CONTENT_TYPE_VIDEO = "android.contentType.video";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a movie.
     */
    public static final java.lang.String CONTENT_TYPE_MOVIE = "android.contentType.movie";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a trailer.
     */
    public static final java.lang.String CONTENT_TYPE_TRAILER = "android.contentType.trailer";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is serial. It can refer to an entire show, a single season or
     * series, or a single episode.
     */
    public static final java.lang.String CONTENT_TYPE_SERIAL = "android.contentType.serial";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a song or album.
     */
    public static final java.lang.String CONTENT_TYPE_MUSIC = "android.contentType.music";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a radio station.
     */
    public static final java.lang.String CONTENT_TYPE_RADIO = "android.contentType.radio";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a podcast.
     */
    public static final java.lang.String CONTENT_TYPE_PODCAST = "android.contentType.podcast";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a news item.
     */
    public static final java.lang.String CONTENT_TYPE_NEWS = "android.contentType.news";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is sports.
     */
    public static final java.lang.String CONTENT_TYPE_SPORTS = "android.contentType.sports";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is an application.
     */
    public static final java.lang.String CONTENT_TYPE_APP = "android.contentType.app";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a game.
     */
    public static final java.lang.String CONTENT_TYPE_GAME = "android.contentType.game";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a book.
     */
    public static final java.lang.String CONTENT_TYPE_BOOK = "android.contentType.book";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a comic book.
     */
    public static final java.lang.String CONTENT_TYPE_COMIC = "android.contentType.comic";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a magazine.
     */
    public static final java.lang.String CONTENT_TYPE_MAGAZINE = "android.contentType.magazine";

    /**
     * Value to be used with {@link Builder#setContentTypes} to indicate that the content referred
     * by the notification item is a website.
     */
    public static final java.lang.String CONTENT_TYPE_WEBSITE = "android.contentType.website";

    @android.support.annotation.StringDef({ android.support.app.recommendation.ContentRecommendation.CONTENT_PRICING_FREE, android.support.app.recommendation.ContentRecommendation.CONTENT_PRICING_RENTAL, android.support.app.recommendation.ContentRecommendation.CONTENT_PRICING_PURCHASE, android.support.app.recommendation.ContentRecommendation.CONTENT_PRICING_PREORDER, android.support.app.recommendation.ContentRecommendation.CONTENT_PRICING_SUBSCRIPTION })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ContentPricing {}

    /**
     * Value to be used with {@link Builder#setPricingInformation} to indicate that the content
     * referred by the notification item is free to consume.
     */
    public static final java.lang.String CONTENT_PRICING_FREE = "android.contentPrice.free";

    /**
     * Value to be used with {@link Builder#setPricingInformation} to indicate that the content
     * referred by the notification item is available as a rental, and the price value provided is
     * the rental price for the item.
     */
    public static final java.lang.String CONTENT_PRICING_RENTAL = "android.contentPrice.rental";

    /**
     * Value to be used with {@link Builder#setPricingInformation} to indicate that the content
     * referred by the notification item is available for purchase, and the price value provided is
     * the purchase price for the item.
     */
    public static final java.lang.String CONTENT_PRICING_PURCHASE = "android.contentPrice.purchase";

    /**
     * Value to be used with {@link Builder#setPricingInformation} to indicate that the content
     * referred by the notification item is available currently as a pre-order, and the price value
     * provided is the purchase price for the item.
     */
    public static final java.lang.String CONTENT_PRICING_PREORDER = "android.contentPrice.preorder";

    /**
     * Value to be used with {@link Builder#setPricingInformation} to indicate that the content
     * referred by the notification item is available as part of a subscription based service, and
     * the price value provided is the subscription price for the service.
     */
    public static final java.lang.String CONTENT_PRICING_SUBSCRIPTION = "android.contentPrice.subscription";

    @android.support.annotation.IntDef({ android.support.app.recommendation.ContentRecommendation.CONTENT_STATUS_READY, android.support.app.recommendation.ContentRecommendation.CONTENT_STATUS_PENDING, android.support.app.recommendation.ContentRecommendation.CONTENT_STATUS_AVAILABLE, android.support.app.recommendation.ContentRecommendation.CONTENT_STATUS_UNAVAILABLE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ContentStatus {}

    /**
     * Value to be used with {@link Builder#setStatus} to indicate that the content referred by the
     * notification is available and ready to be consumed immediately.
     */
    public static final int CONTENT_STATUS_READY = 0;

    /**
     * Value to be used with {@link Builder#setStatus} to indicate that the content referred by the
     * notification is pending, waiting on either a download or purchase operation to complete
     * before it can be consumed.
     */
    public static final int CONTENT_STATUS_PENDING = 1;

    /**
     * Value to be used with {@link Builder#setStatus} to indicate that the content referred by the
     * notification is available, but needs to be first purchased, rented, subscribed or downloaded
     * before it can be consumed.
     */
    public static final int CONTENT_STATUS_AVAILABLE = 2;

    /**
     * Value to be used with {@link Builder#setStatus} to indicate that the content referred by the
     * notification is not available. This could be content not available in a certain region or
     * incompatible with the device in use.
     */
    public static final int CONTENT_STATUS_UNAVAILABLE = 3;

    @android.support.annotation.StringDef({ android.support.app.recommendation.ContentRecommendation.CONTENT_MATURITY_ALL, android.support.app.recommendation.ContentRecommendation.CONTENT_MATURITY_LOW, android.support.app.recommendation.ContentRecommendation.CONTENT_MATURITY_MEDIUM, android.support.app.recommendation.ContentRecommendation.CONTENT_MATURITY_HIGH })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ContentMaturity {}

    /**
     * Value to be used with {@link Builder#setMaturityRating} to indicate that the content referred
     * by the notification is suitable for all audiences.
     */
    public static final java.lang.String CONTENT_MATURITY_ALL = "android.contentMaturity.all";

    /**
     * Value to be used with {@link Builder#setMaturityRating} to indicate that the content referred
     * by the notification is suitable for audiences of low maturity and above.
     */
    public static final java.lang.String CONTENT_MATURITY_LOW = "android.contentMaturity.low";

    /**
     * Value to be used with {@link Builder#setMaturityRating} to indicate that the content referred
     * by the notification is suitable for audiences of medium maturity and above.
     */
    public static final java.lang.String CONTENT_MATURITY_MEDIUM = "android.contentMaturity.medium";

    /**
     * Value to be used with {@link Builder#setMaturityRating} to indicate that the content referred
     * by the notification is suitable for audiences of high maturity and above.
     */
    public static final java.lang.String CONTENT_MATURITY_HIGH = "android.contentMaturity.high";

    @android.support.annotation.IntDef({ android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_ACTIVITY, android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_BROADCAST, android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_SERVICE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface IntentType {}

    /**
     * Value to be used with {@link Builder#setContentIntentData} and
     * {@link Builder#setDismissIntentData} to indicate that a {@link PendingIntent} for an Activity
     * should be created when posting the recommendation to the HomeScreen.
     */
    public static final int INTENT_TYPE_ACTIVITY = 1;

    /**
     * Value to be used with {@link Builder#setContentIntentData} and
     * {@link Builder#setDismissIntentData} to indicate that a {@link PendingIntent} for a Broadcast
     * should be created when posting the recommendation to the HomeScreen.
     */
    public static final int INTENT_TYPE_BROADCAST = 2;

    /**
     * Value to be used with {@link Builder#setContentIntentData} and
     * {@link Builder#setDismissIntentData} to indicate that a {@link PendingIntent} for a Service
     * should be created when posting the recommendation to the HomeScreen.
     */
    public static final int INTENT_TYPE_SERVICE = 3;

    /**
     * Object used to encapsulate the data to be used to build the {@link PendingIntent} object
     * associated with a given content recommendation, at the time this recommendation gets posted
     * to the home Screen.
     * <p>
     * The members of this object correspond to the fields passed into the {@link PendingIntent}
     * factory methods, when creating a new PendingIntent.
     */
    public static class IntentData {
        int mType;

        android.content.Intent mIntent;

        int mRequestCode;

        android.os.Bundle mOptions;
    }

    private final java.lang.String mIdTag;

    private final java.lang.String mTitle;

    private final java.lang.String mText;

    private final java.lang.String mSourceName;

    private final android.graphics.Bitmap mContentImage;

    private final int mBadgeIconId;

    private final java.lang.String mBackgroundImageUri;

    private final int mColor;

    private final android.support.app.recommendation.ContentRecommendation.IntentData mContentIntentData;

    private final android.support.app.recommendation.ContentRecommendation.IntentData mDismissIntentData;

    private final java.lang.String[] mContentTypes;

    private final java.lang.String[] mContentGenres;

    private final java.lang.String mPriceType;

    private final java.lang.String mPriceValue;

    private final java.lang.String mMaturityRating;

    private final long mRunningTime;

    // Mutable fields
    private java.lang.String mGroup;

    private java.lang.String mSortKey;

    private int mProgressAmount;

    private int mProgressMax;

    private boolean mAutoDismiss;

    private int mStatus;

    private ContentRecommendation(android.support.app.recommendation.ContentRecommendation.Builder builder) {
        mIdTag = builder.mBuilderIdTag;
        mTitle = builder.mBuilderTitle;
        mText = builder.mBuilderText;
        mSourceName = builder.mBuilderSourceName;
        mContentImage = builder.mBuilderContentImage;
        mBadgeIconId = builder.mBuilderBadgeIconId;
        mBackgroundImageUri = builder.mBuilderBackgroundImageUri;
        mColor = builder.mBuilderColor;
        mContentIntentData = builder.mBuilderContentIntentData;
        mDismissIntentData = builder.mBuilderDismissIntentData;
        mContentTypes = builder.mBuilderContentTypes;
        mContentGenres = builder.mBuilderContentGenres;
        mPriceType = builder.mBuilderPriceType;
        mPriceValue = builder.mBuilderPriceValue;
        mMaturityRating = builder.mBuilderMaturityRating;
        mRunningTime = builder.mBuilderRunningTime;
        mGroup = builder.mBuilderGroup;
        mSortKey = builder.mBuilderSortKey;
        mProgressAmount = builder.mBuilderProgressAmount;
        mProgressMax = builder.mBuilderProgressMax;
        mAutoDismiss = builder.mBuilderAutoDismiss;
        mStatus = builder.mBuilderStatus;
    }

    /**
     * Returns the String Id tag which uniquely identifies this recommendation.
     *
     * @return The String Id tag for this recommendation.
     */
    public java.lang.String getIdTag() {
        return mIdTag;
    }

    /**
     * Returns the content title for this recommendation.
     *
     * @return A String containing the recommendation content title.
     */
    public java.lang.String getTitle() {
        return mTitle;
    }

    /**
     * Returns the description text for this recommendation.
     *
     * @return A String containing the recommendation description text.
     */
    public java.lang.String getText() {
        return mText;
    }

    /**
     * Returns the source application name for this recommendation.
     *
     * @return A String containing the recommendation source name.
     */
    public java.lang.String getSourceName() {
        return mSourceName;
    }

    /**
     * Returns the Bitmap containing the recommendation image.
     *
     * @return A Bitmap containing the recommendation image.
     */
    public android.graphics.Bitmap getContentImage() {
        return mContentImage;
    }

    /**
     * Returns the resource id for the recommendation badging icon.
     * <p>
     * The resource id represents the icon resource in the source application package.
     *
     * @return An integer id for the badge icon resource.
     */
    public int getBadgeImageResourceId() {
        return mBadgeIconId;
    }

    /**
     * Returns a Content URI that can be used to retrieve the background image for this
     * recommendation.
     *
     * @return A Content URI pointing to the recommendation background image.
     */
    public java.lang.String getBackgroundImageUri() {
        return mBackgroundImageUri;
    }

    /**
     * Returns the accent color value to be used in the UI when displaying this content
     * recommendation to the user.
     *
     * @return An integer value representing the accent color for this recommendation.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the String group ID tag for this recommendation.
     * <p>
     * Recommendations in the same group are ranked by the Home Screen together, and the sort order
     * within a group is respected. This can be useful if the application has different sources for
     * recommendations, like "trending", "subscriptions", and "new music" categories for YouTube,
     * where the user can be more interested in recommendations from one group than another.
     *
     * @param groupTag
     * 		A String containing the group ID tag for this recommendation.
     */
    public void setGroup(java.lang.String groupTag) {
        mGroup = groupTag;
    }

    /**
     * Returns the String group ID tag for this recommendation.
     *
     * @return A String containing the group ID tag for this recommendation.
     */
    public java.lang.String getGroup() {
        return mGroup;
    }

    /**
     * Sets the String sort key for this recommendation.
     * <p>
     * The sort key must be a String representation of a float number between 0.0 and 1.0, and is
     * used to indicate the relative importance (and sort order) of a single recommendation within
     * its specified group. The recommendations will be ordered in decreasing order of importance
     * within a given group.
     *
     * @param sortKey
     * 		A String containing the sort key for this recommendation.
     */
    public void setSortKey(java.lang.String sortKey) {
        mSortKey = sortKey;
    }

    /**
     * Returns the String sort key for this recommendation.
     *
     * @return A String containing the sort key for this recommendation.
     */
    public java.lang.String getSortKey() {
        return mSortKey;
    }

    /**
     * Sets the progress information for the content pointed to by this recommendation.
     *
     * @param max
     * 		The maximum value for the progress of this content.
     * @param progress
     * 		The progress amount for this content. Must be in the range (0 - max).
     */
    public void setProgress(int max, int progress) {
        if ((max < 0) || (progress < 0)) {
            throw new java.lang.IllegalArgumentException();
        }
        mProgressMax = max;
        mProgressAmount = progress;
    }

    /**
     * Indicates if this recommendation contains valid progress information.
     *
     * @return true if the recommendation contains valid progress data, false otherwise.
     */
    public boolean hasProgressInfo() {
        return mProgressMax != 0;
    }

    /**
     * Returns the maximum value for the progress data of this recommendation.
     *
     * @return An integer representing the maximum progress value.
     */
    public int getProgressMax() {
        return mProgressMax;
    }

    /**
     * Returns the progress amount for this recommendation.
     *
     * @return An integer representing the recommendation progress amount.
     */
    public int getProgressValue() {
        return mProgressAmount;
    }

    /**
     * Sets the flag indicating if this recommendation should be dismissed automatically.
     * <p>
     * Auto-dismiss notifications are automatically removed from the Home Screen when the user
     * clicks on them.
     *
     * @param autoDismiss
     * 		A boolean indicating if the recommendation should be auto dismissed or
     * 		not.
     */
    public void setAutoDismiss(boolean autoDismiss) {
        mAutoDismiss = autoDismiss;
    }

    /**
     * Indicates whether this recommendation should be dismissed automatically.
     * <p>
     * Auto-dismiss notifications are automatically removed from the Home Screen when the user
     * clicks on them.
     *
     * @return true if the recommendation is marked for auto dismissal, or false otherwise.
     */
    public boolean isAutoDismiss() {
        return mAutoDismiss;
    }

    /**
     * Returns the data for the Intent that will be issued when the user clicks on the
     * recommendation.
     *
     * @return An IntentData object, containing the data for the Intent that gets issued when the
    recommendation is clicked on.
     */
    public android.support.app.recommendation.ContentRecommendation.IntentData getContentIntent() {
        return mContentIntentData;
    }

    /**
     * Returns the data for the Intent that will be issued when the recommendation gets dismissed
     * from the Home Screen, due to an user action.
     *
     * @return An IntentData object, containing the data for the Intent that gets issued when the
    recommendation is dismissed from the Home Screen.
     */
    public android.support.app.recommendation.ContentRecommendation.IntentData getDismissIntent() {
        return mDismissIntentData;
    }

    /**
     * Returns an array containing the content types tags that describe the content. The first tag
     * entry is considered the primary type for the content, and is used for content ranking
     * purposes.
     *
     * @return An array of predefined type tags (see the <code>CONTENT_TYPE_*</code> constants) that
    describe the recommended content.
     */
    public java.lang.String[] getContentTypes() {
        if (mContentTypes != null) {
            return java.util.Arrays.copyOf(mContentTypes, mContentTypes.length);
        }
        return mContentTypes;
    }

    /**
     * Returns the primary content type tag for the recommendation, or null if no content types have
     * been specified.
     *
     * @return A predefined type tag (see the <code>CONTENT_TYPE_*</code> constants) indicating the
    primary content type for the recommendation.
     */
    public java.lang.String getPrimaryContentType() {
        if ((mContentTypes != null) && (mContentTypes.length > 0)) {
            return mContentTypes[0];
        }
        return null;
    }

    /**
     * Returns an array containing the genres that describe the content. Genres are open ended
     * String tags.
     *
     * @return An array of genre tags that describe the recommended content.
     */
    public java.lang.String[] getGenres() {
        if (mContentGenres != null) {
            return java.util.Arrays.copyOf(mContentGenres, mContentGenres.length);
        }
        return mContentGenres;
    }

    /**
     * Gets the pricing type for the content.
     *
     * @return A predefined tag indicating the pricing type for the content (see the <code>
    CONTENT_PRICING_*</code> constants).
     */
    public java.lang.String getPricingType() {
        return mPriceType;
    }

    /**
     * Gets the price value (when applicable) for the content. The value will be provided as a
     * String containing the price in the appropriate currency for the current locale.
     *
     * @return A string containing a representation of the content price in the current locale and
    currency.
     */
    public java.lang.String getPricingValue() {
        return mPriceValue;
    }

    /**
     * Sets the availability status value for the content. This status indicates whether the content
     * is ready to be consumed on the device, or if the user must first purchase, rent, subscribe
     * to, or download the content.
     *
     * @param status
     * 		The status value for the content. (see the <code>CONTENT_STATUS_*</code> for
     * 		the valid status values).
     */
    public void setStatus(@android.support.app.recommendation.ContentRecommendation.ContentStatus
    int status) {
        mStatus = status;
    }

    /**
     * Returns availability status value for the content. This status indicates whether the content
     * is ready to be consumed on the device, or if the user must first purchase, rent, subscribe
     * to, or download the content.
     *
     * @return The status value for the content, or -1 is a valid status has not been specified (see
    the <code>CONTENT_STATUS_*</code> constants for the valid status values).
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * Returns the maturity level rating for the content.
     *
     * @return returns a predefined tag indicating the maturity level rating for the content (see
    the <code>CONTENT_MATURITY_*</code> constants).
     */
    public java.lang.String getMaturityRating() {
        return mMaturityRating;
    }

    /**
     * Returns the running time for the content.
     *
     * @return The run length, in seconds, of the content associated with the notification.
     */
    public long getRunningTime() {
        return mRunningTime;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (other instanceof android.support.app.recommendation.ContentRecommendation) {
            return android.text.TextUtils.equals(mIdTag, ((android.support.app.recommendation.ContentRecommendation) (other)).getIdTag());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        if (mIdTag != null) {
            return mIdTag.hashCode();
        }
        return java.lang.Integer.MAX_VALUE;
    }

    /**
     * Builder class for {@link ContentRecommendation} objects. Provides a convenient way to set the
     * various fields of a {@link ContentRecommendation}.
     * <p>
     * Example:
     *
     * <pre class="prettyprint">
     * ContentRecommendation rec = new ContentRecommendation.Builder()
     *         .setIdInfo(id, &quot;MyTagId&quot;)
     *         .setTitle(&quot;My Content Recommendation&quot;)
     *         .setText(&quot;An example of content recommendation&quot;)
     *         .setContentImage(myBitmap)
     *         .setBadgeIcon(R.drawable.app_icon)
     *         .setGroup(&quot;Trending&quot;)
     *         .build();
     * </pre>
     */
    public static final class Builder {
        private java.lang.String mBuilderIdTag;

        private java.lang.String mBuilderTitle;

        private java.lang.String mBuilderText;

        private java.lang.String mBuilderSourceName;

        private android.graphics.Bitmap mBuilderContentImage;

        private int mBuilderBadgeIconId;

        private java.lang.String mBuilderBackgroundImageUri;

        private int mBuilderColor;

        private java.lang.String mBuilderGroup;

        private java.lang.String mBuilderSortKey;

        private int mBuilderProgressAmount;

        private int mBuilderProgressMax;

        private boolean mBuilderAutoDismiss;

        private android.support.app.recommendation.ContentRecommendation.IntentData mBuilderContentIntentData;

        private android.support.app.recommendation.ContentRecommendation.IntentData mBuilderDismissIntentData;

        private java.lang.String[] mBuilderContentTypes;

        private java.lang.String[] mBuilderContentGenres;

        private java.lang.String mBuilderPriceType;

        private java.lang.String mBuilderPriceValue;

        private int mBuilderStatus;

        private java.lang.String mBuilderMaturityRating;

        private long mBuilderRunningTime;

        /**
         * Constructs a new Builder.
         */
        public Builder() {
        }

        /**
         * Sets the Id tag that uniquely identifies this recommendation object.
         *
         * @param idTag
         * 		A String tag identifier for this recommendation.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setIdTag(java.lang.String idTag) {
            mBuilderIdTag = android.support.app.recommendation.ContentRecommendation.checkNotNull(idTag);
            return this;
        }

        /**
         * Sets the content title for the recommendation.
         *
         * @param title
         * 		A String containing the recommendation content title.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setTitle(java.lang.String title) {
            mBuilderTitle = android.support.app.recommendation.ContentRecommendation.checkNotNull(title);
            return this;
        }

        /**
         * Sets the description text for the recommendation.
         *
         * @param description
         * 		A String containing the recommendation description text.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setText(@android.support.annotation.Nullable
        java.lang.String description) {
            mBuilderText = description;
            return this;
        }

        /**
         * Sets the source application name for the recommendation.
         * <P>
         * If the source name is never set, or set to null, the application name retrieved from its
         * package will be used by default.
         *
         * @param source
         * 		A String containing the recommendation source name.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setSourceName(@android.support.annotation.Nullable
        java.lang.String source) {
            mBuilderSourceName = source;
            return this;
        }

        /**
         * Sets the recommendation image.
         *
         * @param image
         * 		A Bitmap containing the recommendation image.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setContentImage(android.graphics.Bitmap image) {
            mBuilderContentImage = android.support.app.recommendation.ContentRecommendation.checkNotNull(image);
            return this;
        }

        /**
         * Sets the resource ID for the recommendation badging icon.
         * <p>
         * The resource id represents the icon resource in the source application package. If not
         * set, or an invalid resource ID is specified, the application icon retrieved from its
         * package will be used by default.
         *
         * @param iconResourceId
         * 		An integer id for the badge icon resource.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setBadgeIcon(@android.support.annotation.DrawableRes
        int iconResourceId) {
            mBuilderBadgeIconId = iconResourceId;
            return this;
        }

        /**
         * Sets the Content URI that will be used to retrieve the background image for the
         * recommendation.
         *
         * @param imageUri
         * 		A Content URI pointing to the recommendation background image.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setBackgroundImageUri(@android.support.annotation.Nullable
        java.lang.String imageUri) {
            mBuilderBackgroundImageUri = imageUri;
            return this;
        }

        /**
         * Sets the accent color value to be used in the UI when displaying this content
         * recommendation to the user.
         *
         * @param color
         * 		An integer value representing the accent color for this recommendation.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setColor(@android.support.annotation.ColorInt
        int color) {
            mBuilderColor = color;
            return this;
        }

        /**
         * Sets the String group ID tag for the recommendation.
         * <p>
         * Recommendations in the same group are ranked by the Home Screen together, and the sort
         * order within a group is respected. This can be useful if the application has different
         * sources for recommendations, like "trending", "subscriptions", and "new music" categories
         * for YouTube, where the user can be more interested in recommendations from one group than
         * another.
         *
         * @param groupTag
         * 		A String containing the group ID tag for this recommendation.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setGroup(@android.support.annotation.Nullable
        java.lang.String groupTag) {
            mBuilderGroup = groupTag;
            return this;
        }

        /**
         * Sets the String sort key for the recommendation.
         * <p>
         * The sort key must be a String representation of a float number between 0.0 and 1.0, and
         * is used to indicate the relative importance (and sort order) of a single recommendation
         * within its specified group. The recommendations will be ordered in decreasing order of
         * importance within a given group.
         *
         * @param sortKey
         * 		A String containing the sort key for this recommendation.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setSortKey(@android.support.annotation.Nullable
        java.lang.String sortKey) {
            mBuilderSortKey = sortKey;
            return this;
        }

        /**
         * Sets the progress information for the content pointed to by the recommendation.
         *
         * @param max
         * 		The maximum value for the progress of this content.
         * @param progress
         * 		The progress amount for this content. Must be in the range (0 - max).
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setProgress(int max, int progress) {
            if ((max < 0) || (progress < 0)) {
                throw new java.lang.IllegalArgumentException();
            }
            mBuilderProgressMax = max;
            mBuilderProgressAmount = progress;
            return this;
        }

        /**
         * Sets the flag indicating if the recommendation should be dismissed automatically.
         * <p>
         * Auto-dismiss notifications are automatically removed from the Home Screen when the user
         * clicks on them.
         *
         * @param autoDismiss
         * 		A boolean indicating if the recommendation should be auto dismissed or
         * 		not.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setAutoDismiss(boolean autoDismiss) {
            mBuilderAutoDismiss = autoDismiss;
            return this;
        }

        /**
         * Sets the data for the Intent that will be issued when the user clicks on the
         * recommendation.
         * <p>
         * The Intent data fields provided correspond to the fields passed into the
         * {@link PendingIntent} factory methods, when creating a new PendingIntent. The actual
         * PengindIntent object will only be created at the time a recommendation is posted to the
         * Home Screen.
         *
         * @param intentType
         * 		The type of {@link PendingIntent} to be created when posting this
         * 		recommendation.
         * @param intent
         * 		The Intent which to be issued when the recommendation is clicked on.
         * @param requestCode
         * 		The private request code to be used when creating the
         * 		{@link PendingIntent}
         * @param options
         * 		Only used for the Activity Intent type. Additional options for how the
         * 		Activity should be started. May be null if there are no options.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setContentIntentData(@android.support.app.recommendation.ContentRecommendation.IntentType
        int intentType, android.content.Intent intent, int requestCode, @android.support.annotation.Nullable
        android.os.Bundle options) {
            if (((intentType != android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_ACTIVITY) && (intentType != android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_BROADCAST)) && (intentType != android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_SERVICE)) {
                throw new java.lang.IllegalArgumentException("Invalid Intent type specified.");
            }
            mBuilderContentIntentData = new android.support.app.recommendation.ContentRecommendation.IntentData();
            mBuilderContentIntentData.mType = intentType;
            mBuilderContentIntentData.mIntent = android.support.app.recommendation.ContentRecommendation.checkNotNull(intent);
            mBuilderContentIntentData.mRequestCode = requestCode;
            mBuilderContentIntentData.mOptions = options;
            return this;
        }

        /**
         * Sets the data for the Intent that will be issued when the recommendation gets dismissed
         * from the Home Screen, due to an user action.
         * <p>
         * The Intent data fields provided correspond to the fields passed into the
         * {@link PendingIntent} factory methods, when creating a new PendingIntent. The actual
         * PengindIntent object will only be created at the time a recommendation is posted to the
         * Home Screen.
         *
         * @param intentType
         * 		The type of {@link PendingIntent} to be created when posting this
         * 		recommendation.
         * @param intent
         * 		The Intent which gets issued when the recommendation is dismissed from the
         * 		Home Screen.
         * @param requestCode
         * 		The private request code to be used when creating the
         * 		{@link PendingIntent}
         * @param options
         * 		Only used for the Activity Intent type. Additional options for how the
         * 		Activity should be started. May be null if there are no options.
         * @return The Builder object, for chaining.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setDismissIntentData(@android.support.app.recommendation.ContentRecommendation.IntentType
        int intentType, @android.support.annotation.Nullable
        android.content.Intent intent, int requestCode, @android.support.annotation.Nullable
        android.os.Bundle options) {
            if (intent != null) {
                if (((intentType != android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_ACTIVITY) && (intentType != android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_BROADCAST)) && (intentType != android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_SERVICE)) {
                    throw new java.lang.IllegalArgumentException("Invalid Intent type specified.");
                }
                mBuilderDismissIntentData = new android.support.app.recommendation.ContentRecommendation.IntentData();
                mBuilderDismissIntentData.mType = intentType;
                mBuilderDismissIntentData.mIntent = intent;
                mBuilderDismissIntentData.mRequestCode = requestCode;
                mBuilderDismissIntentData.mOptions = options;
            } else {
                mBuilderDismissIntentData = null;
            }
            return this;
        }

        /**
         * Sets the content types associated with the content recommendation. The first tag entry
         * will be considered the primary type for the content and will be used for ranking
         * purposes. Other secondary type tags may be provided, if applicable, and may be used for
         * filtering purposes.
         *
         * @param types
         * 		Array of predefined type tags (see the <code>CONTENT_TYPE_*</code>
         * 		constants) that describe the recommended content.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setContentTypes(java.lang.String[] types) {
            mBuilderContentTypes = android.support.app.recommendation.ContentRecommendation.checkNotNull(types);
            return this;
        }

        /**
         * Sets the content genres for the recommendation. These genres may be used for content
         * ranking. Genres are open ended String tags.
         * <p>
         * Some examples: "comedy", "action", "dance", "electronica", "racing", etc.
         *
         * @param genres
         * 		Array of genre string tags that describe the recommended content.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setGenres(java.lang.String[] genres) {
            mBuilderContentGenres = genres;
            return this;
        }

        /**
         * Sets the pricing and availability information for the recommendation. The provided
         * information will indicate the access model for the content (free, rental, purchase or
         * subscription) and the price value (if not free).
         *
         * @param priceType
         * 		Pricing type for this content. Must be one of the predefined pricing
         * 		type tags (see the <code>CONTENT_PRICING_*</code> constants).
         * @param priceValue
         * 		A string containing a representation of the content price in the
         * 		current locale and currency.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setPricingInformation(@android.support.app.recommendation.ContentRecommendation.ContentPricing
        java.lang.String priceType, @android.support.annotation.Nullable
        java.lang.String priceValue) {
            mBuilderPriceType = android.support.app.recommendation.ContentRecommendation.checkNotNull(priceType);
            mBuilderPriceValue = priceValue;
            return this;
        }

        /**
         * Sets the availability status for the content. This status indicates whether the referred
         * content is ready to be consumed on the device, or if the user must first purchase, rent,
         * subscribe to, or download the content.
         *
         * @param contentStatus
         * 		The status value for this content. Must be one of the predefined
         * 		content status values (see the <code>CONTENT_STATUS_*</code> constants).
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setStatus(@android.support.app.recommendation.ContentRecommendation.ContentStatus
        int contentStatus) {
            mBuilderStatus = contentStatus;
            return this;
        }

        /**
         * Sets the maturity level rating for the content.
         *
         * @param maturityRating
         * 		A tag indicating the maturity level rating for the content. This
         * 		tag must be one of the predefined maturity rating tags (see the <code>
         * 		CONTENT_MATURITY_*</code> constants).
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setMaturityRating(@android.support.app.recommendation.ContentRecommendation.ContentMaturity
        java.lang.String maturityRating) {
            mBuilderMaturityRating = android.support.app.recommendation.ContentRecommendation.checkNotNull(maturityRating);
            return this;
        }

        /**
         * Sets the running time (when applicable) for the content.
         *
         * @param length
         * 		The running time, in seconds, of the content.
         */
        public android.support.app.recommendation.ContentRecommendation.Builder setRunningTime(long length) {
            if (length < 0) {
                throw new java.lang.IllegalArgumentException();
            }
            mBuilderRunningTime = length;
            return this;
        }

        /**
         * Combine all of the options that have been set and return a new
         * {@link ContentRecommendation} object.
         */
        public android.support.app.recommendation.ContentRecommendation build() {
            return new android.support.app.recommendation.ContentRecommendation(this);
        }
    }

    /**
     * Returns a {@link android.app.Notification Notification} object which contains the content
     * recommendation data encapsulated by this object, which can be used for posting the
     * recommendation via the {@link android.app.NotificationManager NotificationManager}.
     *
     * @param context
     * 		A {@link Context} that will be used to construct the
     * 		{@link android.app.Notification Notification} object which will carry the
     * 		recommendation data.
     * @return A {@link android.app.Notification Notification} containing the stored recommendation
    data.
     */
    public android.app.Notification getNotificationObject(android.content.Context context) {
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context);
        android.support.app.recommendation.RecommendationExtender recExtender = new android.support.app.recommendation.RecommendationExtender();
        // Encode all the content recommendation data in a Notification object
        builder.setCategory(android.app.Notification.CATEGORY_RECOMMENDATION);
        builder.setContentTitle(mTitle);
        builder.setContentText(mText);
        builder.setContentInfo(mSourceName);
        builder.setLargeIcon(mContentImage);
        builder.setSmallIcon(mBadgeIconId);
        if (mBackgroundImageUri != null) {
            builder.getExtras().putString(android.app.Notification.EXTRA_BACKGROUND_IMAGE_URI, mBackgroundImageUri);
        }
        builder.setColor(mColor);
        builder.setGroup(mGroup);
        builder.setSortKey(mSortKey);
        builder.setProgress(mProgressMax, mProgressAmount, false);
        builder.setAutoCancel(mAutoDismiss);
        if (mContentIntentData != null) {
            android.app.PendingIntent contentPending;
            if (mContentIntentData.mType == android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_ACTIVITY) {
                contentPending = android.app.PendingIntent.getActivity(context, mContentIntentData.mRequestCode, mContentIntentData.mIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT, mContentIntentData.mOptions);
            } else
                if (mContentIntentData.mType == android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_SERVICE) {
                    contentPending = android.app.PendingIntent.getService(context, mContentIntentData.mRequestCode, mContentIntentData.mIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT);
                } else {
                    // Default:INTENT_TYPE_BROADCAST{
                    contentPending = android.app.PendingIntent.getBroadcast(context, mContentIntentData.mRequestCode, mContentIntentData.mIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT);
                }

            builder.setContentIntent(contentPending);
        }
        if (mDismissIntentData != null) {
            android.app.PendingIntent dismissPending;
            if (mDismissIntentData.mType == android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_ACTIVITY) {
                dismissPending = android.app.PendingIntent.getActivity(context, mDismissIntentData.mRequestCode, mDismissIntentData.mIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT, mDismissIntentData.mOptions);
            } else
                if (mDismissIntentData.mType == android.support.app.recommendation.ContentRecommendation.INTENT_TYPE_SERVICE) {
                    dismissPending = android.app.PendingIntent.getService(context, mDismissIntentData.mRequestCode, mDismissIntentData.mIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT);
                } else {
                    // Default:INTENT_TYPE_BROADCAST{
                    dismissPending = android.app.PendingIntent.getBroadcast(context, mDismissIntentData.mRequestCode, mDismissIntentData.mIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT);
                }

            builder.setDeleteIntent(dismissPending);
        }
        recExtender.setContentTypes(mContentTypes);
        recExtender.setGenres(mContentGenres);
        recExtender.setPricingInformation(mPriceType, mPriceValue);
        recExtender.setStatus(mStatus);
        recExtender.setMaturityRating(mMaturityRating);
        recExtender.setRunningTime(mRunningTime);
        builder.extend(recExtender);
        android.app.Notification notif = builder.build();
        return notif;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference
     * 		an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException
     * 		if {@code reference} is null
     */
    private static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new java.lang.NullPointerException();
        }
        return reference;
    }
}

