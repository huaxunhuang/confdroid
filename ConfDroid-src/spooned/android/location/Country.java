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
 * limitations under the License
 */
package android.location;


/**
 * This class wraps the country information.
 *
 * @unknown 
 */
public class Country implements android.os.Parcelable {
    /**
     * The country code came from the mobile network
     */
    public static final int COUNTRY_SOURCE_NETWORK = 0;

    /**
     * The country code came from the location service
     */
    public static final int COUNTRY_SOURCE_LOCATION = 1;

    /**
     * The country code was read from the SIM card
     */
    public static final int COUNTRY_SOURCE_SIM = 2;

    /**
     * The country code came from the system locale setting
     */
    public static final int COUNTRY_SOURCE_LOCALE = 3;

    /**
     * The ISO 3166-1 two letters country code.
     */
    private final java.lang.String mCountryIso;

    /**
     * Where the country code came from.
     */
    private final int mSource;

    private int mHashCode;

    /**
     * Time that this object was created (which we assume to be the time that the source was
     * consulted). This time is in milliseconds since boot up.
     */
    private final long mTimestamp;

    /**
     *
     *
     * @param countryIso
     * 		the ISO 3166-1 two letters country code.
     * @param source
     * 		where the countryIso came from, could be one of below
     * 		values
     * 		<p>
     * 		<ul>
     * 		<li>{@link #COUNTRY_SOURCE_NETWORK}</li>
     * 		<li>{@link #COUNTRY_SOURCE_LOCATION}</li>
     * 		<li>{@link #COUNTRY_SOURCE_SIM}</li>
     * 		<li>{@link #COUNTRY_SOURCE_LOCALE}</li>
     * 		</ul>
     */
    public Country(final java.lang.String countryIso, final int source) {
        if (((countryIso == null) || (source < android.location.Country.COUNTRY_SOURCE_NETWORK)) || (source > android.location.Country.COUNTRY_SOURCE_LOCALE)) {
            throw new java.lang.IllegalArgumentException();
        }
        mCountryIso = countryIso.toUpperCase(java.util.Locale.US);
        mSource = source;
        mTimestamp = android.os.SystemClock.elapsedRealtime();
    }

    private Country(final java.lang.String countryIso, final int source, long timestamp) {
        if (((countryIso == null) || (source < android.location.Country.COUNTRY_SOURCE_NETWORK)) || (source > android.location.Country.COUNTRY_SOURCE_LOCALE)) {
            throw new java.lang.IllegalArgumentException();
        }
        mCountryIso = countryIso.toUpperCase(java.util.Locale.US);
        mSource = source;
        mTimestamp = timestamp;
    }

    public Country(android.location.Country country) {
        mCountryIso = country.mCountryIso;
        mSource = country.mSource;
        mTimestamp = country.mTimestamp;
    }

    /**
     *
     *
     * @return the ISO 3166-1 two letters country code
     */
    public final java.lang.String getCountryIso() {
        return mCountryIso;
    }

    /**
     *
     *
     * @return where the country code came from, could be one of below values
    <p>
    <ul>
    <li>{@link #COUNTRY_SOURCE_NETWORK}</li>
    <li>{@link #COUNTRY_SOURCE_LOCATION}</li>
    <li>{@link #COUNTRY_SOURCE_SIM}</li>
    <li>{@link #COUNTRY_SOURCE_LOCALE}</li>
    </ul>
     */
    public final int getSource() {
        return mSource;
    }

    /**
     * Returns the time that this object was created (which we assume to be the time that the source
     * was consulted).
     */
    public final long getTimestamp() {
        return mTimestamp;
    }

    public static final android.os.Parcelable.Creator<android.location.Country> CREATOR = new android.os.Parcelable.Creator<android.location.Country>() {
        public android.location.Country createFromParcel(android.os.Parcel in) {
            return new android.location.Country(in.readString(), in.readInt(), in.readLong());
        }

        public android.location.Country[] newArray(int size) {
            return new android.location.Country[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mCountryIso);
        parcel.writeInt(mSource);
        parcel.writeLong(mTimestamp);
    }

    /**
     * Returns true if this {@link Country} is equivalent to the given object. This ignores
     * the timestamp value and just checks for equivalence of countryIso and source values.
     * Returns false otherwise.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof android.location.Country) {
            android.location.Country c = ((android.location.Country) (object));
            // No need to check the equivalence of the timestamp
            return mCountryIso.equals(c.getCountryIso()) && (mSource == c.getSource());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        int hash = mHashCode;
        if (hash == 0) {
            hash = 17;
            hash = (hash * 13) + mCountryIso.hashCode();
            hash = (hash * 13) + mSource;
            mHashCode = hash;
        }
        return mHashCode;
    }

    /**
     * Compare the specified country to this country object ignoring the source
     * and timestamp fields, return true if the countryIso fields are equal
     *
     * @param country
     * 		the country to compare
     * @return true if the specified country's countryIso field is equal to this
    country's, false otherwise.
     */
    public boolean equalsIgnoreSource(android.location.Country country) {
        return (country != null) && mCountryIso.equals(country.getCountryIso());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("Country {ISO=" + mCountryIso) + ", source=") + mSource) + ", time=") + mTimestamp) + "}";
    }
}

