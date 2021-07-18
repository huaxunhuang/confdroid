/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.hardware;


/**
 * Estimates magnetic field at a given point on
 * Earth, and in particular, to compute the magnetic declination from true
 * north.
 *
 * <p>This uses the World Magnetic Model produced by the United States National
 * Geospatial-Intelligence Agency.  More details about the model can be found at
 * <a href="http://www.ngdc.noaa.gov/geomag/WMM/DoDWMM.shtml">http://www.ngdc.noaa.gov/geomag/WMM/DoDWMM.shtml</a>.
 * This class currently uses WMM-2010 which is valid until 2015, but should
 * produce acceptable results for several years after that. Future versions of
 * Android may use a newer version of the model.
 */
public class GeomagneticField {
    // The magnetic field at a given point, in nonoteslas in geodetic
    // coordinates.
    private float mX;

    private float mY;

    private float mZ;

    // Geocentric coordinates -- set by computeGeocentricCoordinates.
    private float mGcLatitudeRad;

    private float mGcLongitudeRad;

    private float mGcRadiusKm;

    // Constants from WGS84 (the coordinate system used by GPS)
    private static final float EARTH_SEMI_MAJOR_AXIS_KM = 6378.137F;

    private static final float EARTH_SEMI_MINOR_AXIS_KM = 6356.7524F;

    private static final float EARTH_REFERENCE_RADIUS_KM = 6371.2F;

    // These coefficients and the formulae used below are from:
    // NOAA Technical Report: The US/UK World Magnetic Model for 2010-2015
    private static final float[][] G_COEFF = new float[][]{ new float[]{ 0.0F }, new float[]{ -29496.6F, -1586.3F }, new float[]{ -2396.6F, 3026.1F, 1668.6F }, new float[]{ 1340.1F, -2326.2F, 1231.9F, 634.0F }, new float[]{ 912.6F, 808.9F, 166.7F, -357.1F, 89.4F }, new float[]{ -230.9F, 357.2F, 200.3F, -141.1F, -163.0F, -7.8F }, new float[]{ 72.8F, 68.6F, 76.0F, -141.4F, -22.8F, 13.2F, -77.9F }, new float[]{ 80.5F, -75.1F, -4.7F, 45.3F, 13.9F, 10.4F, 1.7F, 4.9F }, new float[]{ 24.4F, 8.1F, -14.5F, -5.6F, -19.3F, 11.5F, 10.9F, -14.1F, -3.7F }, new float[]{ 5.4F, 9.4F, 3.4F, -5.2F, 3.1F, -12.4F, -0.7F, 8.4F, -8.5F, -10.1F }, new float[]{ -2.0F, -6.3F, 0.9F, -1.1F, -0.2F, 2.5F, -0.3F, 2.2F, 3.1F, -1.0F, -2.8F }, new float[]{ 3.0F, -1.5F, -2.1F, 1.7F, -0.5F, 0.5F, -0.8F, 0.4F, 1.8F, 0.1F, 0.7F, 3.8F }, new float[]{ -2.2F, -0.2F, 0.3F, 1.0F, -0.6F, 0.9F, -0.1F, 0.5F, -0.4F, -0.4F, 0.2F, -0.8F, 0.0F } };

    private static final float[][] H_COEFF = new float[][]{ new float[]{ 0.0F }, new float[]{ 0.0F, 4944.4F }, new float[]{ 0.0F, -2707.7F, -576.1F }, new float[]{ 0.0F, -160.2F, 251.9F, -536.6F }, new float[]{ 0.0F, 286.4F, -211.2F, 164.3F, -309.1F }, new float[]{ 0.0F, 44.6F, 188.9F, -118.2F, 0.0F, 100.9F }, new float[]{ 0.0F, -20.8F, 44.1F, 61.5F, -66.3F, 3.1F, 55.0F }, new float[]{ 0.0F, -57.9F, -21.1F, 6.5F, 24.9F, 7.0F, -27.7F, -3.3F }, new float[]{ 0.0F, 11.0F, -20.0F, 11.9F, -17.4F, 16.7F, 7.0F, -10.8F, 1.7F }, new float[]{ 0.0F, -20.5F, 11.5F, 12.8F, -7.2F, -7.4F, 8.0F, 2.1F, -6.1F, 7.0F }, new float[]{ 0.0F, 2.8F, -0.1F, 4.7F, 4.4F, -7.2F, -1.0F, -3.9F, -2.0F, -2.0F, -8.3F }, new float[]{ 0.0F, 0.2F, 1.7F, -0.6F, -1.8F, 0.9F, -0.4F, -2.5F, -1.3F, -2.1F, -1.9F, -1.8F }, new float[]{ 0.0F, -0.9F, 0.3F, 2.1F, -2.5F, 0.5F, 0.6F, 0.0F, 0.1F, 0.3F, -0.9F, -0.2F, 0.9F } };

