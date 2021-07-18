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
package android.media.tv;


/**
 * TvContentRatingSystemInfo class provides information about a specific TV content rating system
 * defined either by a system app or by a third-party app.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class TvContentRatingSystemInfo implements android.os.Parcelable {
    private final android.net.Uri mXmlUri;

    private final android.content.pm.ApplicationInfo mApplicationInfo;

    /**
     * Creates a TvContentRatingSystemInfo object with given resource ID and receiver info.
     *
     * @param xmlResourceId
     * 		The ID of an XML resource whose root element is
     * 		<code> &lt;rating-system-definitions&gt;</code>
     * @param applicationInfo
     * 		Information about the application that provides the TV content rating
     * 		system definition.
     */
    public static final android.media.tv.TvContentRatingSystemInfo createTvContentRatingSystemInfo(int xmlResourceId, android.content.pm.ApplicationInfo applicationInfo) {
        android.net.Uri uri = new android.net.Uri.Builder().scheme(android.content.ContentResolver.SCHEME_ANDROID_RESOURCE).authority(applicationInfo.packageName).appendPath(java.lang.String.valueOf(xmlResourceId)).build();
        return new android.media.tv.TvContentRatingSystemInfo(uri, applicationInfo);
    }

    private TvContentRatingSystemInfo(android.net.Uri xmlUri, android.content.pm.ApplicationInfo applicationInfo) {
        mXmlUri = xmlUri;
        mApplicationInfo = applicationInfo;
    }

    /**
     * Returns {@code true} if the TV content rating system is defined by a system app,
     * {@code false} otherwise.
     */
    public final boolean isSystemDefined() {
        return (mApplicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     * Returns the URI to the XML resource that defines the TV content rating system.
     *
     * TODO: Remove. Instead, parse the XML resource and provide an interface to directly access
     * parsed information.
     */
    public final android.net.Uri getXmlUri() {
        return mXmlUri;
    }

    /**
     * Used to make this class parcelable.
     *
     * @unknown 
     */
    public static final android.os.Parcelable.Creator<android.media.tv.TvContentRatingSystemInfo> CREATOR = new android.os.Parcelable.Creator<android.media.tv.TvContentRatingSystemInfo>() {
        @java.lang.Override
        public android.media.tv.TvContentRatingSystemInfo createFromParcel(android.os.Parcel in) {
            return new android.media.tv.TvContentRatingSystemInfo(in);
        }

        @java.lang.Override
        public android.media.tv.TvContentRatingSystemInfo[] newArray(int size) {
            return new android.media.tv.TvContentRatingSystemInfo[size];
        }
    };

    private TvContentRatingSystemInfo(android.os.Parcel in) {
        mXmlUri = in.readParcelable(null);
        mApplicationInfo = in.readParcelable(null);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(mXmlUri, flags);
        dest.writeParcelable(mApplicationInfo, flags);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

