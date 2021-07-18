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


// End of IBordeauxLearner Interface implemenation
public class Learning_StochasticLinearRanker extends android.bordeaux.services.ILearning_StochasticLinearRanker.Stub implements android.bordeaux.services.IBordeauxLearner {
    private final java.lang.String TAG = "ILearning_StochasticLinearRanker";

    private android.bordeaux.services.StochasticLinearRankerWithPrior mLearningSlRanker = null;

    private android.bordeaux.services.IBordeauxLearner.ModelChangeCallback modelChangeCallback = null;

    public Learning_StochasticLinearRanker() {
    }

    public void ResetRanker() {
        if (mLearningSlRanker == null)
            mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

        mLearningSlRanker.resetRanker();
    }

    public boolean UpdateClassifier(java.util.List<android.bordeaux.services.StringFloat> sample_1, java.util.List<android.bordeaux.services.StringFloat> sample_2) {
        java.util.ArrayList<android.bordeaux.services.StringFloat> temp_1 = ((java.util.ArrayList<android.bordeaux.services.StringFloat>) (sample_1));
        java.lang.String[] keys_1 = new java.lang.String[temp_1.size()];
        float[] values_1 = new float[temp_1.size()];
        for (int i = 0; i < temp_1.size(); i++) {
            keys_1[i] = temp_1.get(i).key;
            values_1[i] = temp_1.get(i).value;
        }
        java.util.ArrayList<android.bordeaux.services.StringFloat> temp_2 = ((java.util.ArrayList<android.bordeaux.services.StringFloat>) (sample_2));
        java.lang.String[] keys_2 = new java.lang.String[temp_2.size()];
        float[] values_2 = new float[temp_2.size()];
        for (int i = 0; i < temp_2.size(); i++) {
            keys_2[i] = temp_2.get(i).key;
            values_2[i] = temp_2.get(i).value;
        }
        if (mLearningSlRanker == null)
            mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

        boolean res = mLearningSlRanker.updateClassifier(keys_1, values_1, keys_2, values_2);
        if (res && (modelChangeCallback != null)) {
            modelChangeCallback.modelChanged(this);
        }
        return res;
    }

    public float ScoreSample(java.util.List<android.bordeaux.services.StringFloat> sample) {
        java.util.ArrayList<android.bordeaux.services.StringFloat> temp = ((java.util.ArrayList<android.bordeaux.services.StringFloat>) (sample));
        java.lang.String[] keys = new java.lang.String[temp.size()];
        float[] values = new float[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            keys[i] = temp.get(i).key;
            values[i] = temp.get(i).value;
        }
        if (mLearningSlRanker == null)
            mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

        return mLearningSlRanker.scoreSample(keys, values);
    }

    public boolean SetModelPriorWeight(java.util.List<android.bordeaux.services.StringFloat> sample) {
        java.util.ArrayList<android.bordeaux.services.StringFloat> temp = ((java.util.ArrayList<android.bordeaux.services.StringFloat>) (sample));
        java.util.HashMap<java.lang.String, java.lang.Float> weights = new java.util.HashMap<java.lang.String, java.lang.Float>();
        for (int i = 0; i < temp.size(); i++)
            weights.put(temp.get(i).key, temp.get(i).value);

        if (mLearningSlRanker == null)
            mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

        return mLearningSlRanker.setModelPriorWeights(weights);
    }

    public boolean SetModelParameter(java.lang.String key, java.lang.String value) {
        if (mLearningSlRanker == null)
            mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

        return mLearningSlRanker.setModelParameter(key, value);
    }

    // Beginning of the IBordeauxLearner Interface implementation
    public byte[] getModel() {
        if (mLearningSlRanker == null)
            mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

        android.bordeaux.services.StochasticLinearRankerWithPrior.Model model = mLearningSlRanker.getModel();
        try {
            java.io.ByteArrayOutputStream byteStream = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream objStream = new java.io.ObjectOutputStream(byteStream);
            objStream.writeObject(model);
            // return byteStream.toByteArray();
            byte[] bytes = byteStream.toByteArray();
            return bytes;
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Can't get model");
        }
    }

    public boolean setModel(final byte[] modelData) {
        try {
            java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(modelData);
            java.io.ObjectInputStream objStream = new java.io.ObjectInputStream(input);
            android.bordeaux.services.StochasticLinearRankerWithPrior.Model model = ((android.bordeaux.services.StochasticLinearRankerWithPrior.Model) (objStream.readObject()));
            if (mLearningSlRanker == null)
                mLearningSlRanker = new android.bordeaux.services.StochasticLinearRankerWithPrior();

            boolean res = mLearningSlRanker.loadModel(model);
            return res;
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Can't load model");
        } catch (java.lang.ClassNotFoundException e) {
            throw new java.lang.RuntimeException("Learning class not found");
        }
    }

    public android.os.IBinder getBinder() {
        return this;
    }

    public void setModelChangeCallback(android.bordeaux.services.IBordeauxLearner.ModelChangeCallback callback) {
        modelChangeCallback = callback;
    }
}

