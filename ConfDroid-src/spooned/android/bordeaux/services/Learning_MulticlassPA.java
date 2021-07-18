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


public class Learning_MulticlassPA extends android.bordeaux.services.ILearning_MulticlassPA.Stub implements android.bordeaux.services.IBordeauxLearner {
    private android.bordeaux.learning.MulticlassPA mMulticlassPA_learner;

    private android.bordeaux.services.IBordeauxLearner.ModelChangeCallback modelChangeCallback = null;

    class IntFloatArray {
        int[] indexArray;

        float[] floatArray;
    }

    private android.bordeaux.services.Learning_MulticlassPA.IntFloatArray splitIntFloatArray(java.util.List<android.bordeaux.services.IntFloat> sample) {
        android.bordeaux.services.Learning_MulticlassPA.IntFloatArray splited = new android.bordeaux.services.Learning_MulticlassPA.IntFloatArray();
        java.util.ArrayList<android.bordeaux.services.IntFloat> s = ((java.util.ArrayList<android.bordeaux.services.IntFloat>) (sample));
        splited.indexArray = new int[s.size()];
        splited.floatArray = new float[s.size()];
        for (int i = 0; i < s.size(); i++) {
            splited.indexArray[i] = s.get(i).index;
            splited.floatArray[i] = s.get(i).value;
        }
        return splited;
    }

    public Learning_MulticlassPA() {
        mMulticlassPA_learner = new android.bordeaux.learning.MulticlassPA(2, 2, 0.001F);
    }

    // Beginning of the IBordeauxLearner Interface implementation
    public byte[] getModel() {
        return null;
    }

    public boolean setModel(final byte[] modelData) {
        return false;
    }

    public android.os.IBinder getBinder() {
        return this;
    }

    public void setModelChangeCallback(android.bordeaux.services.IBordeauxLearner.ModelChangeCallback callback) {
        modelChangeCallback = callback;
    }

    // End of IBordeauxLearner Interface implemenation
    // This implementation, combines training and prediction in one step.
    // The return value is the prediction value for the supplied sample. It
    // also update the model with the current sample.
    public void TrainOneSample(java.util.List<android.bordeaux.services.IntFloat> sample, int target) {
        android.bordeaux.services.Learning_MulticlassPA.IntFloatArray splited = splitIntFloatArray(sample);
        mMulticlassPA_learner.sparseTrainOneExample(splited.indexArray, splited.floatArray, target);
        if (modelChangeCallback != null) {
            modelChangeCallback.modelChanged(this);
        }
    }

    public int Classify(java.util.List<android.bordeaux.services.IntFloat> sample) {
        android.bordeaux.services.Learning_MulticlassPA.IntFloatArray splited = splitIntFloatArray(sample);
        int prediction = mMulticlassPA_learner.sparseGetClass(splited.indexArray, splited.floatArray);
        return prediction;
    }
}

