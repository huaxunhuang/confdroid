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
 * A class to encapsulate rating information used as content metadata.
 * A rating is defined by its rating style (see {@link #RATING_HEART},
 * {@link #RATING_THUMB_UP_DOWN}, {@link #RATING_3_STARS}, {@link #RATING_4_STARS},
 * {@link #RATING_5_STARS} or {@link #RATING_PERCENTAGE}) and the actual rating value (which may
 * be defined as "unrated"), both of which are defined when the rating instance is constructed
 * through one of the factory methods.
 */
public final class Rating implements android.os.Parcelable {
    private static final java.lang.String TAG = "Rating";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.Rating.RATING_NONE, android.media.Rating.RATING_HEART, android.media.Rating.RATING_THUMB_UP_DOWN, android.media.Rating.RATING_3_STARS, android.media.Rating.RATING_4_STARS, android.media.Rating.RATING_5_STARS, android.media.Rating.RATING_PERCENTAGE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Style {}

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.Rating.RATING_3_STARS, android.media.Rating.RATING_4_STARS, android.media.Rating.RATING_5_STARS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface StarStyle {}

    /**
     * Indicates a rating style is not supported. A Rating will never have this
     * type, but can be used by other classes to indicate they do not support
     * Rating.
     */
    public static final int RATING_NONE = 0;

    /**
     * A rating style with a single degree of rating, "heart" vs "no heart". Can be used to
     * indicate the content referred to is a favorite (or not).
     */
    public static final int RATING_HEART = 1;

    /**
     * A rating style for "thumb up" vs "thumb down".
     */
    public static final int RATING_THUMB_UP_DOWN = 2;

    /**
     * A rating style with 0 to 3 stars.
     */
    public static final int RATING_3_STARS = 3;

    /**
     * A rating style with 0 to 4 stars.
     */
    public static final int RATING_4_STARS = 4;

    /**
     * A rating style with 0 to 5 stars.
     */
    public static final int RATING_5_STARS = 5;

    /**
     * A rating style expressed as a percentage.
     */
    public static final int RATING_PERCENTAGE = 6;

    private static final float RATING_NOT_RATED = -1.0F;

    private final int mRatingStyle;

    private final float mRatingValue;

    private Rating(@android.media.Rating.Style
    int ratingStyle, float rating) {
        mRatingStyle = ratingStyle;
        mRatingValue = rating;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("Rating:style=" + mRatingStyle) + " rating=") + (mRatingValue < 0.0F ? "unrated" : java.lang.String.valueOf(mRatingValue));
    }

    @java.lang.Override
    public int describeContents() {
        return mRatingStyle;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mRatingStyle);
        dest.writeFloat(mRatingValue);
    }

    public static final android.os.Parcelable.Creator<android.media.Rating> CREATOR = new android.os.Parcelable.Creator<android.media.Rating>() {
        /**
         * Rebuilds a Rating previously stored with writeToParcel().
         *
         * @param p
         * 		Parcel object to read the Rating from
         * @return a new Rating created from the data in the parcel
         */
        @java.lang.Override
        public android.media.Rating createFromParcel(android.os.Parcel p) {
            return new android.media.Rating(p.readInt(), p.readFloat());
        }

        @java.lang.Override
        public android.media.Rating[] newArray(int size) {
            return new android.media.Rating[size];
        }
    };

    /**
     * Return a Rating instance with no rating.
     * Create and return a new Rating instance with no rating known for the given
     * rating style.
     *
     * @param ratingStyle
     * 		one of {@link #RATING_HEART}, {@link #RATING_THUMB_UP_DOWN},
     * 		{@link #RATING_3_STARS}, {@link #RATING_4_STARS}, {@link #RATING_5_STARS},
     * 		or {@link #RATING_PERCENTAGE}.
     * @return null if an invalid rating style is passed, a new Rating instance otherwise.
     */
    public static android.media.Rating newUnratedRating(@android.media.Rating.Style
    int ratingStyle) {
        switch (ratingStyle) {
            case android.media.Rating.RATING_HEART :
            case android.media.Rating.RATING_THUMB_UP_DOWN :
            case android.media.Rating.RATING_3_STARS :
            case android.media.Rating.RATING_4_STARS :
            case android.media.Rating.RATING_5_STARS :
            case android.media.Rating.RATING_PERCENTAGE :
                return new android.media.Rating(ratingStyle, android.media.Rating.RATING_NOT_RATED);
            default :
                return null;
        }
    }

    /**
     * Return a Rating instance with a heart-based rating.
     * Create and return a new Rating instance with a rating style of {@link #RATING_HEART},
     * and a heart-based rating.
     *
     * @param hasHeart
     * 		true for a "heart selected" rating, false for "heart unselected".
     * @return a new Rating instance.
     */
    public static android.media.Rating newHeartRating(boolean hasHeart) {
        return new android.media.Rating(android.media.Rating.RATING_HEART, hasHeart ? 1.0F : 0.0F);
    }

    /**
     * Return a Rating instance with a thumb-based rating.
     * Create and return a new Rating instance with a {@link #RATING_THUMB_UP_DOWN}
     * rating style, and a "thumb up" or "thumb down" rating.
     *
     * @param thumbIsUp
     * 		true for a "thumb up" rating, false for "thumb down".
     * @return a new Rating instance.
     */
    public static android.media.Rating newThumbRating(boolean thumbIsUp) {
        return new android.media.Rating(android.media.Rating.RATING_THUMB_UP_DOWN, thumbIsUp ? 1.0F : 0.0F);
    }

    /**
     * Return a Rating instance with a star-based rating.
     * Create and return a new Rating instance with one of the star-base rating styles
     * and the given integer or fractional number of stars. Non integer values can for instance
     * be used to represent an average rating value, which might not be an integer number of stars.
     *
     * @param starRatingStyle
     * 		one of {@link #RATING_3_STARS}, {@link #RATING_4_STARS},
     * 		{@link #RATING_5_STARS}.
     * @param starRating
     * 		a number ranging from 0.0f to 3.0f, 4.0f or 5.0f according to
     * 		the rating style.
     * @return null if the rating style is invalid, or the rating is out of range,
    a new Rating instance otherwise.
     */
    public static android.media.Rating newStarRating(@android.media.Rating.StarStyle
    int starRatingStyle, float starRating) {
        float maxRating = -1.0F;
        switch (starRatingStyle) {
            case android.media.Rating.RATING_3_STARS :
                maxRating = 3.0F;
                break;
            case android.media.Rating.RATING_4_STARS :
                maxRating = 4.0F;
                break;
            case android.media.Rating.RATING_5_STARS :
                maxRating = 5.0F;
                break;
            default :
                android.util.Log.e(android.media.Rating.TAG, ("Invalid rating style (" + starRatingStyle) + ") for a star rating");
                return null;
        }
        if ((starRating < 0.0F) || (starRating > maxRating)) {
            android.util.Log.e(android.media.Rating.TAG, "Trying to set out of range star-based rating");
            return null;
        }
        return new android.media.Rating(starRatingStyle, starRating);
    }

    /**
     * Return a Rating instance with a percentage-based rating.
     * Create and return a new Rating instance with a {@link #RATING_PERCENTAGE}
     * rating style, and a rating of the given percentage.
     *
     * @param percent
     * 		the value of the rating
     * @return null if the rating is out of range, a new Rating instance otherwise.
     */
    public static android.media.Rating newPercentageRating(float percent) {
        if ((percent < 0.0F) || (percent > 100.0F)) {
            android.util.Log.e(android.media.Rating.TAG, "Invalid percentage-based rating value");
            return null;
        } else {
            return new android.media.Rating(android.media.Rating.RATING_PERCENTAGE, percent);
        }
    }

    /**
     * Return whether there is a rating value available.
     *
     * @return true if the instance was not created with {@link #newUnratedRating(int)}.
     */
    public boolean isRated() {
        return mRatingValue >= 0.0F;
    }

    /**
     * Return the rating style.
     *
     * @return one of {@link #RATING_HEART}, {@link #RATING_THUMB_UP_DOWN},
    {@link #RATING_3_STARS}, {@link #RATING_4_STARS}, {@link #RATING_5_STARS},
    or {@link #RATING_PERCENTAGE}.
     */
    @android.media.Rating.Style
    public int getRatingStyle() {
        return mRatingStyle;
    }

    /**
     * Return whether the rating is "heart selected".
     *
     * @return true if the rating is "heart selected", false if the rating is "heart unselected",
    if the rating style is not {@link #RATING_HEART} or if it is unrated.
     */
    public boolean hasHeart() {
        if (mRatingStyle != android.media.Rating.RATING_HEART) {
            return false;
        } else {
            return mRatingValue == 1.0F;
        }
    }

    /**
     * Return whether the rating is "thumb up".
     *
     * @return true if the rating is "thumb up", false if the rating is "thumb down",
    if the rating style is not {@link #RATING_THUMB_UP_DOWN} or if it is unrated.
     */
    public boolean isThumbUp() {
        if (mRatingStyle != android.media.Rating.RATING_THUMB_UP_DOWN) {
            return false;
        } else {
            return mRatingValue == 1.0F;
        }
    }

    /**
     * Return the star-based rating value.
     *
     * @return a rating value greater or equal to 0.0f, or a negative value if the rating style is
    not star-based, or if it is unrated.
     */
    public float getStarRating() {
        switch (mRatingStyle) {
            case android.media.Rating.RATING_3_STARS :
            case android.media.Rating.RATING_4_STARS :
            case android.media.Rating.RATING_5_STARS :
                if (isRated()) {
                    return mRatingValue;
                }
            default :
                return -1.0F;
        }
    }

    /**
     * Return the percentage-based rating value.
     *
     * @return a rating value greater or equal to 0.0f, or a negative value if the rating style is
    not percentage-based, or if it is unrated.
     */
    public float getPercentRating() {
        if ((mRatingStyle != android.media.Rating.RATING_PERCENTAGE) || (!isRated())) {
            return -1.0F;
        } else {
            return mRatingValue;
        }
    }
}

