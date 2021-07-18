/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * This class represents the current state of the GPS engine.
 *
 * <p>This class is used in conjunction with the {@link Listener} interface.
 *
 * @deprecated use {@link GnssStatus} and {@link GnssStatus.Callback}.
 */
@java.lang.Deprecated
public final class GpsStatus {
    private static final int NUM_SATELLITES = 255;

    private static final int GLONASS_SVID_OFFSET = 64;

    private static final int BEIDOU_SVID_OFFSET = 200;

    private static final int SBAS_SVID_OFFSET = -87;

    /* These package private values are modified by the LocationManager class */
    private int mTimeToFirstFix;

    private final android.util.SparseArray<android.location.GpsSatellite> mSatellites = new android.util.SparseArray<>();

    private final class SatelliteIterator implements java.util.Iterator<android.location.GpsSatellite> {
        private final int mSatellitesCount;

        private int mIndex = 0;

        SatelliteIterator() {
            mSatellitesCount = mSatellites.size();
        }

        @java.lang.Override
        public boolean hasNext() {
            for (; mIndex < mSatellitesCount; ++mIndex) {
                android.location.GpsSatellite satellite = mSatellites.valueAt(mIndex);
                if (satellite.mValid) {
                    return true;
                }
            }
            return false;
        }

        @java.lang.Override
        public android.location.GpsSatellite next() {
            while (mIndex < mSatellitesCount) {
                android.location.GpsSatellite satellite = mSatellites.valueAt(mIndex);
                ++mIndex;
                if (satellite.mValid) {
                    return satellite;
                }
            } 
            throw new java.util.NoSuchElementException();
        }

        @java.lang.Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    private java.lang.Iterable<android.location.GpsSatellite> mSatelliteList = new java.lang.Iterable<android.location.GpsSatellite>() {
        @java.lang.Override
        public java.util.Iterator<android.location.GpsSatellite> iterator() {
            return new android.location.GpsStatus.SatelliteIterator();
        }
    };

    /**
     * Event sent when the GPS system has started.
     */
    public static final int GPS_EVENT_STARTED = 1;

    /**
     * Event sent when the GPS system has stopped.
     */
    public static final int GPS_EVENT_STOPPED = 2;

    /**
     * Event sent when the GPS system has received its first fix since starting.
     * Call {@link #getTimeToFirstFix()} to find the time from start to first fix.
     */
    public static final int GPS_EVENT_FIRST_FIX = 3;

    /**
     * Event sent periodically to report GPS satellite status.
     * Call {@link #getSatellites()} to retrieve the status for each satellite.
     */
    public static final int GPS_EVENT_SATELLITE_STATUS = 4;

    /**
     * Used for receiving notifications when GPS status has changed.
     *
     * @deprecated use {@link GnssStatus.Callback} instead.
     */
    @java.lang.Deprecated
    public interface Listener {
        /**
         * Called to report changes in the GPS status.
         * The event number is one of:
         * <ul>
         * <li> {@link GpsStatus#GPS_EVENT_STARTED}
         * <li> {@link GpsStatus#GPS_EVENT_STOPPED}
         * <li> {@link GpsStatus#GPS_EVENT_FIRST_FIX}
         * <li> {@link GpsStatus#GPS_EVENT_SATELLITE_STATUS}
         * </ul>
         *
         * When this method is called, the client should call
         * {@link LocationManager#getGpsStatus} to get additional
         * status information.
         *
         * @param event
         * 		event number for this notification
         */
        void onGpsStatusChanged(int event);
    }

    /**
     * Used for receiving NMEA sentences from the GPS.
     * NMEA 0183 is a standard for communicating with marine electronic devices
     * and is a common method for receiving data from a GPS, typically over a serial port.
     * See <a href="http://en.wikipedia.org/wiki/NMEA_0183">NMEA 0183</a> for more details.
     * You can implement this interface and call {@link LocationManager#addNmeaListener}
     * to receive NMEA data from the GPS engine.
     *
     * @deprecated use {@link OnNmeaMessageListener} instead.
     */
    @java.lang.Deprecated
    public interface NmeaListener {
        void onNmeaReceived(long timestamp, java.lang.String nmea);
    }

    // For API-compat a public ctor() is not available
    GpsStatus() {
    }

