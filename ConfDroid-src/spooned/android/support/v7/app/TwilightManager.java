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
 * Class which managing whether we are in the night or not.
 */
class TwilightManager {
    private static final java.lang.String TAG = "TwilightManager";

    private static final int SUNRISE = 6;// 6am


    private static final int SUNSET = 22;// 10pm


    private static android.support.v7.app.TwilightManager sInstance;

    static android.support.v7.app.TwilightManager getInstance(@android.support.annotation.NonNull
    android.content.Context context) {
        if (android.support.v7.app.TwilightManager.sInstance == null) {
            context = context.getApplicationContext();
            android.support.v7.app.TwilightManager.sInstance = new android.support.v7.app.TwilightManager(context, ((android.location.LocationManager) (context.getSystemService(android.content.Context.LOCATION_SERVICE))));
        }
        return android.support.v7.app.TwilightManager.sInstance;
    }

    @android.support.annotation.VisibleForTesting
    static void setInstance(android.support.v7.app.TwilightManager twilightManager) {
        android.support.v7.app.TwilightManager.sInstance = twilightManager;
    }

    private final android.content.Context mContext;

    private final android.location.LocationManager mLocationManager;

    private final android.support.v7.app.TwilightManager.TwilightState mTwilightState = new android.support.v7.app.TwilightManager.TwilightState();

    @android.support.annotation.VisibleForTesting
    TwilightManager(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.location.LocationManager locationManager) {
        mContext = context;
        mLocationManager = locationManager;
    }

    /**
     * Returns true we are currently in the 'night'.
     *
     * @return true if we are at night, false if the day.
     */
    boolean isNight() {
        final android.support.v7.app.TwilightManager.TwilightState state = mTwilightState;
        if (isStateValid()) {
            // If the current twilight state is still valid, use it
            return state.isNight;
        }
        // Else, we will try and grab the last known location
        final android.location.Location location = getLastKnownLocation();
        if (location != null) {
            updateState(location);
            return state.isNight;
        }
        android.util.Log.i(android.support.v7.app.TwilightManager.TAG, "Could not get last known location. This is probably because the app does not" + (" have any location permissions. Falling back to hardcoded" + " sunrise/sunset values."));
        // If we don't have a location, we'll use our hardcoded sunrise/sunset values.
        // These aren't great, but it's better than nothing.
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        final int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        return (hour < android.support.v7.app.TwilightManager.SUNRISE) || (hour >= android.support.v7.app.TwilightManager.SUNSET);
    }

    private android.location.Location getLastKnownLocation() {
        android.location.Location coarseLoc = null;
        android.location.Location fineLoc = null;
        int permission = android.support.v4.content.PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission == android.support.v4.content.PermissionChecker.PERMISSION_GRANTED) {
            coarseLoc = getLastKnownLocationForProvider(android.location.LocationManager.NETWORK_PROVIDER);
        }
        permission = android.support.v4.content.PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == android.support.v4.content.PermissionChecker.PERMISSION_GRANTED) {
            fineLoc = getLastKnownLocationForProvider(android.location.LocationManager.GPS_PROVIDER);
        }
        if ((fineLoc != null) && (coarseLoc != null)) {
            // If we have both a fine and coarse location, use the latest
            return fineLoc.getTime() > coarseLoc.getTime() ? fineLoc : coarseLoc;
        } else {
            // Else, return the non-null one (if there is one)
            return fineLoc != null ? fineLoc : coarseLoc;
        }
    }

    private android.location.Location getLastKnownLocationForProvider(java.lang.String provider) {
        if (mLocationManager != null) {
            try {
                if (mLocationManager.isProviderEnabled(provider)) {
                    return mLocationManager.getLastKnownLocation(provider);
                }
            } catch (java.lang.Exception e) {
                android.util.Log.d(android.support.v7.app.TwilightManager.TAG, "Failed to get last known location", e);
            }
        }
        return null;
    }

    private boolean isStateValid() {
        return (mTwilightState != null) && (mTwilightState.nextUpdate > java.lang.System.currentTimeMillis());
    }

    private void updateState(@android.support.annotation.NonNull
    android.location.Location location) {
        final android.support.v7.app.TwilightManager.TwilightState state = mTwilightState;
        final long now = java.lang.System.currentTimeMillis();
        final android.support.v7.app.TwilightCalculator calculator = android.support.v7.app.TwilightCalculator.getInstance();
        // calculate yesterday's twilight
        calculator.calculateTwilight(now - android.text.format.DateUtils.DAY_IN_MILLIS, location.getLatitude(), location.getLongitude());
        final long yesterdaySunset = calculator.sunset;
        // calculate today's twilight
        calculator.calculateTwilight(now, location.getLatitude(), location.getLongitude());
        final boolean isNight = calculator.state == android.support.v7.app.TwilightCalculator.NIGHT;
        final long todaySunrise = calculator.sunrise;
        final long todaySunset = calculator.sunset;
        // calculate tomorrow's twilight
        calculator.calculateTwilight(now + android.text.format.DateUtils.DAY_IN_MILLIS, location.getLatitude(), location.getLongitude());
        final long tomorrowSunrise = calculator.sunrise;
        // Set next update
        long nextUpdate = 0;
        if ((todaySunrise == (-1)) || (todaySunset == (-1))) {
            // In the case the day or night never ends the update is scheduled 12 hours later.
            nextUpdate = now + (12 * android.text.format.DateUtils.HOUR_IN_MILLIS);
        } else {
            if (now > todaySunset) {
                nextUpdate += tomorrowSunrise;
            } else
                if (now > todaySunrise) {
                    nextUpdate += todaySunset;
                } else {
                    nextUpdate += todaySunrise;
                }

            // add some extra time to be on the safe side.
            nextUpdate += android.text.format.DateUtils.MINUTE_IN_MILLIS;
        }
        // Update the twilight state
        state.isNight = isNight;
        state.yesterdaySunset = yesterdaySunset;
        state.todaySunrise = todaySunrise;
        state.todaySunset = todaySunset;
        state.tomorrowSunrise = tomorrowSunrise;
        state.nextUpdate = nextUpdate;
    }

    /**
     * Describes whether it is day or night.
     */
    private static class TwilightState {
        boolean isNight;

        long yesterdaySunset;

        long todaySunrise;

        long todaySunset;

        long tomorrowSunrise;

        long nextUpdate;

        TwilightState() {
        }
    }
}

