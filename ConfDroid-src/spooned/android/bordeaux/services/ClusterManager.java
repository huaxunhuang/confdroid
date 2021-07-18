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


/**
 * ClusterManager incrementally indentify representitve clusters from the input location
 * stream. Clusters are updated online using leader based clustering algorithm. The input
 * locations initially are kept by the clusters. Periodially, a cluster consolidating
 * procedure is carried out to refine the cluster centers. After consolidation, the
 * location data are released.
 */
public class ClusterManager {
    private static java.lang.String TAG = "ClusterManager";

    private static float LOCATION_CLUSTER_RADIUS = 25;// meter


    private static float SEMANTIC_CLUSTER_RADIUS = 75;// meter


    // Consoliate location clusters (and check for new semantic clusters)
    // every 10 minutes (600 seconds).
    private static final long CONSOLIDATE_INTERVAL = 600;

    // A location cluster can be labeled as a semantic cluster if it has been
    // stayed for at least 10 minutes (600 seconds) within a day.
    private static final long SEMANTIC_CLUSTER_THRESHOLD = 600;// seconds


    // Reset location cluters every 24 hours (86400 seconds).
    private static final long LOCATION_REFRESH_PERIOD = 86400;// seconds


    private static java.lang.String UNKNOWN_LOCATION = "Unknown Location";

    private android.location.Location mLastLocation = null;

    private long mClusterDuration;

    private long mConsolidateRef = 0;

    private long mRefreshRef = 0;

    private long mSemanticClusterCount = 0;

    private java.util.ArrayList<android.bordeaux.services.LocationCluster> mLocationClusters = new java.util.ArrayList<android.bordeaux.services.LocationCluster>();

    private java.util.ArrayList<android.bordeaux.services.BaseCluster> mSemanticClusters = new java.util.ArrayList<android.bordeaux.services.BaseCluster>();

    private android.bordeaux.services.AggregatorRecordStorage mStorage;

    private static java.lang.String SEMANTIC_TABLE = "SemanticTable";

    private static java.lang.String SEMANTIC_ID = "ID";

    private static final java.lang.String SEMANTIC_LONGITUDE = "Longitude";

    private static final java.lang.String SEMANTIC_LATITUDE = "Latitude";

    private static final java.lang.String SEMANTIC_DURATION = "Duration";

    private static final java.lang.String[] SEMANTIC_COLUMNS = new java.lang.String[]{ android.bordeaux.services.ClusterManager.SEMANTIC_ID, android.bordeaux.services.ClusterManager.SEMANTIC_LONGITUDE, android.bordeaux.services.ClusterManager.SEMANTIC_LATITUDE, android.bordeaux.services.ClusterManager.SEMANTIC_DURATION, android.bordeaux.services.TimeStatsAggregator.WEEKEND, android.bordeaux.services.TimeStatsAggregator.WEEKDAY, android.bordeaux.services.TimeStatsAggregator.MORNING, android.bordeaux.services.TimeStatsAggregator.NOON, android.bordeaux.services.TimeStatsAggregator.AFTERNOON, android.bordeaux.services.TimeStatsAggregator.EVENING, android.bordeaux.services.TimeStatsAggregator.NIGHT, android.bordeaux.services.TimeStatsAggregator.LATENIGHT };

    private static final int mFeatureValueStart = 4;

    private static final int mFeatureValueEnd = 11;

    public ClusterManager(android.content.Context context) {
        mStorage = new android.bordeaux.services.AggregatorRecordStorage(context, android.bordeaux.services.ClusterManager.SEMANTIC_TABLE, android.bordeaux.services.ClusterManager.SEMANTIC_COLUMNS);
        loadSemanticClusters();
    }

