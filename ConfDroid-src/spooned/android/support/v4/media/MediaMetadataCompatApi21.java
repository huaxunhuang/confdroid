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


class MediaMetadataCompatApi21 {
    public static java.util.Set<java.lang.String> keySet(java.lang.Object metadataObj) {
        return ((android.media.MediaMetadata) (metadataObj)).keySet();
    }

    public static android.graphics.Bitmap getBitmap(java.lang.Object metadataObj, java.lang.String key) {
        return ((android.media.MediaMetadata) (metadataObj)).getBitmap(key);
    }

    public static long getLong(java.lang.Object metadataObj, java.lang.String key) {
        return ((android.media.MediaMetadata) (metadataObj)).getLong(key);
    }

    public static java.lang.Object getRating(java.lang.Object metadataObj, java.lang.String key) {
        return ((android.media.MediaMetadata) (metadataObj)).getRating(key);
    }

    public static java.lang.CharSequence getText(java.lang.Object metadataObj, java.lang.String key) {
        return ((android.media.MediaMetadata) (metadataObj)).getText(key);
    }

    public static void writeToParcel(java.lang.Object metadataObj, android.os.Parcel dest, int flags) {
        ((android.media.MediaMetadata) (metadataObj)).writeToParcel(dest, flags);
    }

    public static java.lang.Object createFromParcel(android.os.Parcel in) {
        return android.media.MediaMetadata.CREATOR.createFromParcel(in);
    }

    public static class Builder {
        public static java.lang.Object newInstance() {
            return new android.media.MediaMetadata.Builder();
        }

        public static void putBitmap(java.lang.Object builderObj, java.lang.String key, android.graphics.Bitmap value) {
            ((android.media.MediaMetadata.Builder) (builderObj)).putBitmap(key, value);
        }

        public static void putLong(java.lang.Object builderObj, java.lang.String key, long value) {
            ((android.media.MediaMetadata.Builder) (builderObj)).putLong(key, value);
        }

        public static void putRating(java.lang.Object builderObj, java.lang.String key, java.lang.Object ratingObj) {
            ((android.media.MediaMetadata.Builder) (builderObj)).putRating(key, ((android.media.Rating) (ratingObj)));
        }

        public static void putText(java.lang.Object builderObj, java.lang.String key, java.lang.CharSequence value) {
            ((android.media.MediaMetadata.Builder) (builderObj)).putText(key, value);
        }

        public static void putString(java.lang.Object builderObj, java.lang.String key, java.lang.String value) {
            ((android.media.MediaMetadata.Builder) (builderObj)).putString(key, value);
        }

        public static java.lang.Object build(java.lang.Object builderObj) {
            return ((android.media.MediaMetadata.Builder) (builderObj)).build();
        }
    }
}

