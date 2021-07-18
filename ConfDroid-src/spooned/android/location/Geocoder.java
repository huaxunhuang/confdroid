/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A class for handling geocoding and reverse geocoding.  Geocoding is
 * the process of transforming a street address or other description
 * of a location into a (latitude, longitude) coordinate.  Reverse
 * geocoding is the process of transforming a (latitude, longitude)
 * coordinate into a (partial) address.  The amount of detail in a
 * reverse geocoded location description may vary, for example one
 * might contain the full street address of the closest building, while
 * another might contain only a city name and postal code.
 *
 * The Geocoder class requires a backend service that is not included in
 * the core android framework.  The Geocoder query methods will return an
 * empty list if there no backend service in the platform.  Use the
 * isPresent() method to determine whether a Geocoder implementation
 * exists.
 */
public final class Geocoder {
    private static final java.lang.String TAG = "Geocoder";

    private android.location.GeocoderParams mParams;

    private android.location.ILocationManager mService;

    /**
     * Returns true if the Geocoder methods getFromLocation and
     * getFromLocationName are implemented.  Lack of network
     * connectivity may still cause these methods to return null or
     * empty lists.
     */
    public static boolean isPresent() {
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.LOCATION_SERVICE);
        android.location.ILocationManager lm = ILocationManager.Stub.asInterface(b);
        try {
            return lm.geocoderIsPresent();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.location.Geocoder.TAG, "isPresent: got RemoteException", e);
            return false;
        }
    }

    /**
     * Constructs a Geocoder whose responses will be localized for the
     * given Locale.
     *
     * @param context
     * 		the Context of the calling Activity
     * @param locale
     * 		the desired Locale for the query results
     * @throws NullPointerException
     * 		if Locale is null
     */
    public Geocoder(android.content.Context context, java.util.Locale locale) {
        if (locale == null) {
            throw new java.lang.NullPointerException("locale == null");
        }
        mParams = new android.location.GeocoderParams(context, locale);
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.LOCATION_SERVICE);
        mService = ILocationManager.Stub.asInterface(b);
    }

    /**
     * Constructs a Geocoder whose responses will be localized for the
     * default system Locale.
     *
     * @param context
     * 		the Context of the calling Activity
     */
    public Geocoder(android.content.Context context) {
        this(context, java.util.Locale.getDefault());
    }

    /**
     * Returns an array of Addresses that are known to describe the
     * area immediately surrounding the given latitude and longitude.
     * The returned addresses will be localized for the locale
     * provided to this class's constructor.
     *
     * <p> The returned values may be obtained by means of a network lookup.
     * The results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from your
     * primary UI thread.
     *
     * @param latitude
     * 		the latitude a point for the search
     * @param longitude
     * 		the longitude a point for the search
     * @param maxResults
     * 		max number of addresses to return. Smaller numbers (1 to 5) are recommended
     * @return a list of Address objects. Returns null or empty list if no matches were
    found or there is no backend service available.
     * @throws IllegalArgumentException
     * 		if latitude is
     * 		less than -90 or greater than 90
     * @throws IllegalArgumentException
     * 		if longitude is
     * 		less than -180 or greater than 180
     * @throws IOException
     * 		if the network is unavailable or any other
     * 		I/O problem occurs
     */
    public java.util.List<android.location.Address> getFromLocation(double latitude, double longitude, int maxResults) throws java.io.IOException {
        if ((latitude < (-90.0)) || (latitude > 90.0)) {
            throw new java.lang.IllegalArgumentException("latitude == " + latitude);
        }
        if ((longitude < (-180.0)) || (longitude > 180.0)) {
            throw new java.lang.IllegalArgumentException("longitude == " + longitude);
        }
        try {
            java.util.List<android.location.Address> results = new java.util.ArrayList<android.location.Address>();
            java.lang.String ex = mService.getFromLocation(latitude, longitude, maxResults, mParams, results);
            if (ex != null) {
                throw new java.io.IOException(ex);
            } else {
                return results;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.location.Geocoder.TAG, "getFromLocation: got RemoteException", e);
            return null;
        }
    }

    /**
     * Returns an array of Addresses that are known to describe the
     * named location, which may be a place name such as "Dalvik,
     * Iceland", an address such as "1600 Amphitheatre Parkway,
     * Mountain View, CA", an airport code such as "SFO", etc..  The
     * returned addresses will be localized for the locale provided to
     * this class's constructor.
     *
     * <p> The query will block and returned values will be obtained by means of a network lookup.
     * The results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from your
     * primary UI thread.
     *
     * @param locationName
     * 		a user-supplied description of a location
     * @param maxResults
     * 		max number of results to return. Smaller numbers (1 to 5) are recommended
     * @return a list of Address objects. Returns null or empty list if no matches were
    found or there is no backend service available.
     * @throws IllegalArgumentException
     * 		if locationName is null
     * @throws IOException
     * 		if the network is unavailable or any other
     * 		I/O problem occurs
     */
    public java.util.List<android.location.Address> getFromLocationName(java.lang.String locationName, int maxResults) throws java.io.IOException {
        if (locationName == null) {
            throw new java.lang.IllegalArgumentException("locationName == null");
        }
        try {
            java.util.List<android.location.Address> results = new java.util.ArrayList<android.location.Address>();
            java.lang.String ex = mService.getFromLocationName(locationName, 0, 0, 0, 0, maxResults, mParams, results);
            if (ex != null) {
                throw new java.io.IOException(ex);
            } else {
                return results;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.location.Geocoder.TAG, "getFromLocationName: got RemoteException", e);
            return null;
        }
    }

    /**
     * Returns an array of Addresses that are known to describe the
     * named location, which may be a place name such as "Dalvik,
     * Iceland", an address such as "1600 Amphitheatre Parkway,
     * Mountain View, CA", an airport code such as "SFO", etc..  The
     * returned addresses will be localized for the locale provided to
     * this class's constructor.
     *
     * <p> You may specify a bounding box for the search results by including
     * the Latitude and Longitude of the Lower Left point and Upper Right
     * point of the box.
     *
     * <p> The query will block and returned values will be obtained by means of a network lookup.
     * The results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from your
     * primary UI thread.
     *
     * @param locationName
     * 		a user-supplied description of a location
     * @param maxResults
     * 		max number of addresses to return. Smaller numbers (1 to 5) are recommended
     * @param lowerLeftLatitude
     * 		the latitude of the lower left corner of the bounding box
     * @param lowerLeftLongitude
     * 		the longitude of the lower left corner of the bounding box
     * @param upperRightLatitude
     * 		the latitude of the upper right corner of the bounding box
     * @param upperRightLongitude
     * 		the longitude of the upper right corner of the bounding box
     * @return a list of Address objects. Returns null or empty list if no matches were
    found or there is no backend service available.
     * @throws IllegalArgumentException
     * 		if locationName is null
     * @throws IllegalArgumentException
     * 		if any latitude is
     * 		less than -90 or greater than 90
     * @throws IllegalArgumentException
     * 		if any longitude is
     * 		less than -180 or greater than 180
     * @throws IOException
     * 		if the network is unavailable or any other
     * 		I/O problem occurs
     */
    public java.util.List<android.location.Address> getFromLocationName(java.lang.String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) throws java.io.IOException {
        if (locationName == null) {
            throw new java.lang.IllegalArgumentException("locationName == null");
        }
        if ((lowerLeftLatitude < (-90.0)) || (lowerLeftLatitude > 90.0)) {
            throw new java.lang.IllegalArgumentException("lowerLeftLatitude == " + lowerLeftLatitude);
        }
        if ((lowerLeftLongitude < (-180.0)) || (lowerLeftLongitude > 180.0)) {
            throw new java.lang.IllegalArgumentException("lowerLeftLongitude == " + lowerLeftLongitude);
        }
        if ((upperRightLatitude < (-90.0)) || (upperRightLatitude > 90.0)) {
            throw new java.lang.IllegalArgumentException("upperRightLatitude == " + upperRightLatitude);
        }
        if ((upperRightLongitude < (-180.0)) || (upperRightLongitude > 180.0)) {
            throw new java.lang.IllegalArgumentException("upperRightLongitude == " + upperRightLongitude);
        }
        try {
            java.util.ArrayList<android.location.Address> result = new java.util.ArrayList<android.location.Address>();
            java.lang.String ex = mService.getFromLocationName(locationName, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude, maxResults, mParams, result);
            if (ex != null) {
                throw new java.io.IOException(ex);
            } else {
                return result;
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.location.Geocoder.TAG, "getFromLocationName: got RemoteException", e);
            return null;
        }
    }
}

