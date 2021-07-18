/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v7.app;


/**
 * Imported from frameworks/base/services/core/java/com/android/server/TwilightCalculator.java
 *
 * <p>Calculates the sunrise and sunsets times for a given location.</p>
 */
class TwilightCalculator {
    private static android.support.v7.app.TwilightCalculator sInstance;

    static android.support.v7.app.TwilightCalculator getInstance() {
        if (android.support.v7.app.TwilightCalculator.sInstance == null) {
            android.support.v7.app.TwilightCalculator.sInstance = new android.support.v7.app.TwilightCalculator();
        }
        return android.support.v7.app.TwilightCalculator.sInstance;
    }

    /**
     * Value of {@link #state} if it is currently day
     */
    public static final int DAY = 0;

    /**
     * Value of {@link #state} if it is currently night
     */
    public static final int NIGHT = 1;

    private static final float DEGREES_TO_RADIANS = ((float) (java.lang.Math.PI / 180.0F));

    // element for calculating solar transit.
    private static final float J0 = 9.0E-4F;

    // correction for civil twilight
    private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976F;

    // coefficients for calculating Equation of Center.
    private static final float C1 = 0.0334196F;

    private static final float C2 = 3.49066E-4F;

    private static final float C3 = 5.236E-6F;

    private static final float OBLIQUITY = 0.4092797F;

    // Java time on Jan 1, 2000 12:00 UTC.
    private static final long UTC_2000 = 946728000000L;

    /**
     * Time of sunset (civil twilight) in milliseconds or -1 in the case the day
     * or night never ends.
     */
    public long sunset;

    /**
     * Time of sunrise (civil twilight) in milliseconds or -1 in the case the
     * day or night never ends.
     */
    public long sunrise;

    /**
     * Current state
     */
    public int state;

    /**
     * calculates the civil twilight bases on time and geo-coordinates.
     *
     * @param time
     * 		time in milliseconds.
     * @param latitude
     * 		latitude in degrees.
     * @param longitude
     * 		latitude in degrees.
     */
    public void calculateTwilight(long time, double latitude, double longitude) {
        final float daysSince2000 = ((float) (time - android.support.v7.app.TwilightCalculator.UTC_2000)) / android.text.format.DateUtils.DAY_IN_MILLIS;
        // mean anomaly
        final float meanAnomaly = 6.24006F + (daysSince2000 * 0.01720197F);
        // true anomaly
        final double trueAnomaly = ((meanAnomaly + (android.support.v7.app.TwilightCalculator.C1 * java.lang.Math.sin(meanAnomaly))) + (android.support.v7.app.TwilightCalculator.C2 * java.lang.Math.sin(2 * meanAnomaly))) + (android.support.v7.app.TwilightCalculator.C3 * java.lang.Math.sin(3 * meanAnomaly));
        // ecliptic longitude
        final double solarLng = (trueAnomaly + 1.796593063) + java.lang.Math.PI;
        // solar transit in days since 2000
        final double arcLongitude = (-longitude) / 360;
        float n = java.lang.Math.round((daysSince2000 - android.support.v7.app.TwilightCalculator.J0) - arcLongitude);
        double solarTransitJ2000 = (((n + android.support.v7.app.TwilightCalculator.J0) + arcLongitude) + (0.0053 * java.lang.Math.sin(meanAnomaly))) + ((-0.0069) * java.lang.Math.sin(2 * solarLng));
        // declination of sun
        double solarDec = java.lang.Math.asin(java.lang.Math.sin(solarLng) * java.lang.Math.sin(android.support.v7.app.TwilightCalculator.OBLIQUITY));
        final double latRad = latitude * android.support.v7.app.TwilightCalculator.DEGREES_TO_RADIANS;
        double cosHourAngle = (java.lang.Math.sin(android.support.v7.app.TwilightCalculator.ALTIDUTE_CORRECTION_CIVIL_TWILIGHT) - (java.lang.Math.sin(latRad) * java.lang.Math.sin(solarDec))) / (java.lang.Math.cos(latRad) * java.lang.Math.cos(solarDec));
        // The day or night never ends for the given date and location, if this value is out of
        // range.
        if (cosHourAngle >= 1) {
            state = android.support.v7.app.TwilightCalculator.NIGHT;
            sunset = -1;
            sunrise = -1;
            return;
        } else
            if (cosHourAngle <= (-1)) {
                state = android.support.v7.app.TwilightCalculator.DAY;
                sunset = -1;
                sunrise = -1;
                return;
            }

        float hourAngle = ((float) (java.lang.Math.acos(cosHourAngle) / (2 * java.lang.Math.PI)));
        sunset = java.lang.Math.round((solarTransitJ2000 + hourAngle) * android.text.format.DateUtils.DAY_IN_MILLIS) + android.support.v7.app.TwilightCalculator.UTC_2000;
        sunrise = java.lang.Math.round((solarTransitJ2000 - hourAngle) * android.text.format.DateUtils.DAY_IN_MILLIS) + android.support.v7.app.TwilightCalculator.UTC_2000;
        if ((sunrise < time) && (sunset > time)) {
            state = android.support.v7.app.TwilightCalculator.DAY;
        } else {
            state = android.support.v7.app.TwilightCalculator.NIGHT;
        }
    }
}

