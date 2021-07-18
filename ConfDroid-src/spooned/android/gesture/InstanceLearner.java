/**
 * Copyright (C) 2008-2009 The Android Open Source Project
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
package android.gesture;


/**
 * An implementation of an instance-based learner
 */
class InstanceLearner extends android.gesture.Learner {
    private static final java.util.Comparator<android.gesture.Prediction> sComparator = new java.util.Comparator<android.gesture.Prediction>() {
        public int compare(android.gesture.Prediction object1, android.gesture.Prediction object2) {
            double score1 = object1.score;
            double score2 = object2.score;
            if (score1 > score2) {
                return -1;
            } else
                if (score1 < score2) {
                    return 1;
                } else {
                    return 0;
                }

        }
    };

    @java.lang.Override
    java.util.ArrayList<android.gesture.Prediction> classify(int sequenceType, int orientationType, float[] vector) {
        java.util.ArrayList<android.gesture.Prediction> predictions = new java.util.ArrayList<android.gesture.Prediction>();
        java.util.ArrayList<android.gesture.Instance> instances = getInstances();
        int count = instances.size();
        java.util.TreeMap<java.lang.String, java.lang.Double> label2score = new java.util.TreeMap<java.lang.String, java.lang.Double>();
        for (int i = 0; i < count; i++) {
            android.gesture.Instance sample = instances.get(i);
            if (sample.vector.length != vector.length) {
                continue;
            }
            double distance;
            if (sequenceType == android.gesture.GestureStore.SEQUENCE_SENSITIVE) {
                distance = android.gesture.GestureUtils.minimumCosineDistance(sample.vector, vector, orientationType);
            } else {
                distance = android.gesture.GestureUtils.squaredEuclideanDistance(sample.vector, vector);
            }
            double weight;
            if (distance == 0) {
                weight = java.lang.Double.MAX_VALUE;
            } else {
                weight = 1 / distance;
            }
            java.lang.Double score = label2score.get(sample.label);
            if ((score == null) || (weight > score)) {
                label2score.put(sample.label, weight);
            }
        }
        // double sum = 0;
        for (java.lang.String name : label2score.keySet()) {
            double score = label2score.get(name);
            // sum += score;
            predictions.add(new android.gesture.Prediction(name, score));
        }
        // normalize
        // for (Prediction prediction : predictions) {
        // prediction.score /= sum;
        // }
        java.util.Collections.sort(predictions, android.gesture.InstanceLearner.sComparator);
        return predictions;
    }
}