    private static final float[][] DELTA_G = new float[][]{ new float[]{ 0.0F }, new float[]{ 11.6F, 16.5F }, new float[]{ -12.1F, -4.4F, 1.9F }, new float[]{ 0.4F, -4.1F, -2.9F, -7.7F }, new float[]{ -1.8F, 2.3F, -8.7F, 4.6F, -2.1F }, new float[]{ -1.0F, 0.6F, -1.8F, -1.0F, 0.9F, 1.0F }, new float[]{ -0.2F, -0.2F, -0.1F, 2.0F, -1.7F, -0.3F, 1.7F }, new float[]{ 0.1F, -0.1F, -0.6F, 1.3F, 0.4F, 0.3F, -0.7F, 0.6F }, new float[]{ -0.1F, 0.1F, -0.6F, 0.2F, -0.2F, 0.3F, 0.3F, -0.6F, 0.2F }, new float[]{ 0.0F, -0.1F, 0.0F, 0.3F, -0.4F, -0.3F, 0.1F, -0.1F, -0.4F, -0.2F }, new float[]{ 0.0F, 0.0F, -0.1F, 0.2F, 0.0F, -0.1F, -0.2F, 0.0F, -0.1F, -0.2F, -0.2F }, new float[]{ 0.0F, 0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1F, 0.0F }, new float[]{ 0.0F, 0.0F, 0.1F, 0.1F, -0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1F, 0.1F } };

    private static final float[][] DELTA_H = new float[][]{ new float[]{ 0.0F }, new float[]{ 0.0F, -25.9F }, new float[]{ 0.0F, -22.5F, -11.8F }, new float[]{ 0.0F, 7.3F, -3.9F, -2.6F }, new float[]{ 0.0F, 1.1F, 2.7F, 3.9F, -0.8F }, new float[]{ 0.0F, 0.4F, 1.8F, 1.2F, 4.0F, -0.6F }, new float[]{ 0.0F, -0.2F, -2.1F, -0.4F, -0.6F, 0.5F, 0.9F }, new float[]{ 0.0F, 0.7F, 0.3F, -0.1F, -0.1F, -0.8F, -0.3F, 0.3F }, new float[]{ 0.0F, -0.1F, 0.2F, 0.4F, 0.4F, 0.1F, -0.1F, 0.4F, 0.3F }, new float[]{ 0.0F, 0.0F, -0.2F, 0.0F, -0.1F, 0.1F, 0.0F, -0.2F, 0.3F, 0.2F }, new float[]{ 0.0F, 0.1F, -0.1F, 0.0F, -0.1F, -0.1F, 0.0F, -0.1F, -0.2F, 0.0F, -0.1F }, new float[]{ 0.0F, 0.0F, 0.1F, 0.0F, 0.1F, 0.0F, 0.1F, 0.0F, -0.1F, -0.1F, 0.0F, -0.1F }, new float[]{ 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F } };

    private static final long BASE_TIME = new java.util.GregorianCalendar(2010, 1, 1).getTimeInMillis();

    // The ratio between the Gauss-normalized associated Legendre functions and
    // the Schmid quasi-normalized ones. Compute these once staticly since they
    // don't depend on input variables at all.
    private static final float[][] SCHMIDT_QUASI_NORM_FACTORS = android.hardware.GeomagneticField.computeSchmidtQuasiNormFactors(android.hardware.GeomagneticField.G_COEFF.length);