    private void setStatus(int svCount, int[] svidWithFlags, float[] cn0s, float[] elevations, float[] azimuths) {
        clearSatellites();
        for (int i = 0; i < svCount; i++) {
            final int constellationType = (svidWithFlags[i] >> android.location.GnssStatus.CONSTELLATION_TYPE_SHIFT_WIDTH) & android.location.GnssStatus.CONSTELLATION_TYPE_MASK;
            int prn = svidWithFlags[i] >> android.location.GnssStatus.SVID_SHIFT_WIDTH;
            // Other satellites passed through these APIs before GnssSvStatus was availble.
            // GPS, SBAS & QZSS can pass through at their nominally
            // assigned prn number (as long as it fits in the valid 0-255 range below.)
            // Glonass, and Beidou are passed through with the defacto standard offsets
            // Other future constellation reporting (e.g. Galileo) needs to use
            // GnssSvStatus on (N level) HAL & Java layers.
            if (constellationType == android.location.GnssStatus.CONSTELLATION_GLONASS) {
                prn += android.location.GpsStatus.GLONASS_SVID_OFFSET;
            } else
                if (constellationType == android.location.GnssStatus.CONSTELLATION_BEIDOU) {
                    prn += android.location.GpsStatus.BEIDOU_SVID_OFFSET;
                } else
                    if (constellationType == android.location.GnssStatus.CONSTELLATION_SBAS) {
                        prn += android.location.GpsStatus.SBAS_SVID_OFFSET;
                    } else
                        if ((constellationType != android.location.GnssStatus.CONSTELLATION_GPS) && (constellationType != android.location.GnssStatus.CONSTELLATION_QZSS)) {
                            continue;
                        }



            if ((prn > 0) && (prn <= android.location.GpsStatus.NUM_SATELLITES)) {
                android.location.GpsSatellite satellite = mSatellites.get(prn);
                if (satellite == null) {
                    satellite = new android.location.GpsSatellite(prn);
                    mSatellites.put(prn, satellite);
                }
                satellite.mValid = true;
                satellite.mSnr = cn0s[i];
                satellite.mElevation = elevations[i];
                satellite.mAzimuth = azimuths[i];
                satellite.mHasEphemeris = (svidWithFlags[i] & android.location.GnssStatus.GNSS_SV_FLAGS_HAS_EPHEMERIS_DATA) != 0;
                satellite.mHasAlmanac = (svidWithFlags[i] & android.location.GnssStatus.GNSS_SV_FLAGS_HAS_ALMANAC_DATA) != 0;
                satellite.mUsedInFix = (svidWithFlags[i] & android.location.GnssStatus.GNSS_SV_FLAGS_USED_IN_FIX) != 0;
            }
        }
    }

    /**
     * Copies GPS satellites information from GnssStatus object.
     * Since this method is only used within {@link LocationManager#getGpsStatus},
     * it does not need to be synchronized.
     *
     * @unknown 
     */
    void setStatus(android.location.GnssStatus status, int timeToFirstFix) {
        mTimeToFirstFix = timeToFirstFix;
        setStatus(status.mSvCount, status.mSvidWithFlags, status.mCn0DbHz, status.mElevations, status.mAzimuths);
    }

    void setTimeToFirstFix(int ttff) {
        mTimeToFirstFix = ttff;
    }

    /**
     * Returns the time required to receive the first fix since the most recent
     * restart of the GPS engine.
     *
     * @return time to first fix in milliseconds
     */
    public int getTimeToFirstFix() {
        return mTimeToFirstFix;
    }

    /**
     * Returns an array of {@link GpsSatellite} objects, which represent the
     * current state of the GPS engine.
     *
     * @return the list of satellites
     */
    public java.lang.Iterable<android.location.GpsSatellite> getSatellites() {
        return mSatelliteList;
    }

    /**
     * Returns the maximum number of satellites that can be in the satellite
     * list that can be returned by {@link #getSatellites()}.
     *
     * @return the maximum number of satellites
     */
    public int getMaxSatellites() {
        return android.location.GpsStatus.NUM_SATELLITES;
    }

    private void clearSatellites() {
        int satellitesCount = mSatellites.size();
        for (int i = 0; i < satellitesCount; i++) {
            android.location.GpsSatellite satellite = mSatellites.valueAt(i);
            satellite.mValid = false;
        }
    }
}

