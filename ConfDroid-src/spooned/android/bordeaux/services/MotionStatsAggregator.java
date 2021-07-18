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


public class MotionStatsAggregator extends android.bordeaux.services.Aggregator {
    final java.lang.String TAG = "MotionStatsAggregator";

    public static final java.lang.String CURRENT_MOTION = "Current Motion";

    public java.lang.String[] getListOfFeatures() {
        java.lang.String[] list = new java.lang.String[1];
        list[0] = android.bordeaux.services.MotionStatsAggregator.CURRENT_MOTION;
        return list;
    }

    public java.util.Map<java.lang.String, java.lang.String> getFeatureValue(java.lang.String featureName) {
        java.util.HashMap<java.lang.String, java.lang.String> m = new java.util.HashMap<java.lang.String, java.lang.String>();
        if (featureName.equals(android.bordeaux.services.MotionStatsAggregator.CURRENT_MOTION))
            m.put(android.bordeaux.services.MotionStatsAggregator.CURRENT_MOTION, "Running");
        else// TODO maybe use clustering for user motion

            android.util.Log.e(TAG, "There is no motion feature called " + featureName);

        return ((java.util.Map) (m));
    }
}