    public void addSample(android.location.Location location) {
        float bestClusterDistance = java.lang.Float.MAX_VALUE;
        int bestClusterIndex = -1;
        long lastDuration;
        long currentTime = location.getTime() / 1000;// measure time in seconds

        if (mLastLocation != null) {
            if (location.getTime() == mLastLocation.getTime()) {
                return;
            }
            // get the duration spent in the last location
            long duration = (location.getTime() - mLastLocation.getTime()) / 1000;
            mClusterDuration += duration;
            android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, (("sample duration: " + duration) + ", number of clusters: ") + mLocationClusters.size());
            synchronized(mLocationClusters) {
                // add the last location to cluster.
                // first find the cluster it belongs to.
                for (int i = 0; i < mLocationClusters.size(); ++i) {
                    float distance = mLocationClusters.get(i).distanceToCenter(mLastLocation);
                    android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, ((("clulster " + i) + " is within ") + distance) + " meters");
                    if (distance < bestClusterDistance) {
                        bestClusterDistance = distance;
                        bestClusterIndex = i;
                    }
                }
                // add the location to the selected cluster
                if (bestClusterDistance < android.bordeaux.services.ClusterManager.LOCATION_CLUSTER_RADIUS) {
                    mLocationClusters.get(bestClusterIndex).addSample(mLastLocation, duration);
                } else {
                    // if it is far away from all existing clusters, create a new cluster.
                    android.bordeaux.services.LocationCluster cluster = new android.bordeaux.services.LocationCluster(mLastLocation, duration);
                    mLocationClusters.add(cluster);
                }
            }
        } else {
            mConsolidateRef = currentTime;
            mRefreshRef = currentTime;
            if (mLocationClusters.isEmpty()) {
                mClusterDuration = 0;
            }
        }
        long collectDuration = currentTime - mConsolidateRef;
        android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, "collect duration: " + collectDuration);
        if (collectDuration > android.bordeaux.services.ClusterManager.CONSOLIDATE_INTERVAL) {
            // TODO : conslidation takes time. move this to a separate thread later.
            consolidateClusters();
            mConsolidateRef = currentTime;
            long refreshDuration = currentTime - mRefreshRef;
            android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, "refresh duration: " + refreshDuration);
            if (refreshDuration > android.bordeaux.services.ClusterManager.LOCATION_REFRESH_PERIOD) {
                updateSemanticClusters();
                mRefreshRef = currentTime;
            }
            saveSemanticClusters();
        }
        mLastLocation = location;
    }

    private void consolidateClusters() {
        synchronized(mSemanticClusters) {
            android.bordeaux.services.LocationCluster locationCluster;
            for (int i = mLocationClusters.size() - 1; i >= 0; --i) {
                locationCluster = mLocationClusters.get(i);
                locationCluster.consolidate();
            }
            // merge clusters whose regions are overlapped. note that after merge
            // cluster center changes but cluster size remains unchanged.
            for (int i = mLocationClusters.size() - 1; i >= 0; --i) {
                locationCluster = mLocationClusters.get(i);
                for (int j = i - 1; j >= 0; --j) {
                    float distance = mLocationClusters.get(j).distanceToCluster(locationCluster);
                    if (distance < android.bordeaux.services.ClusterManager.LOCATION_CLUSTER_RADIUS) {
                        mLocationClusters.get(j).absorbCluster(locationCluster);
                        mLocationClusters.remove(locationCluster);
                    }
                }
            }
            android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, mLocationClusters.size() + " location clusters after consolidate");
            // assign each candidate to a semantic cluster and check if new semantic
            // clusters are found
            for (android.bordeaux.services.LocationCluster candidate : mLocationClusters) {
                if ((candidate.hasSemanticId() || candidate.hasSemanticClusterId()) || (!candidate.passThreshold(android.bordeaux.services.ClusterManager.SEMANTIC_CLUSTER_THRESHOLD))) {
                    continue;
                }
                // find the closest semantic cluster
                float bestClusterDistance = java.lang.Float.MAX_VALUE;
                java.lang.String bestClusterId = "Unused Id";
                for (android.bordeaux.services.BaseCluster cluster : mSemanticClusters) {
                    float distance = cluster.distanceToCluster(candidate);
                    android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, (distance + "distance to semantic cluster: ") + cluster.getSemanticId());
                    if (distance < bestClusterDistance) {
                        bestClusterDistance = distance;
                        bestClusterId = cluster.getSemanticId();
                    }
                }
                // if candidate doesn't belong to any semantic cluster, create a new
                // semantic cluster
                if (bestClusterDistance > android.bordeaux.services.ClusterManager.SEMANTIC_CLUSTER_RADIUS) {
                    candidate.generateSemanticId(mSemanticClusterCount++);
                    mSemanticClusters.add(candidate);
                } else {
                    candidate.setSemanticClusterId(bestClusterId);
                }
            }
            android.util.Log.v(android.bordeaux.services.ClusterManager.TAG, mSemanticClusters.size() + " semantic clusters after consolidate");
        }
    }

    private void updateSemanticClusters() {
        synchronized(mSemanticClusters) {
            // create index to cluster map
            java.util.HashMap<java.lang.String, android.bordeaux.services.BaseCluster> semanticIdMap = new java.util.HashMap<java.lang.String, android.bordeaux.services.BaseCluster>();
            for (android.bordeaux.services.BaseCluster cluster : mSemanticClusters) {
                // TODO: apply forgetting factor on existing semantic cluster stats,
                // duration, histogram, etc.
                cluster.forgetPastHistory();
                semanticIdMap.put(cluster.getSemanticId(), cluster);
            }
            // assign each candidate to a semantic cluster
            for (android.bordeaux.services.LocationCluster cluster : mLocationClusters) {
                if (cluster.hasSemanticClusterId()) {
                    android.bordeaux.services.BaseCluster semanticCluster = semanticIdMap.get(cluster.getSemanticClusterId());
                    semanticCluster.absorbCluster(cluster);
                }
            }
            // reset location clusters.
            mLocationClusters.clear();
        }
    }

    private void loadSemanticClusters() {
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> allData = mStorage.getAllData();
        java.util.HashMap<java.lang.String, java.lang.Long> histogram = new java.util.HashMap<java.lang.String, java.lang.Long>();
        synchronized(mSemanticClusters) {
            mSemanticClusters.clear();
            for (java.util.Map<java.lang.String, java.lang.String> map : allData) {
                java.lang.String semanticId = map.get(android.bordeaux.services.ClusterManager.SEMANTIC_ID);
                double longitude = java.lang.Double.valueOf(map.get(android.bordeaux.services.ClusterManager.SEMANTIC_LONGITUDE));
                double latitude = java.lang.Double.valueOf(map.get(android.bordeaux.services.ClusterManager.SEMANTIC_LATITUDE));
                long duration = java.lang.Long.valueOf(map.get(android.bordeaux.services.ClusterManager.SEMANTIC_DURATION));
                android.bordeaux.services.BaseCluster cluster = new android.bordeaux.services.BaseCluster(semanticId, longitude, latitude, duration);
                histogram.clear();
                for (int i = android.bordeaux.services.ClusterManager.mFeatureValueStart; i <= android.bordeaux.services.ClusterManager.mFeatureValueEnd; i++) {
                    java.lang.String featureValue = android.bordeaux.services.ClusterManager.SEMANTIC_COLUMNS[i];
                    if (map.containsKey(featureValue)) {
                        histogram.put(featureValue, java.lang.Long.valueOf(map.get(featureValue)));
                    }
                }
                cluster.setHistogram(histogram);
                mSemanticClusters.add(cluster);
            }
            mSemanticClusterCount = mSemanticClusters.size();
            android.util.Log.e(android.bordeaux.services.ClusterManager.TAG, ("load " + mSemanticClusterCount) + " semantic clusters.");
        }
    }

    private void saveSemanticClusters() {
        java.util.HashMap<java.lang.String, java.lang.String> rowFeatures = new java.util.HashMap<java.lang.String, java.lang.String>();
        mStorage.removeAllData();
        synchronized(mSemanticClusters) {
            for (android.bordeaux.services.BaseCluster cluster : mSemanticClusters) {
                rowFeatures.clear();
                rowFeatures.put(android.bordeaux.services.ClusterManager.SEMANTIC_ID, cluster.getSemanticId());
                rowFeatures.put(android.bordeaux.services.ClusterManager.SEMANTIC_LONGITUDE, java.lang.String.valueOf(cluster.getCenterLongitude()));
                rowFeatures.put(android.bordeaux.services.ClusterManager.SEMANTIC_LATITUDE, java.lang.String.valueOf(cluster.getCenterLatitude()));
                rowFeatures.put(android.bordeaux.services.ClusterManager.SEMANTIC_DURATION, java.lang.String.valueOf(cluster.getDuration()));
                java.util.HashMap<java.lang.String, java.lang.Long> histogram = cluster.getHistogram();
                for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : histogram.entrySet()) {
                    rowFeatures.put(entry.getKey(), java.lang.String.valueOf(entry.getValue()));
                }
                mStorage.addData(rowFeatures);
                android.util.Log.e(android.bordeaux.services.ClusterManager.TAG, "saving semantic cluster: " + rowFeatures);
            }
        }
    }

    public java.lang.String getSemanticLocation() {
        java.lang.String label = android.bordeaux.services.LocationStatsAggregator.UNKNOWN_LOCATION;
        // instead of using the last location, try acquiring the latest location.
        if (mLastLocation != null) {
            // TODO: use fast neatest neighbor search speed up location search
            synchronized(mSemanticClusters) {
                for (android.bordeaux.services.BaseCluster cluster : mSemanticClusters) {
                    if (cluster.distanceToCenter(mLastLocation) < android.bordeaux.services.ClusterManager.SEMANTIC_CLUSTER_RADIUS) {
                        return cluster.getSemanticId();
                    }
                }
            }
        }
        return label;
    }

    public java.util.List<java.lang.String> getClusterNames() {
        java.util.ArrayList<java.lang.String> clusters = new java.util.ArrayList<java.lang.String>();
        synchronized(mSemanticClusters) {
            for (android.bordeaux.services.BaseCluster cluster : mSemanticClusters) {
                clusters.add(cluster.getSemanticId());
            }
        }
        return clusters;
    }
}

