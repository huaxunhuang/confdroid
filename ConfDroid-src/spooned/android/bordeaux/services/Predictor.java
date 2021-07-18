/**
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you my not use this file except in compliance with the License.
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
 * This is interface to implement Prediction based on histogram that
 * uses predictor_histogram from learnerning section
 */
// End of IBordeauxLearner Interface implemenation
public class Predictor extends android.bordeaux.services.IPredictor.Stub implements android.bordeaux.services.IBordeauxLearner {
    private final java.lang.String TAG = "Predictor";

    private android.bordeaux.services.IBordeauxLearner.ModelChangeCallback modelChangeCallback = null;

    private android.bordeaux.services.FeatureAssembly mFeatureAssembly = new android.bordeaux.services.FeatureAssembly();

    public static final java.lang.String SET_FEATURE = "SetFeature";

    public static final java.lang.String SET_PAIRED_FEATURES = "SetPairedFeatures";

    public static final java.lang.String FEATURE_SEPARATOR = ":";

    public static final java.lang.String USE_HISTORY = "UseHistory";

    public static final java.lang.String PREVIOUS_SAMPLE = "PreviousSample";

    private boolean mUseHistory = false;

    private long mHistorySpan = 0;

    private java.lang.String mPrevSample;

    private long mPrevSampleTime;

    // TODO: blacklist should be set outside Bordeaux service!
    private static final java.lang.String[] APP_BLACKLIST = new java.lang.String[]{ "com.android.contacts", "com.android.chrome", "com.android.providers.downloads.ui", "com.android.settings", "com.android.vending", "com.android.mms", "com.google.android.gm", "com.google.android.gallery3d", "com.google.android.apps.googlevoice" };

    private android.bordeaux.learning.HistogramPredictor mPredictor = new android.bordeaux.learning.HistogramPredictor(android.bordeaux.services.Predictor.APP_BLACKLIST);

    /**
     * Reset the Predictor
     */
    public void resetPredictor() {
        mPredictor.resetPredictor();
        if (modelChangeCallback != null) {
            modelChangeCallback.modelChanged(this);
        }
    }

    /**
     * Input is a sampleName e.g.action name. This input is then augmented with requested build-in
     * features such as time and location to create sampleFeatures. The sampleFeatures is then
     * pushed to the histogram
     */
    public void pushNewSample(java.lang.String sampleName) {
        java.util.Map<java.lang.String, java.lang.String> sampleFeatures = getSampleFeatures();
        android.util.Log.i(TAG, (("pushNewSample " + sampleName) + ": ") + sampleFeatures);
        // TODO: move to the end of the function?
        mPrevSample = sampleName;
        mPrevSampleTime = java.lang.System.currentTimeMillis();
        mPredictor.addSample(sampleName, sampleFeatures);
        if (modelChangeCallback != null) {
            modelChangeCallback.modelChanged(this);
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> getSampleFeatures() {
        java.util.Map<java.lang.String, java.lang.String> sampleFeatures = mFeatureAssembly.getFeatureMap();
        long currTime = java.lang.System.currentTimeMillis();
        if ((mUseHistory && (mPrevSample != null)) && ((currTime - mPrevSampleTime) < mHistorySpan)) {
            sampleFeatures.put(android.bordeaux.services.Predictor.PREVIOUS_SAMPLE, mPrevSample);
        }
        return sampleFeatures;
    }

    // TODO: getTopK samples instead get scord for debugging only
    /**
     * return probabilty of an exmple using the histogram
     */
    public java.util.List<android.bordeaux.services.StringFloat> getTopCandidates(int topK) {
        java.util.Map<java.lang.String, java.lang.String> features = getSampleFeatures();
        java.util.List<java.util.Map.Entry<java.lang.String, java.lang.Double>> topApps = mPredictor.findTopClasses(features, topK);
        int listSize = topApps.size();
        java.util.ArrayList<android.bordeaux.services.StringFloat> result = new java.util.ArrayList<android.bordeaux.services.StringFloat>(listSize);
        for (int i = 0; i < listSize; ++i) {
            java.util.Map.Entry<java.lang.String, java.lang.Double> entry = topApps.get(i);
            result.add(new android.bordeaux.services.StringFloat(entry.getKey(), entry.getValue().floatValue()));
        }
        return result;
    }

    /**
     * Set parameters for 1) using History in probability estimations e.g. consider the last event
     * and 2) featureAssembly e.g. time and location.
     */
    public boolean setPredictorParameter(java.lang.String key, java.lang.String value) {
        android.util.Log.i(TAG, (("setParameter : " + key) + ", ") + value);
        boolean result = true;
        if (key.equals(android.bordeaux.services.Predictor.SET_FEATURE)) {
            result = mFeatureAssembly.registerFeature(value);
            if (!result) {
                android.util.Log.e(TAG, ("Setting on feauture: " + value) + " which is not available");
            }
        } else
            if (key.equals(android.bordeaux.services.Predictor.SET_PAIRED_FEATURES)) {
                java.lang.String[] features = value.split(android.bordeaux.services.Predictor.FEATURE_SEPARATOR);
                result = mFeatureAssembly.registerFeaturePair(features);
                if (!result) {
                    android.util.Log.e(TAG, ("Setting feauture pair: " + value) + " is not valid");
                }
            } else
                if (key.equals(android.bordeaux.services.Predictor.USE_HISTORY)) {
                    mUseHistory = true;
                    mHistorySpan = java.lang.Long.valueOf(value);
                } else {
                    android.util.Log.e(TAG, ((("Setting parameter " + key) + " with ") + value) + " is not valid");
                }


        return result;
    }

    // Beginning of the IBordeauxLearner Interface implementation
    public byte[] getModel() {
        return mPredictor.getModel();
    }

    public boolean setModel(final byte[] modelData) {
        return mPredictor.setModel(modelData);
    }

    public android.os.IBinder getBinder() {
        return this;
    }

    public void setModelChangeCallback(android.bordeaux.services.IBordeauxLearner.ModelChangeCallback callback) {
        modelChangeCallback = callback;
    }
}

