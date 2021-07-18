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


class MediaDescriptionCompatApi21 {
    public static java.lang.String getMediaId(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getMediaId();
    }

    public static java.lang.CharSequence getTitle(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getTitle();
    }

    public static java.lang.CharSequence getSubtitle(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getSubtitle();
    }

    public static java.lang.CharSequence getDescription(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getDescription();
    }

    public static android.graphics.Bitmap getIconBitmap(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getIconBitmap();
    }

    public static android.net.Uri getIconUri(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getIconUri();
    }

    public static android.os.Bundle getExtras(java.lang.Object descriptionObj) {
        return ((android.media.MediaDescription) (descriptionObj)).getExtras();
    }

    public static void writeToParcel(java.lang.Object descriptionObj, android.os.Parcel dest, int flags) {
        ((android.media.MediaDescription) (descriptionObj)).writeToParcel(dest, flags);
    }

    public static java.lang.Object fromParcel(android.os.Parcel in) {
        return android.media.MediaDescription.CREATOR.createFromParcel(in);
    }

    static class Builder {
        public static java.lang.Object newInstance() {
            return new android.media.MediaDescription.Builder();
        }

        public static void setMediaId(java.lang.Object builderObj, java.lang.String mediaId) {
            ((android.media.MediaDescription.Builder) (builderObj)).setMediaId(mediaId);
        }

        public static void setTitle(java.lang.Object builderObj, java.lang.CharSequence title) {
            ((android.media.MediaDescription.Builder) (builderObj)).setTitle(title);
        }

        public static void setSubtitle(java.lang.Object builderObj, java.lang.CharSequence subtitle) {
            ((android.media.MediaDescription.Builder) (builderObj)).setSubtitle(subtitle);
        }

        public static void setDescription(java.lang.Object builderObj, java.lang.CharSequence description) {
            ((android.media.MediaDescription.Builder) (builderObj)).setDescription(description);
        }

        public static void setIconBitmap(java.lang.Object builderObj, android.graphics.Bitmap iconBitmap) {
            ((android.media.MediaDescription.Builder) (builderObj)).setIconBitmap(iconBitmap);
        }

        public static void setIconUri(java.lang.Object builderObj, android.net.Uri iconUri) {
            ((android.media.MediaDescription.Builder) (builderObj)).setIconUri(iconUri);
        }

        public static void setExtras(java.lang.Object builderObj, android.os.Bundle extras) {
            ((android.media.MediaDescription.Builder) (builderObj)).setExtras(extras);
        }

        public static java.lang.Object build(java.lang.Object builderObj) {
            return ((android.media.MediaDescription.Builder) (builderObj)).build();
        }
    }
}

