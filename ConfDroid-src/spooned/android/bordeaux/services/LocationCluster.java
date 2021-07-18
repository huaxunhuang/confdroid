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
package android.bordeaux.services;


public class LocationCluster extends android.bordeaux.services.BaseCluster {
    public static java.lang.String TAG = "LocationCluster";

    private java.util.ArrayList<android.location.Location> mLocations = new java.util.ArrayList<android.location.Location>();

    private java.util.HashMap<java.lang.String, java.lang.Long> mNewHistogram = new java.util.HashMap<java.lang.String, java.lang.Long>();

    private java.lang.String mSemanticClusterId = null;

    public void setSemanticClusterId(java.lang.String semanticClusterId) {
        mSemanticClusterId = semanticClusterId;
    }

    public java.lang.String getSemanticClusterId() {
        return mSemanticClusterId;
    }

    public boolean hasSemanticClusterId() {
        return mSemanticClusterId != null;
    }

    // TODO: make it a singleton class
    public LocationCluster(android.location.Location location, long duration) {
        super(location);
        addSample(location, duration);
    }

    public void addSample(android.location.Location location, long duration) {
        updateTemporalHistogram(location.getTime(), duration);
        // use time field to store duation of this location
        // TODO: extend Location class with additional field for this.
        location.setTime(duration);
        mLocations.add(location);
    }

    public void consolidate() {
        // If there is no new location added during this period, do nothing.
        if (mLocations.size() == 0) {
            return;
        }
        double[] newCenter = new double[]{ 0.0F, 0.0F, 0.0F };
        long newDuration = 0L;
        // update cluster center
        for (android.location.Location location : mLocations) {
            double[] vector = getLocationVector(location);
            long duration = location.getTime();// in seconds

            if (duration == 0) {
                throw new java.lang.RuntimeException("location duration is zero");
            }
            newDuration += duration;
            for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
                newCenter[i] += vector[i] * duration;
            }
        }
        if (newDuration == 0L) {
            throw new java.lang.RuntimeException("new duration is zero!");
        }
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            newCenter[i] /= newDuration;
        }
        // remove location data
        mLocations.clear();
        // The updated center is the weighted average of the existing and the new
        // centers. Note that if the cluster is consolidated for the first time,
        // the weight for the existing cluster would be zero.
        averageCenter(newCenter, newDuration);
        // update histogram
        for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : mNewHistogram.entrySet()) {
            java.lang.String timeLabel = entry.getKey();
            long duration = entry.getValue();
            if (mHistogram.containsKey(timeLabel)) {
                duration += mHistogram.get(timeLabel);
            }
            mHistogram.put(timeLabel, duration);
        }
        mDuration += newDuration;
        mNewHistogram.clear();
    }

    /* if the new created cluster whose covered area overlaps with any existing
    cluster move the center away from that cluster till there is no overlap.
     */
    public void moveAwayCluster(android.bordeaux.services.LocationCluster cluster, float distance) {
        double[] vector = new double[android.bordeaux.services.BaseCluster.VECTOR_LENGTH];
        double dot = 0.0F;
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            dot += mCenter[i] * cluster.mCenter[i];
        }
        double norm = 0.0F;
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            vector[i] = mCenter[i] - (dot * cluster.mCenter[i]);
            norm += vector[i] * vector[i];
        }
        norm = java.lang.Math.sqrt(norm);
        double radian = distance / android.bordeaux.services.BaseCluster.EARTH_RADIUS;
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            mCenter[i] = (cluster.mCenter[i] * java.lang.Math.cos(radian)) + ((vector[i] / norm) * java.lang.Math.sin(radian));
        }
    }

    private void updateTemporalHistogram(long time, long duration) {
        java.util.HashMap<java.lang.String, java.lang.String> timeFeatures = android.bordeaux.services.TimeStatsAggregator.getAllTimeFeatures(time);
        java.lang.String timeOfWeek = timeFeatures.get(android.bordeaux.services.TimeStatsAggregator.TIME_OF_WEEK);
        long totalDuration = (mNewHistogram.containsKey(timeOfWeek)) ? mNewHistogram.get(timeOfWeek) + duration : duration;
        mNewHistogram.put(timeOfWeek, totalDuration);
        java.lang.String timeOfDay = timeFeatures.get(android.bordeaux.services.TimeStatsAggregator.TIME_OF_DAY);
        totalDuration = (mNewHistogram.containsKey(timeOfDay)) ? mNewHistogram.get(timeOfDay) + duration : duration;
        mNewHistogram.put(timeOfDay, totalDuration);
    }
}

