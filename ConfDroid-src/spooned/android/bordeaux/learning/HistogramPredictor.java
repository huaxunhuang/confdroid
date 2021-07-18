/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.bordeaux.learning;


/**
 * A histogram based predictor which records co-occurrences of applations with a speficic
 * feature, for example, location, * time of day, etc. The histogram is kept in a two level
 * hash table. The first level key is the feature value and the second level key is the app
 * id.
 */
// TODOS:
// 1. Use forgetting factor to downweight istances propotional to the time
// 2. Different features could have different weights on prediction scores.
// 3. Add function to remove sampleid (i.e. remove apps that are uninstalled).
public class HistogramPredictor {
    static final java.lang.String TAG = "HistogramPredictor";

    private java.util.HashMap<java.lang.String, android.bordeaux.learning.HistogramPredictor.HistogramCounter> mPredictor = new java.util.HashMap<java.lang.String, android.bordeaux.learning.HistogramPredictor.HistogramCounter>();

    private java.util.HashMap<java.lang.String, java.lang.Integer> mClassCounts = new java.util.HashMap<java.lang.String, java.lang.Integer>();

    private java.util.HashSet<java.lang.String> mBlacklist = new java.util.HashSet<java.lang.String>();

    private static final int MINIMAL_FEATURE_VALUE_COUNTS = 5;

    private static final int MINIMAL_APP_APPEARANCE_COUNTS = 5;

    // This parameter ranges from 0 to 1 which determines the effect of app prior.
    // When it is set to 0, app prior means completely neglected. When it is set to 1
    // the predictor is a standard naive bayes model.
    private static final int PRIOR_K_VALUE = 1;

    private static final java.lang.String[] APP_BLACKLIST = new java.lang.String[]{ "com.android.contacts", "com.android.chrome", "com.android.providers.downloads.ui", "com.android.settings", "com.android.vending", "com.android.mms", "com.google.android.gm", "com.google.android.gallery3d", "com.google.android.apps.googlevoice" };

    public HistogramPredictor(java.lang.String[] blackList) {
        for (java.lang.String appName : blackList) {
            mBlacklist.add(appName);
        }
    }

    /* This class keeps the histogram counts for each feature and provide the
    joint probabilities of <feature, class>.
     */
    private class HistogramCounter {
        private java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>> mCounter = new java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>();

        public HistogramCounter() {
            mCounter.clear();
        }

        public void setCounter(java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>> counter) {
            resetCounter();
            mCounter.putAll(counter);
        }

        public void resetCounter() {
            mCounter.clear();
        }

        public void addSample(java.lang.String className, java.lang.String featureValue) {
            java.util.HashMap<java.lang.String, java.lang.Integer> classCounts;
            if (!mCounter.containsKey(featureValue)) {
                classCounts = new java.util.HashMap<java.lang.String, java.lang.Integer>();
                mCounter.put(featureValue, classCounts);
            } else {
                classCounts = mCounter.get(featureValue);
            }
            int count = (classCounts.containsKey(className)) ? classCounts.get(className) + 1 : 1;
            classCounts.put(className, count);
        }

        public java.util.HashMap<java.lang.String, java.lang.Double> getClassScores(java.lang.String featureValue) {
            java.util.HashMap<java.lang.String, java.lang.Double> classScores = new java.util.HashMap<java.lang.String, java.lang.Double>();
            if (mCounter.containsKey(featureValue)) {
                int totalCount = 0;
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : mCounter.get(featureValue).entrySet()) {
                    java.lang.String app = entry.getKey();
                    int count = entry.getValue();
                    // For apps with counts less than or equal to one, we treated
                    // those as having count one. Hence their score, i.e. log(count)
                    // would be zero. classScroes stores only apps with non-zero scores.
                    // Note that totalCount also neglect app with single occurrence.
                    if (count > 1) {
                        double score = java.lang.Math.log(((double) (count)));
                        classScores.put(app, score);
                        totalCount += count;
                    }
                }
                if (totalCount < android.bordeaux.learning.HistogramPredictor.MINIMAL_FEATURE_VALUE_COUNTS) {
                    classScores.clear();
                }
            }
            return classScores;
        }

