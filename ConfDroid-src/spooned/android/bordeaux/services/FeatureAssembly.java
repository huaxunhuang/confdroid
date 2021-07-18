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


class FeatureAssembly {
    private static final java.lang.String TAG = "FeatureAssembly";

    private java.util.List<java.lang.String> mPossibleFeatures;

    private java.util.HashSet<java.lang.String> mUseFeatures;

    private java.util.HashSet<android.util.Pair<java.lang.String, java.lang.String>> mUsePairedFeatures;

    private android.bordeaux.services.AggregatorManager mAggregatorManager;

    public FeatureAssembly() {
        mAggregatorManager = android.bordeaux.services.AggregatorManager.getInstance();
        mPossibleFeatures = java.util.Arrays.asList(mAggregatorManager.getListOfFeatures());
        mUseFeatures = new java.util.HashSet<java.lang.String>();
        mUsePairedFeatures = new java.util.HashSet<android.util.Pair<java.lang.String, java.lang.String>>();
    }

    public boolean registerFeature(java.lang.String s) {
        if (mPossibleFeatures.contains(s)) {
            mUseFeatures.add(s);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerFeaturePair(java.lang.String[] features) {
        if (((features.length != 2) || (!mPossibleFeatures.contains(features[0]))) || (!mPossibleFeatures.contains(features[1]))) {
            return false;
        } else {
            mUseFeatures.add(features[0]);
            mUseFeatures.add(features[1]);
            mUsePairedFeatures.add(android.util.Pair.create(features[0], features[1]));
            return true;
        }
    }

    public java.util.Set<java.lang.String> getUsedFeatures() {
        return ((java.util.Set) (mUseFeatures));
    }

    public java.util.Map<java.lang.String, java.lang.String> getFeatureMap() {
        java.util.HashMap<java.lang.String, java.lang.String> featureMap = new java.util.HashMap<java.lang.String, java.lang.String>();
        java.util.Iterator itr = mUseFeatures.iterator();
        while (itr.hasNext()) {
            java.lang.String f = ((java.lang.String) (itr.next()));
            java.util.Map<java.lang.String, java.lang.String> features = mAggregatorManager.getDataMap(f);
            // TODO: sanity check for now.
            if (features.size() > 1) {
                throw new java.lang.RuntimeException("Incorrect feature format extracted from aggregator.");
            }
            featureMap.putAll(features);
        } 
        if (!mUsePairedFeatures.isEmpty()) {
            itr = mUsePairedFeatures.iterator();
            while (itr.hasNext()) {
                android.util.Pair<java.lang.String, java.lang.String> pair = ((android.util.Pair<java.lang.String, java.lang.String>) (itr.next()));
                if (featureMap.containsKey(pair.first) && featureMap.containsKey(pair.second)) {
                    java.lang.String key = (pair.first + android.bordeaux.services.Predictor.FEATURE_SEPARATOR) + pair.second;
                    java.lang.String value = (featureMap.get(pair.first) + android.bordeaux.services.Predictor.FEATURE_SEPARATOR) + featureMap.get(pair.second);
                    featureMap.put(key, value);
                }
            } 
        }
        return ((java.util.Map) (featureMap));
    }

    public java.lang.String augmentFeatureInputString(java.lang.String s) {
        java.lang.String fs = s;
        java.util.Iterator itr = mUseFeatures.iterator();
        while (itr.hasNext()) {
            java.lang.String f = ((java.lang.String) (itr.next()));
            fs = (fs + "+") + mAggregatorManager.getDataMap(f).get(f);
        } 
        return fs;
    }
}

