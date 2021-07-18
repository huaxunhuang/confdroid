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
 * Classifier for the Learning framework.
 *  For training: call trainOneSample
 *  For classifying: call classify
 *  Data is represented as sparse key, value pair. And key is an integer, value
 *  is a float. Class label(target) for the training data is an integer.
 *  Note: since the actual classifier is running in a remote the service.
 *  Sometimes the connection may be lost or not established.
 */
public class BordeauxClassifier {
    static final java.lang.String TAG = "BordeauxClassifier";

    private android.content.Context mContext;

    private java.lang.String mName;

    private android.bordeaux.services.ILearning_MulticlassPA mClassifier;

    private java.util.ArrayList<android.bordeaux.services.IntFloat> getArrayList(final java.util.HashMap<java.lang.Integer, java.lang.Float> sample) {
        java.util.ArrayList<android.bordeaux.services.IntFloat> intfloat_sample = new java.util.ArrayList<android.bordeaux.services.IntFloat>();
        for (java.util.Map.Entry<java.lang.Integer, java.lang.Float> x : sample.entrySet()) {
            android.bordeaux.services.IntFloat v = new android.bordeaux.services.IntFloat();
            v.index = x.getKey();
            v.value = x.getValue();
            intfloat_sample.add(v);
        }
        return intfloat_sample;
    }

    private boolean retrieveClassifier() {
        if (mClassifier == null)
            mClassifier = android.bordeaux.services.BordeauxManagerService.getClassifier(mContext, mName);

        // if classifier is not available, return false
        if (mClassifier == null) {
            android.util.Log.i(android.bordeaux.services.BordeauxClassifier.TAG, "Classifier not available.");
            return false;
        }
        return true;
    }

    public BordeauxClassifier(android.content.Context context) {
        mContext = context;
        mName = "defaultClassifier";
        mClassifier = android.bordeaux.services.BordeauxManagerService.getClassifier(context, mName);
    }

    public BordeauxClassifier(android.content.Context context, java.lang.String name) {
        mContext = context;
        mName = name;
        mClassifier = android.bordeaux.services.BordeauxManagerService.getClassifier(context, mName);
    }

    public boolean update(final java.util.HashMap<java.lang.Integer, java.lang.Float> sample, int target) {
        if (!retrieveClassifier())
            return false;

        try {
            mClassifier.TrainOneSample(getArrayList(sample), target);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxClassifier.TAG, "Exception: training one sample.");
            return false;
        }
        return true;
    }

    public int classify(final java.util.HashMap<java.lang.Integer, java.lang.Float> sample) {
        // if classifier is not available return -1 as an indication of fail.
        if (!retrieveClassifier())
            return -1;

        try {
            return mClassifier.Classify(getArrayList(sample));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxClassifier.TAG, "Exception: classify the sample.");
            // return an invalid number.
            // TODO: throw exception.
            return -1;
        }
    }
}

