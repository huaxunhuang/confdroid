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


class AggregatorManager extends android.bordeaux.services.IAggregatorManager.Stub {
    private final java.lang.String TAG = "AggregatorMnager";

    // this maps from the aggregator name to the registered aggregator instance
    private static java.util.HashMap<java.lang.String, android.bordeaux.services.Aggregator> mAggregators = new java.util.HashMap<java.lang.String, android.bordeaux.services.Aggregator>();

    // this maps from the feature names to the aggregator that generates the feature
    private static java.util.HashMap<java.lang.String, android.bordeaux.services.Aggregator> sFeatureMap = new java.util.HashMap<java.lang.String, android.bordeaux.services.Aggregator>();

    private static android.bordeaux.services.AggregatorManager mManager = null;

    private java.lang.String mFakeLocation = null;

    private java.lang.String mFakeTimeOfDay = null;

    private java.lang.String mFakeDayOfWeek = null;

    private AggregatorManager() {
        android.bordeaux.services.AggregatorManager.sFeatureMap = new java.util.HashMap<java.lang.String, android.bordeaux.services.Aggregator>();
    }

    public static android.bordeaux.services.AggregatorManager getInstance() {
        if (android.bordeaux.services.AggregatorManager.mManager == null)
            android.bordeaux.services.AggregatorManager.mManager = new android.bordeaux.services.AggregatorManager();

        return android.bordeaux.services.AggregatorManager.mManager;
    }

    public java.lang.String[] getListOfFeatures() {
        java.lang.String[] s = new java.lang.String[android.bordeaux.services.AggregatorManager.sFeatureMap.size()];
        int i = 0;
        for (java.util.Map.Entry<java.lang.String, android.bordeaux.services.Aggregator> x : android.bordeaux.services.AggregatorManager.sFeatureMap.entrySet()) {
            s[i] = x.getKey();
            i++;
        }
        return s;
    }

    public void registerAggregator(android.bordeaux.services.Aggregator agg, android.bordeaux.services.AggregatorManager m) {
        if (android.bordeaux.services.AggregatorManager.mAggregators.get(agg.getClass().getName()) != null) {
            // only one instance
            // throw new RuntimeException("Can't register more than one instance");
        }
        android.bordeaux.services.AggregatorManager.mAggregators.put(agg.getClass().getName(), agg);
        agg.setManager(m);
        java.lang.String[] fl = agg.getListOfFeatures();
        for (int i = 0; i < fl.length; i++)
            android.bordeaux.services.AggregatorManager.sFeatureMap.put(fl[i], agg);

    }

    // Start of IAggregatorManager interface
    public java.util.ArrayList<android.bordeaux.services.StringString> getData(java.lang.String dataName) {
        return getList(getDataMap(dataName));
    }

    public java.util.List<java.lang.String> getLocationClusters() {
        android.bordeaux.services.LocationStatsAggregator agg = ((android.bordeaux.services.LocationStatsAggregator) (android.bordeaux.services.AggregatorManager.mAggregators.get(android.bordeaux.services.LocationStatsAggregator.class.getName())));
        if (agg == null)
            return new java.util.ArrayList<java.lang.String>();

        return agg.getClusterNames();
    }

    public java.util.List<java.lang.String> getTimeOfDayValues() {
        android.bordeaux.services.TimeStatsAggregator agg = ((android.bordeaux.services.TimeStatsAggregator) (android.bordeaux.services.AggregatorManager.mAggregators.get(android.bordeaux.services.TimeStatsAggregator.class.getName())));
        if (agg == null)
            return new java.util.ArrayList<java.lang.String>();

        return agg.getTimeOfDayValues();
    }

    public java.util.List<java.lang.String> getDayOfWeekValues() {
        android.bordeaux.services.TimeStatsAggregator agg = ((android.bordeaux.services.TimeStatsAggregator) (android.bordeaux.services.AggregatorManager.mAggregators.get(android.bordeaux.services.TimeStatsAggregator.class.getName())));
        if (agg == null)
            return new java.util.ArrayList<java.lang.String>();

        return agg.getDayOfWeekValues();
    }

    // Set an empty string "" to disable the fake location
    public boolean setFakeLocation(java.lang.String location) {
        android.bordeaux.services.LocationStatsAggregator agg = ((android.bordeaux.services.LocationStatsAggregator) (android.bordeaux.services.AggregatorManager.mAggregators.get(android.bordeaux.services.LocationStatsAggregator.class.getName())));
        if (agg == null)
            return false;

        agg.setFakeLocation(location);
        mFakeLocation = location;
        return true;
    }

    // Set an empty string "" to disable the fake time of day
    public boolean setFakeTimeOfDay(java.lang.String time_of_day) {
        android.bordeaux.services.TimeStatsAggregator agg = ((android.bordeaux.services.TimeStatsAggregator) (android.bordeaux.services.AggregatorManager.mAggregators.get(android.bordeaux.services.TimeStatsAggregator.class.getName())));
        if (agg == null)
            return false;

        agg.setFakeTimeOfDay(time_of_day);
        mFakeTimeOfDay = time_of_day;
        return true;
    }

    // Set an empty string "" to disable the fake day of week
    public boolean setFakeDayOfWeek(java.lang.String day_of_week) {
        android.bordeaux.services.TimeStatsAggregator agg = ((android.bordeaux.services.TimeStatsAggregator) (android.bordeaux.services.AggregatorManager.mAggregators.get(android.bordeaux.services.TimeStatsAggregator.class.getName())));
        if (agg == null)
            return false;

        agg.setFakeDayOfWeek(day_of_week);
        mFakeDayOfWeek = day_of_week;
        return true;
    }

    // Get the current mode, if fake mode return true
    public boolean getFakeMode() {
        boolean fakeMode = false;
        // checking any features that are in the fake mode
        if ((mFakeLocation != null) && (mFakeLocation.length() != 0))
            fakeMode = true;

        if ((mFakeTimeOfDay != null) && (mFakeTimeOfDay.length() != 0))
            fakeMode = true;

        if ((mFakeDayOfWeek != null) && (mFakeDayOfWeek.length() != 0))
            fakeMode = true;

        return fakeMode;
    }

    // End of IAggregatorManger interface
    public java.util.Map<java.lang.String, java.lang.String> getDataMap(java.lang.String dataName) {
        if (android.bordeaux.services.AggregatorManager.sFeatureMap.get(dataName) != null)
            return android.bordeaux.services.AggregatorManager.sFeatureMap.get(dataName).getFeatureValue(dataName);
        else
            android.util.Log.e(TAG, "There is no feature called " + dataName);

        return null;
    }

    private java.util.ArrayList<android.bordeaux.services.StringString> getList(final java.util.Map<java.lang.String, java.lang.String> sample) {
        java.util.ArrayList<android.bordeaux.services.StringString> StringString_sample = new java.util.ArrayList<android.bordeaux.services.StringString>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> x : sample.entrySet()) {
            android.bordeaux.services.StringString v = new android.bordeaux.services.StringString();
            v.key = x.getKey();
            v.value = x.getValue();
            StringString_sample.add(v);
        }
        return StringString_sample;
    }
}