        public byte[] getModel() {
            try {
                java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream();
                java.io.ObjectOutputStream objStream = new java.io.ObjectOutputStream(byteStream);
                synchronized(mCounter) {
                    objStream.writeObject(mCounter);
                }
                byte[] bytes = byteStream.toByteArray();
                return bytes;
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException("Can't get model");
            }
        }

        public boolean setModel(final byte[] modelData) {
            mCounter.clear();
            java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>> model;
            try {
                java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(modelData);
                java.io.ObjectInputStream objStream = new java.io.ObjectInputStream(input);
                model = ((java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>) (objStream.readObject()));
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException("Can't load model");
            } catch (java.lang.ClassNotFoundException e) {
                throw new java.lang.RuntimeException("Learning class not found");
            }
            synchronized(mCounter) {
                mCounter.putAll(model);
            }
            return true;
        }

        public java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>> getCounter() {
            return mCounter;
        }

        public java.lang.String toString() {
            java.lang.String result = "";
            for (java.util.Map.Entry<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>> entry : mCounter.entrySet()) {
                result += ((("{ " + entry.getKey()) + " : ") + entry.getValue().toString()) + " }";
            }
            return result;
        }
    }

    /* Given a map of feature name -value pairs returns topK mostly likely apps to
    be launched with corresponding likelihoods. If topK is set zero, it will return
    the whole list.
     */
    public java.util.List<java.util.Map.Entry<java.lang.String, java.lang.Double>> findTopClasses(java.util.Map<java.lang.String, java.lang.String> features, int topK) {
        // Most sophisticated function in this class
        java.util.HashMap<java.lang.String, java.lang.Double> appScores = new java.util.HashMap<java.lang.String, java.lang.Double>();
        int validFeatureCount = 0;
        // compute all app scores
        for (java.util.Map.Entry<java.lang.String, android.bordeaux.learning.HistogramPredictor.HistogramCounter> entry : mPredictor.entrySet()) {
            java.lang.String featureName = entry.getKey();
            android.bordeaux.learning.HistogramPredictor.HistogramCounter counter = entry.getValue();
            if (features.containsKey(featureName)) {
                java.lang.String featureValue = features.get(featureName);
                java.util.HashMap<java.lang.String, java.lang.Double> scoreMap = counter.getClassScores(featureValue);
                if (scoreMap.isEmpty()) {
                    continue;
                }
                validFeatureCount++;
                for (java.util.Map.Entry<java.lang.String, java.lang.Double> item : scoreMap.entrySet()) {
                    java.lang.String appName = item.getKey();
                    double appScore = item.getValue();
                    if (appScores.containsKey(appName)) {
                        appScore += appScores.get(appName);
                    }
                    appScores.put(appName, appScore);
                }
            }
        }
        java.util.HashMap<java.lang.String, java.lang.Double> appCandidates = new java.util.HashMap<java.lang.String, java.lang.Double>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Double> entry : appScores.entrySet()) {
            java.lang.String appName = entry.getKey();
            if (mBlacklist.contains(appName)) {
                android.util.Log.i(android.bordeaux.learning.HistogramPredictor.TAG, appName + " is in blacklist");
                continue;
            }
            if (!mClassCounts.containsKey(appName)) {
                throw new java.lang.RuntimeException("class count error!");
            }
            int appCount = mClassCounts.get(appName);
            if (appCount < android.bordeaux.learning.HistogramPredictor.MINIMAL_APP_APPEARANCE_COUNTS) {
                android.util.Log.i(android.bordeaux.learning.HistogramPredictor.TAG, appName + " doesn't have enough counts");
                continue;
            }
            double appScore = entry.getValue();
            double appPrior = java.lang.Math.log(((double) (appCount)));
            appCandidates.put(appName, appScore - (appPrior * (validFeatureCount - android.bordeaux.learning.HistogramPredictor.PRIOR_K_VALUE)));
        }
        // sort app scores
        java.util.List<java.util.Map.Entry<java.lang.String, java.lang.Double>> appList = new java.util.ArrayList<java.util.Map.Entry<java.lang.String, java.lang.Double>>(appCandidates.size());
        appList.addAll(appCandidates.entrySet());
        java.util.Collections.sort(appList, new java.util.Comparator<java.util.Map.Entry<java.lang.String, java.lang.Double>>() {
            public int compare(java.util.Map.Entry<java.lang.String, java.lang.Double> o1, java.util.Map.Entry<java.lang.String, java.lang.Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        if (topK == 0) {
            topK = appList.size();
        }
        return appList.subList(0, java.lang.Math.min(topK, appList.size()));
    }

    /* Add a new observation of given sample id and features to the histograms */
    public void addSample(java.lang.String sampleId, java.util.Map<java.lang.String, java.lang.String> features) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : features.entrySet()) {
            java.lang.String featureName = entry.getKey();
            java.lang.String featureValue = entry.getValue();
            useFeature(featureName);
            android.bordeaux.learning.HistogramPredictor.HistogramCounter counter = mPredictor.get(featureName);
            counter.addSample(sampleId, featureValue);
        }
        int sampleCount = (mClassCounts.containsKey(sampleId)) ? mClassCounts.get(sampleId) + 1 : 1;
        mClassCounts.put(sampleId, sampleCount);
    }

    /* reset predictor to a empty model */
    public void resetPredictor() {
        // TODO: not sure this step would reduce memory waste
        for (android.bordeaux.learning.HistogramPredictor.HistogramCounter counter : mPredictor.values()) {
            counter.resetCounter();
        }
        mPredictor.clear();
        mClassCounts.clear();
    }

    /* convert the prediction model into a byte array */
    public byte[] getModel() {
        // TODO: convert model to a more memory efficient data structure.
        java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>> model = new java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>>();
        for (java.util.Map.Entry<java.lang.String, android.bordeaux.learning.HistogramPredictor.HistogramCounter> entry : mPredictor.entrySet()) {
            model.put(entry.getKey(), entry.getValue().getCounter());
        }
        try {
            java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream objStream = new java.io.ObjectOutputStream(byteStream);
            objStream.writeObject(model);
            byte[] bytes = byteStream.toByteArray();
            return bytes;
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Can't get model");
        }
    }

    /* set the prediction model from a model data in the format of byte array */
    public boolean setModel(final byte[] modelData) {
        java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>> model;
        try {
            java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(modelData);
            java.io.ObjectInputStream objStream = new java.io.ObjectInputStream(input);
            model = ((java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>>) (objStream.readObject()));
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Can't load model");
        } catch (java.lang.ClassNotFoundException e) {
            throw new java.lang.RuntimeException("Learning class not found");
        }
        resetPredictor();
        for (java.util.Map.Entry<java.lang.String, java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>>> entry : model.entrySet()) {
            useFeature(entry.getKey());
            mPredictor.get(entry.getKey()).setCounter(entry.getValue());
        }
        // TODO: this is a temporary fix for now
        loadClassCounter();
        return true;
    }

    private void loadClassCounter() {
        java.lang.String TIME_OF_WEEK = "Time of Week";
        if (!mPredictor.containsKey(TIME_OF_WEEK)) {
            throw new java.lang.RuntimeException("Precition model error: missing Time of Week!");
        }
        java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.Integer>> counter = mPredictor.get(TIME_OF_WEEK).getCounter();
        mClassCounts.clear();
        for (java.util.HashMap<java.lang.String, java.lang.Integer> map : counter.values()) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : map.entrySet()) {
                int classCount = entry.getValue();
                java.lang.String className = entry.getKey();
                // mTotalClassCount += classCount;
                if (mClassCounts.containsKey(className)) {
                    classCount += mClassCounts.get(className);
                }
                mClassCounts.put(className, classCount);
            }
        }
        android.util.Log.i(android.bordeaux.learning.HistogramPredictor.TAG, "class counts: " + mClassCounts);
    }

    private void useFeature(java.lang.String featureName) {
        if (!mPredictor.containsKey(featureName)) {
            mPredictor.put(featureName, new android.bordeaux.learning.HistogramPredictor.HistogramCounter());
        }
    }
}