    /**
     * Estimate the magnetic field at a given point and time.
     *
     * @param gdLatitudeDeg
     * 		Latitude in WGS84 geodetic coordinates -- positive is east.
     * @param gdLongitudeDeg
     * 		Longitude in WGS84 geodetic coordinates -- positive is north.
     * @param altitudeMeters
     * 		Altitude in WGS84 geodetic coordinates, in meters.
     * @param timeMillis
     * 		Time at which to evaluate the declination, in milliseconds
     * 		since January 1, 1970. (approximate is fine -- the declination
     * 		changes very slowly).
     */
    public GeomagneticField(float gdLatitudeDeg, float gdLongitudeDeg, float altitudeMeters, long timeMillis) {
        final int MAX_N = android.hardware.GeomagneticField.G_COEFF.length;// Maximum degree of the coefficients.

        // We don't handle the north and south poles correctly -- pretend that
        // we're not quite at them to avoid crashing.
        gdLatitudeDeg = java.lang.Math.min(90.0F - 1.0E-5F, java.lang.Math.max((-90.0F) + 1.0E-5F, gdLatitudeDeg));
        computeGeocentricCoordinates(gdLatitudeDeg, gdLongitudeDeg, altitudeMeters);
        assert android.hardware.GeomagneticField.G_COEFF.length == android.hardware.GeomagneticField.H_COEFF.length;
        // Note: LegendreTable computes associated Legendre functions for
        // cos(theta).  We want the associated Legendre functions for
        // sin(latitude), which is the same as cos(PI/2 - latitude), except the
        // derivate will be negated.
        android.hardware.GeomagneticField.LegendreTable legendre = new android.hardware.GeomagneticField.LegendreTable(MAX_N - 1, ((float) ((java.lang.Math.PI / 2.0) - mGcLatitudeRad)));
        // Compute a table of (EARTH_REFERENCE_RADIUS_KM / radius)^n for i in
        // 0..MAX_N-2 (this is much faster than calling Math.pow MAX_N+1 times).
        float[] relativeRadiusPower = new float[MAX_N + 2];
        relativeRadiusPower[0] = 1.0F;
        relativeRadiusPower[1] = android.hardware.GeomagneticField.EARTH_REFERENCE_RADIUS_KM / mGcRadiusKm;
        for (int i = 2; i < relativeRadiusPower.length; ++i) {
            relativeRadiusPower[i] = relativeRadiusPower[i - 1] * relativeRadiusPower[1];
        }
        // Compute tables of sin(lon * m) and cos(lon * m) for m = 0..MAX_N --
        // this is much faster than calling Math.sin and Math.com MAX_N+1 times.
        float[] sinMLon = new float[MAX_N];
        float[] cosMLon = new float[MAX_N];
        sinMLon[0] = 0.0F;
        cosMLon[0] = 1.0F;
        sinMLon[1] = ((float) (java.lang.Math.sin(mGcLongitudeRad)));
        cosMLon[1] = ((float) (java.lang.Math.cos(mGcLongitudeRad)));
        for (int m = 2; m < MAX_N; ++m) {
            // Standard expansions for sin((m-x)*theta + x*theta) and
            // cos((m-x)*theta + x*theta).
            int x = m >> 1;
            sinMLon[m] = (sinMLon[m - x] * cosMLon[x]) + (cosMLon[m - x] * sinMLon[x]);
            cosMLon[m] = (cosMLon[m - x] * cosMLon[x]) - (sinMLon[m - x] * sinMLon[x]);
        }
        float inverseCosLatitude = 1.0F / ((float) (java.lang.Math.cos(mGcLatitudeRad)));
        float yearsSinceBase = (timeMillis - android.hardware.GeomagneticField.BASE_TIME) / ((((365.0F * 24.0F) * 60.0F) * 60.0F) * 1000.0F);
        // We now compute the magnetic field strength given the geocentric
        // location. The magnetic field is the derivative of the potential
        // function defined by the model. See NOAA Technical Report: The US/UK
        // World Magnetic Model for 2010-2015 for the derivation.
        float gcX = 0.0F;// Geocentric northwards component.

        float gcY = 0.0F;// Geocentric eastwards component.

        float gcZ = 0.0F;// Geocentric downwards component.

        for (int n = 1; n < MAX_N; n++) {
            for (int m = 0; m <= n; m++) {
                // Adjust the coefficients for the current date.
                float g = android.hardware.GeomagneticField.G_COEFF[n][m] + (yearsSinceBase * android.hardware.GeomagneticField.DELTA_G[n][m]);
                float h = android.hardware.GeomagneticField.H_COEFF[n][m] + (yearsSinceBase * android.hardware.GeomagneticField.DELTA_H[n][m]);
                // Negative derivative with respect to latitude, divided by
                // radius.  This looks like the negation of the version in the
                // NOAA Techincal report because that report used
                // P_n^m(sin(theta)) and we use P_n^m(cos(90 - theta)), so the
                // derivative with respect to theta is negated.
                gcX += ((relativeRadiusPower[n + 2] * ((g * cosMLon[m]) + (h * sinMLon[m]))) * legendre.mPDeriv[n][m]) * android.hardware.GeomagneticField.SCHMIDT_QUASI_NORM_FACTORS[n][m];
                // Negative derivative with respect to longitude, divided by
                // radius.
                gcY += ((((relativeRadiusPower[n + 2] * m) * ((g * sinMLon[m]) - (h * cosMLon[m]))) * legendre.mP[n][m]) * android.hardware.GeomagneticField.SCHMIDT_QUASI_NORM_FACTORS[n][m]) * inverseCosLatitude;
                // Negative derivative with respect to radius.
                gcZ -= ((((n + 1) * relativeRadiusPower[n + 2]) * ((g * cosMLon[m]) + (h * sinMLon[m]))) * legendre.mP[n][m]) * android.hardware.GeomagneticField.SCHMIDT_QUASI_NORM_FACTORS[n][m];
            }
        }
        // Convert back to geodetic coordinates.  This is basically just a
        // rotation around the Y-axis by the difference in latitudes between the
        // geocentric frame and the geodetic frame.
        double latDiffRad = java.lang.Math.toRadians(gdLatitudeDeg) - mGcLatitudeRad;
        mX = ((float) ((gcX * java.lang.Math.cos(latDiffRad)) + (gcZ * java.lang.Math.sin(latDiffRad))));
        mY = gcY;
        mZ = ((float) (((-gcX) * java.lang.Math.sin(latDiffRad)) + (gcZ * java.lang.Math.cos(latDiffRad))));
    }

