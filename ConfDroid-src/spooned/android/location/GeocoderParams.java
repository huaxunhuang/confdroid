/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.location;


/**
 * This class contains extra parameters to pass to an IGeocodeProvider
 * implementation from the Geocoder class.  Currently this contains the
 * language, country and variant information from the Geocoder's locale
 * as well as the Geocoder client's package name for geocoder server
 * logging.  This information is kept in a separate class to allow for
 * future expansion of the IGeocodeProvider interface.
 *
 * @unknown 
 */
public class GeocoderParams implements android.os.Parcelable {
    private java.util.Locale mLocale;

    private java.lang.String mPackageName;

    // used only for parcelling
    private GeocoderParams() {
    }

    /**
     * This object is only constructed by the Geocoder class
     *
     * @unknown 
     */
    public GeocoderParams(android.content.Context context, java.util.Locale locale) {
        mLocale = locale;
        mPackageName = context.getPackageName();
    }

    /**
     * returns the Geocoder's locale
     */
    public java.util.Locale getLocale() {
        return mLocale;
    }

    /**
     * returns the package name of the Geocoder's client
     */
    public java.lang.String getClientPackage() {
        return mPackageName;
    }

    public static final android.os.Parcelable.Creator<android.location.GeocoderParams> CREATOR = new android.os.Parcelable.Creator<android.location.GeocoderParams>() {
        public android.location.GeocoderParams createFromParcel(android.os.Parcel in) {
            android.location.GeocoderParams gp = new android.location.GeocoderParams();
            java.lang.String language = in.readString();
            java.lang.String country = in.readString();
            java.lang.String variant = in.readString();
            gp.mLocale = new java.util.Locale(language, country, variant);
            gp.mPackageName = in.readString();
            return gp;
        }

        public android.location.GeocoderParams[] newArray(int size) {
            return new android.location.GeocoderParams[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mLocale.getLanguage());
        parcel.writeString(mLocale.getCountry());
        parcel.writeString(mLocale.getVariant());
        parcel.writeString(mPackageName);
    }
}

