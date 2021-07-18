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
 * Predictor for the Learning framework.
 */
public class BordeauxPredictor {
    static final java.lang.String TAG = "BordeauxPredictor";

    static final java.lang.String PREDICTOR_NOTAVAILABLE = "Predictor is not available.";

    private android.content.Context mContext;

    private java.lang.String mName;

    private android.bordeaux.services.IPredictor mPredictor;

    public BordeauxPredictor(android.content.Context context) {
        mContext = context;
        mName = "defaultPredictor";
        mPredictor = android.bordeaux.services.BordeauxManagerService.getPredictor(context, mName);
    }

    public BordeauxPredictor(android.content.Context context, java.lang.String name) {
        mContext = context;
        mName = name;
        mPredictor = android.bordeaux.services.BordeauxManagerService.getPredictor(context, mName);
    }

    public boolean reset() {
        if (!retrievePredictor()) {
            android.util.Log.e(android.bordeaux.services.BordeauxPredictor.TAG, "reset: " + android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);
            return false;
        }
        try {
            mPredictor.resetPredictor();
            return true;
        } catch (android.os.RemoteException e) {
        }
        return false;
    }

    public boolean retrievePredictor() {
        if (mPredictor == null) {
            mPredictor = android.bordeaux.services.BordeauxManagerService.getPredictor(mContext, mName);
        }
        if (mPredictor == null) {
            android.util.Log.e(android.bordeaux.services.BordeauxPredictor.TAG, "retrievePredictor: " + android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);
            return false;
        }
        return true;
    }

    public void addSample(java.lang.String sampleName) {
        if (!retrievePredictor())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);

        try {
            mPredictor.pushNewSample(sampleName);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxPredictor.TAG, "Exception: pushing a new example");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);
        }
    }

    public java.util.ArrayList<android.util.Pair<java.lang.String, java.lang.Float>> getTopSamples() {
        return getTopSamples(0);
    }

    public java.util.ArrayList<android.util.Pair<java.lang.String, java.lang.Float>> getTopSamples(int topK) {
        try {
            java.util.ArrayList<android.bordeaux.services.StringFloat> topList = ((java.util.ArrayList<android.bordeaux.services.StringFloat>) (mPredictor.getTopCandidates(topK)));
            java.util.ArrayList<android.util.Pair<java.lang.String, java.lang.Float>> topSamples = new java.util.ArrayList<android.util.Pair<java.lang.String, java.lang.Float>>(topList.size());
            for (int i = 0; i < topList.size(); ++i) {
                topSamples.add(new android.util.Pair<java.lang.String, java.lang.Float>(topList.get(i).key, topList.get(i).value));
            }
            return topSamples;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxPredictor.TAG, "Exception: getTopSamples");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);
        }
    }

    public boolean setParameter(java.lang.String key, java.lang.String value) {
        if (!retrievePredictor())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);

        try {
            return mPredictor.setPredictorParameter(key, value);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxPredictor.TAG, "Exception: setting predictor parameter");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxPredictor.PREDICTOR_NOTAVAILABLE);
        }
    }
}

