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


public class BaseCluster {
    public static java.lang.String TAG = "BaseCluster";

    public double[] mCenter;

    // protected double[] mCenter;
    protected static final int VECTOR_LENGTH = 3;

    private static final long FORGETTING_ENUMERATOR = 95;

    private static final long FORGETTING_DENOMINATOR = 100;

    // Histogram illustrates the pattern of visit during time of day,
    protected java.util.HashMap<java.lang.String, java.lang.Long> mHistogram = new java.util.HashMap<java.lang.String, java.lang.Long>();

    protected long mDuration;

    protected java.lang.String mSemanticId;

    protected static final double EARTH_RADIUS = 6378100.0F;

    public BaseCluster(android.location.Location location) {
        mCenter = getLocationVector(location);
        mDuration = 0;
    }

    public BaseCluster(java.lang.String semanticId, double longitude, double latitude, long duration) {
        mSemanticId = semanticId;
        mCenter = getLocationVector(longitude, latitude);
        mDuration = duration;
    }

    public java.lang.String getSemanticId() {
        return mSemanticId;
    }

    public void generateSemanticId(long index) {
        mSemanticId = "cluser: " + java.lang.String.valueOf(index);
    }

    public long getDuration() {
        return mDuration;
    }

    public boolean hasSemanticId() {
        return mSemanticId != null;
    }

    protected double[] getLocationVector(android.location.Location location) {
        return getLocationVector(location.getLongitude(), location.getLatitude());
    }

    protected double[] getLocationVector(double longitude, double latitude) {
        double[] vector = new double[android.bordeaux.services.BaseCluster.VECTOR_LENGTH];
        double lambda = java.lang.Math.toRadians(longitude);
        double phi = java.lang.Math.toRadians(latitude);
        vector[0] = java.lang.Math.cos(lambda) * java.lang.Math.cos(phi);
        vector[1] = java.lang.Math.sin(lambda) * java.lang.Math.cos(phi);
        vector[2] = java.lang.Math.sin(phi);
        return vector;
    }

    protected double getCenterLongitude() {
        // Because latitude ranges from -90 to 90 degrees, cosPhi >= 0.
        double cosPhi = java.lang.Math.cos(java.lang.Math.asin(mCenter[2]));
        double longitude = java.lang.Math.toDegrees(java.lang.Math.asin(mCenter[1] / cosPhi));
        if (mCenter[0] < 0) {
            longitude = (longitude > 0) ? 180.0F - longitude : (-180) - longitude;
        }
        return longitude;
    }

    protected double getCenterLatitude() {
        return java.lang.Math.toDegrees(java.lang.Math.asin(mCenter[2]));
    }

    private double computeDistance(double[] vector1, double[] vector2) {
        double product = 0.0F;
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            product += vector1[i] * vector2[i];
        }
        double radian = java.lang.Math.acos(java.lang.Math.min(product, 1.0F));
        return radian * android.bordeaux.services.BaseCluster.EARTH_RADIUS;
    }

    /* This computes the distance from loation to the cluster center in meter. */
    public float distanceToCenter(android.location.Location location) {
        return ((float) (computeDistance(mCenter, getLocationVector(location))));
    }

    public float distanceToCluster(android.bordeaux.services.BaseCluster cluster) {
        return ((float) (computeDistance(mCenter, cluster.mCenter)));
    }

    public void absorbCluster(android.bordeaux.services.BaseCluster cluster) {
        averageCenter(cluster.mCenter, cluster.mDuration);
        absorbHistogram(cluster);
    }

    public void setCluster(android.bordeaux.services.BaseCluster cluster) {
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            mCenter[i] = cluster.mCenter[i];
        }
        mHistogram.clear();
        mHistogram.putAll(cluster.mHistogram);
        mDuration = cluster.mDuration;
    }

    private void absorbHistogram(android.bordeaux.services.BaseCluster cluster) {
        for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : cluster.mHistogram.entrySet()) {
            java.lang.String timeLabel = entry.getKey();
            long duration = entry.getValue();
            if (mHistogram.containsKey(timeLabel)) {
                duration += mHistogram.get(timeLabel);
            }
            mHistogram.put(timeLabel, duration);
        }
        mDuration += cluster.mDuration;
    }

    public boolean passThreshold(long durationThreshold) {
        // TODO: might want to keep semantic cluster
        return mDuration > durationThreshold;
    }

    public final java.util.HashMap<java.lang.String, java.lang.Long> getHistogram() {
        return mHistogram;
    }

    public void setHistogram(java.util.Map<java.lang.String, java.lang.Long> histogram) {
        mHistogram.clear();
        mHistogram.putAll(histogram);
        mDuration = 0;
        if (mHistogram.containsKey(android.bordeaux.services.TimeStatsAggregator.WEEKEND)) {
            mDuration += mHistogram.get(android.bordeaux.services.TimeStatsAggregator.WEEKEND);
        }
        if (mHistogram.containsKey(android.bordeaux.services.TimeStatsAggregator.WEEKDAY)) {
            mDuration += mHistogram.get(android.bordeaux.services.TimeStatsAggregator.WEEKDAY);
        }
    }

    public void forgetPastHistory() {
        mDuration *= android.bordeaux.services.BaseCluster.FORGETTING_ENUMERATOR;
        mDuration /= android.bordeaux.services.BaseCluster.FORGETTING_DENOMINATOR;
        for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : mHistogram.entrySet()) {
            java.lang.String key = entry.getKey();
            long value = entry.getValue();
            value *= android.bordeaux.services.BaseCluster.FORGETTING_ENUMERATOR;
            value /= android.bordeaux.services.BaseCluster.FORGETTING_DENOMINATOR;
            mHistogram.put(key, value);
        }
    }

    protected void normalizeCenter() {
        double norm = 0;
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            norm += mCenter[i] * mCenter[i];
        }
        norm = java.lang.Math.sqrt(norm);
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            mCenter[i] /= norm;
        }
    }

    protected void averageCenter(double[] newCenter, long newDuration) {
        double weight = ((double) (mDuration)) / (mDuration + newDuration);
        double newWeight = 1.0F - weight;
        double norm = 0;
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            mCenter[i] = (weight * mCenter[i]) + (newWeight * newCenter[i]);
            norm += mCenter[i] * mCenter[i];
        }
        norm = java.lang.Math.sqrt(norm);
        for (int i = 0; i < android.bordeaux.services.BaseCluster.VECTOR_LENGTH; ++i) {
            mCenter[i] /= norm;
        }
    }
}

