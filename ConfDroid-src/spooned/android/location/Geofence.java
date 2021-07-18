/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Represents a geographical boundary, also known as a geofence.
 *
 * <p>Currently only circular geofences are supported and they do not support altitude changes.
 *
 * @unknown 
 */
public final class Geofence implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_HORIZONTAL_CIRCLE = 1;

    private final int mType;

    private final double mLatitude;

    private final double mLongitude;

    private final float mRadius;

    /**
     * Create a circular geofence (on a flat, horizontal plane).
     *
     * @param latitude
     * 		latitude in degrees, between -90 and +90 inclusive
     * @param longitude
     * 		longitude in degrees, between -180 and +180 inclusive
     * @param radius
     * 		radius in meters
     * @return a new geofence
     * @throws IllegalArgumentException
     * 		if any parameters are out of range
     */
    public static android.location.Geofence createCircle(double latitude, double longitude, float radius) {
        return new android.location.Geofence(latitude, longitude, radius);
    }

    private Geofence(double latitude, double longitude, float radius) {
        android.location.Geofence.checkRadius(radius);
        android.location.Geofence.checkLatLong(latitude, longitude);
        mType = android.location.Geofence.TYPE_HORIZONTAL_CIRCLE;
        mLatitude = latitude;
        mLongitude = longitude;
        mRadius = radius;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getType() {
        return mType;
    }

    /**
     *
     *
     * @unknown 
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     *
     *
     * @unknown 
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     *
     *
     * @unknown 
     */
    public float getRadius() {
        return mRadius;
    }

    private static void checkRadius(float radius) {
        if (radius <= 0) {
            throw new java.lang.IllegalArgumentException("invalid radius: " + radius);
        }
    }

    private static void checkLatLong(double latitude, double longitude) {
        if ((latitude > 90.0) || (latitude < (-90.0))) {
            throw new java.lang.IllegalArgumentException("invalid latitude: " + latitude);
        }
        if ((longitude > 180.0) || (longitude < (-180.0))) {
            throw new java.lang.IllegalArgumentException("invalid longitude: " + longitude);
        }
    }

    private static void checkType(int type) {
        if (type != android.location.Geofence.TYPE_HORIZONTAL_CIRCLE) {
            throw new java.lang.IllegalArgumentException("invalid type: " + type);
        }
    }

    public static final android.os.Parcelable.Creator<android.location.Geofence> CREATOR = new android.os.Parcelable.Creator<android.location.Geofence>() {
        @java.lang.Override
        public android.location.Geofence createFromParcel(android.os.Parcel in) {
            int type = in.readInt();
            double latitude = in.readDouble();
            double longitude = in.readDouble();
            float radius = in.readFloat();
            android.location.Geofence.checkType(type);
            return android.location.Geofence.createCircle(latitude, longitude, radius);
        }

        @java.lang.Override
        public android.location.Geofence[] newArray(int size) {
            return new android.location.Geofence[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mType);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeFloat(mRadius);
    }

    private static java.lang.String typeToString(int type) {
        switch (type) {
            case android.location.Geofence.TYPE_HORIZONTAL_CIRCLE :
                return "CIRCLE";
            default :
                android.location.Geofence.checkType(type);
                return null;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("Geofence[%s %.6f, %.6f %.0fm]", android.location.Geofence.typeToString(mType), mLatitude, mLongitude, mRadius);
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = java.lang.Double.doubleToLongBits(mLatitude);
        result = (prime * result) + ((int) (temp ^ (temp >>> 32)));
        temp = java.lang.Double.doubleToLongBits(mLongitude);
        result = (prime * result) + ((int) (temp ^ (temp >>> 32)));
        result = (prime * result) + java.lang.Float.floatToIntBits(mRadius);
        result = (prime * result) + mType;
        return result;
    }

    /**
     * Two geofences are equal if they have identical properties.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof android.location.Geofence))
            return false;

        android.location.Geofence other = ((android.location.Geofence) (obj));
        if (mRadius != other.mRadius)
            return false;

        if (mLatitude != other.mLatitude)
            return false;

        if (mLongitude != other.mLongitude)
            return false;

        if (mType != other.mType)
            return false;

        return true;
    }
}