    /**
     *
     *
     * @return The X (northward) component of the magnetic field in nanoteslas.
     */
    public float getX() {
        return mX;
    }

    /**
     *
     *
     * @return The Y (eastward) component of the magnetic field in nanoteslas.
     */
    public float getY() {
        return mY;
    }

    /**
     *
     *
     * @return The Z (downward) component of the magnetic field in nanoteslas.
     */
    public float getZ() {
        return mZ;
    }

    /**
     *
     *
     * @return The declination of the horizontal component of the magnetic
    field from true north, in degrees (i.e. positive means the
    magnetic field is rotated east that much from true north).
     */
    public float getDeclination() {
        return ((float) (java.lang.Math.toDegrees(java.lang.Math.atan2(mY, mX))));
    }

    /**
     *
     *
     * @return The inclination of the magnetic field in degrees -- positive
    means the magnetic field is rotated downwards.
     */
    public float getInclination() {
        return ((float) (java.lang.Math.toDegrees(java.lang.Math.atan2(mZ, getHorizontalStrength()))));
    }

    /**
     *
     *
     * @return Horizontal component of the field strength in nonoteslas.
     */
    public float getHorizontalStrength() {
        return ((float) (java.lang.Math.hypot(mX, mY)));
    }

    /**
     *
     *
     * @return Total field strength in nanoteslas.
     */
    public float getFieldStrength() {
        return ((float) (java.lang.Math.sqrt(((mX * mX) + (mY * mY)) + (mZ * mZ))));
    }

    /**
     *
     *
     * @param gdLatitudeDeg
     * 		Latitude in WGS84 geodetic coordinates.
     * @param gdLongitudeDeg
     * 		Longitude in WGS84 geodetic coordinates.
     * @param altitudeMeters
     * 		Altitude above sea level in WGS84 geodetic coordinates.
     * @return Geocentric latitude (i.e. angle between closest point on the
    equator and this point, at the center of the earth.
     */
    private void computeGeocentricCoordinates(float gdLatitudeDeg, float gdLongitudeDeg, float altitudeMeters) {
        float altitudeKm = altitudeMeters / 1000.0F;
        float a2 = android.hardware.GeomagneticField.EARTH_SEMI_MAJOR_AXIS_KM * android.hardware.GeomagneticField.EARTH_SEMI_MAJOR_AXIS_KM;
        float b2 = android.hardware.GeomagneticField.EARTH_SEMI_MINOR_AXIS_KM * android.hardware.GeomagneticField.EARTH_SEMI_MINOR_AXIS_KM;
        double gdLatRad = java.lang.Math.toRadians(gdLatitudeDeg);
        float clat = ((float) (java.lang.Math.cos(gdLatRad)));
        float slat = ((float) (java.lang.Math.sin(gdLatRad)));
        float tlat = slat / clat;
        float latRad = ((float) (java.lang.Math.sqrt(((a2 * clat) * clat) + ((b2 * slat) * slat))));
        mGcLatitudeRad = ((float) (java.lang.Math.atan((tlat * ((latRad * altitudeKm) + b2)) / ((latRad * altitudeKm) + a2))));
        mGcLongitudeRad = ((float) (java.lang.Math.toRadians(gdLongitudeDeg)));
        float radSq = ((altitudeKm * altitudeKm) + ((2 * altitudeKm) * ((float) (java.lang.Math.sqrt(((a2 * clat) * clat) + ((b2 * slat) * slat)))))) + (((((a2 * a2) * clat) * clat) + (((b2 * b2) * slat) * slat)) / (((a2 * clat) * clat) + ((b2 * slat) * slat)));
        mGcRadiusKm = ((float) (java.lang.Math.sqrt(radSq)));
    }

