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


class RatingCompatKitkat {
    public static java.lang.Object newUnratedRating(int ratingStyle) {
        return android.media.Rating.newUnratedRating(ratingStyle);
    }

    public static java.lang.Object newHeartRating(boolean hasHeart) {
        return android.media.Rating.newHeartRating(hasHeart);
    }

    public static java.lang.Object newThumbRating(boolean thumbIsUp) {
        return android.media.Rating.newThumbRating(thumbIsUp);
    }

    public static java.lang.Object newStarRating(int starRatingStyle, float starRating) {
        return android.media.Rating.newStarRating(starRatingStyle, starRating);
    }

    public static java.lang.Object newPercentageRating(float percent) {
        return android.media.Rating.newPercentageRating(percent);
    }

    public static boolean isRated(java.lang.Object ratingObj) {
        return ((android.media.Rating) (ratingObj)).isRated();
    }

    public static int getRatingStyle(java.lang.Object ratingObj) {
        return ((android.media.Rating) (ratingObj)).getRatingStyle();
    }

    public static boolean hasHeart(java.lang.Object ratingObj) {
        return ((android.media.Rating) (ratingObj)).hasHeart();
    }

    public static boolean isThumbUp(java.lang.Object ratingObj) {
        return ((android.media.Rating) (ratingObj)).isThumbUp();
    }

    public static float getStarRating(java.lang.Object ratingObj) {
        return ((android.media.Rating) (ratingObj)).getStarRating();
    }

    public static float getPercentRating(java.lang.Object ratingObj) {
        return ((android.media.Rating) (ratingObj)).getPercentRating();
    }
}

