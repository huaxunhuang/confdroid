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
 * Ranker for the Learning framework.
 *  For training: call updateClassifier with a pair of samples.
 *  For ranking: call scoreSample to the score of the rank
 *  Data is represented as sparse key, value pair. And key is a String, value
 *  is a float.
 *  Note: since the actual ranker is running in a remote the service.
 *  Sometimes the connection may be lost or not established.
 */
public class BordeauxRanker {
    static final java.lang.String TAG = "BordeauxRanker";

    static final java.lang.String RANKER_NOTAVAILABLE = "Ranker not Available";

    private android.content.Context mContext;

    private java.lang.String mName;

    private android.bordeaux.services.ILearning_StochasticLinearRanker mRanker;

    private java.util.ArrayList<android.bordeaux.services.StringFloat> getArrayList(final java.util.HashMap<java.lang.String, java.lang.Float> sample) {
        java.util.ArrayList<android.bordeaux.services.StringFloat> stringfloat_sample = new java.util.ArrayList<android.bordeaux.services.StringFloat>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Float> x : sample.entrySet()) {
            android.bordeaux.services.StringFloat v = new android.bordeaux.services.StringFloat();
            v.key = x.getKey();
            v.value = x.getValue();
            stringfloat_sample.add(v);
        }
        return stringfloat_sample;
    }

    public boolean retrieveRanker() {
        if (mRanker == null)
            mRanker = android.bordeaux.services.BordeauxManagerService.getRanker(mContext, mName);

        // if classifier is not available, return false
        if (mRanker == null) {
            android.util.Log.e(android.bordeaux.services.BordeauxRanker.TAG, "Ranker not available.");
            return false;
        }
        return true;
    }

    public BordeauxRanker(android.content.Context context) {
        mContext = context;
        mName = "defaultRanker";
        mRanker = android.bordeaux.services.BordeauxManagerService.getRanker(context, mName);
    }

    public BordeauxRanker(android.content.Context context, java.lang.String name) {
        mContext = context;
        mName = name;
        mRanker = android.bordeaux.services.BordeauxManagerService.getRanker(context, mName);
    }

    // Update the ranker with two samples, sample1 has higher rank than
    // sample2.
    public boolean update(final java.util.HashMap<java.lang.String, java.lang.Float> sample1, final java.util.HashMap<java.lang.String, java.lang.Float> sample2) {
        if (!retrieveRanker())
            return false;

        try {
            mRanker.UpdateClassifier(getArrayList(sample1), getArrayList(sample2));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxRanker.TAG, "Exception: updateClassifier.");
            return false;
        }
        return true;
    }

    public boolean reset() {
        if (!retrieveRanker()) {
            android.util.Log.e(android.bordeaux.services.BordeauxRanker.TAG, "Exception: Ranker is not availible");
            return false;
        }
        try {
            mRanker.ResetRanker();
            return true;
        } catch (android.os.RemoteException e) {
        }
        return false;
    }

    public float scoreSample(final java.util.HashMap<java.lang.String, java.lang.Float> sample) {
        if (!retrieveRanker())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxRanker.RANKER_NOTAVAILABLE);

        try {
            return mRanker.ScoreSample(getArrayList(sample));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxRanker.TAG, "Exception: scoring the sample.");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxRanker.RANKER_NOTAVAILABLE);
        }
    }

    public boolean setPriorWeight(final java.util.HashMap<java.lang.String, java.lang.Float> sample) {
        if (!retrieveRanker())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxRanker.RANKER_NOTAVAILABLE);

        try {
            return mRanker.SetModelPriorWeight(getArrayList(sample));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxRanker.TAG, "Exception: set prior Weights");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxRanker.RANKER_NOTAVAILABLE);
        }
    }

    public boolean setParameter(java.lang.String key, java.lang.String value) {
        if (!retrieveRanker())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxRanker.RANKER_NOTAVAILABLE);

        try {
            return mRanker.SetModelParameter(key, value);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxRanker.TAG, "Exception: Setting Parameter");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxRanker.RANKER_NOTAVAILABLE);
        }
    }
}