    /**
     * Utility class to compute a table of Gauss-normalized associated Legendre
     * functions P_n^m(cos(theta))
     */
    private static class LegendreTable {
        // These are the Gauss-normalized associated Legendre functions -- that
        // is, they are normal Legendre functions multiplied by
        // (n-m)!/(2n-1)!! (where (2n-1)!! = 1*3*5*...*2n-1)
        public final float[][] mP;

        // Derivative of mP, with respect to theta.
        public final float[][] mPDeriv;

        /**
         *
         *
         * @param maxN
         * 		The maximum n- and m-values to support
         * @param thetaRad
         * 		Returned functions will be Gauss-normalized
         * 		P_n^m(cos(thetaRad)), with thetaRad in radians.
         */
        public LegendreTable(int maxN, float thetaRad) {
            // Compute the table of Gauss-normalized associated Legendre
            // functions using standard recursion relations. Also compute the
            // table of derivatives using the derivative of the recursion
            // relations.
            float cos = ((float) (java.lang.Math.cos(thetaRad)));
            float sin = ((float) (java.lang.Math.sin(thetaRad)));
            mP = new float[maxN + 1][];
            mPDeriv = new float[maxN + 1][];
            mP[0] = new float[]{ 1.0F };
            mPDeriv[0] = new float[]{ 0.0F };
            for (int n = 1; n <= maxN; n++) {
                mP[n] = new float[n + 1];
                mPDeriv[n] = new float[n + 1];
                for (int m = 0; m <= n; m++) {
                    if (n == m) {
                        mP[n][m] = sin * mP[n - 1][m - 1];
                        mPDeriv[n][m] = (cos * mP[n - 1][m - 1]) + (sin * mPDeriv[n - 1][m - 1]);
                    } else
                        if ((n == 1) || (m == (n - 1))) {
                            mP[n][m] = cos * mP[n - 1][m];
                            mPDeriv[n][m] = ((-sin) * mP[n - 1][m]) + (cos * mPDeriv[n - 1][m]);
                        } else {
                            assert (n > 1) && (m < (n - 1));
                            float k = (((n - 1) * (n - 1)) - (m * m)) / ((float) (((2 * n) - 1) * ((2 * n) - 3)));
                            mP[n][m] = (cos * mP[n - 1][m]) - (k * mP[n - 2][m]);
                            mPDeriv[n][m] = (((-sin) * mP[n - 1][m]) + (cos * mPDeriv[n - 1][m])) - (k * mPDeriv[n - 2][m]);
                        }

                }
            }
        }
    }

    /**
     * Compute the ration between the Gauss-normalized associated Legendre
     * functions and the Schmidt quasi-normalized version. This is equivalent to
     * sqrt((m==0?1:2)*(n-m)!/(n+m!))*(2n-1)!!/(n-m)!
     */
    private static float[][] computeSchmidtQuasiNormFactors(int maxN) {
        float[][] schmidtQuasiNorm = new float[maxN + 1][];
        schmidtQuasiNorm[0] = new float[]{ 1.0F };
        for (int n = 1; n <= maxN; n++) {
            schmidtQuasiNorm[n] = new float[n + 1];
            schmidtQuasiNorm[n][0] = (schmidtQuasiNorm[n - 1][0] * ((2 * n) - 1)) / ((float) (n));
            for (int m = 1; m <= n; m++) {
                schmidtQuasiNorm[n][m] = schmidtQuasiNorm[n][m - 1] * ((float) (java.lang.Math.sqrt((((n - m) + 1) * (m == 1 ? 2 : 1)) / ((float) (n + m)))));
            }
        }
        return schmidtQuasiNorm;
    }
}

