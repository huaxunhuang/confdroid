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
 * An instance represents a sample if the label is available or a query if the
 * label is null.
 */
class Instance {
    private static final int SEQUENCE_SAMPLE_SIZE = 16;

    private static final int PATCH_SAMPLE_SIZE = 16;

    private static final float[] ORIENTATIONS = new float[]{ 0, ((float) (java.lang.Math.PI / 4)), ((float) (java.lang.Math.PI / 2)), ((float) ((java.lang.Math.PI * 3) / 4)), ((float) (java.lang.Math.PI)), -0, ((float) ((-java.lang.Math.PI) / 4)), ((float) ((-java.lang.Math.PI) / 2)), ((float) (((-java.lang.Math.PI) * 3) / 4)), ((float) (-java.lang.Math.PI)) };

    // the feature vector
    final float[] vector;

    // the label can be null
    final java.lang.String label;

    // the id of the instance
    final long id;

    private Instance(long id, float[] sample, java.lang.String sampleName) {
        this.id = id;
        vector = sample;
        label = sampleName;
    }

    private void normalize() {
        float[] sample = vector;
        float sum = 0;
        int size = sample.length;
        for (int i = 0; i < size; i++) {
            sum += sample[i] * sample[i];
        }
        float magnitude = ((float) (java.lang.Math.sqrt(sum)));
        for (int i = 0; i < size; i++) {
            sample[i] /= magnitude;
        }
    }

    /**
     * create a learning instance for a single stroke gesture
     *
     * @param gesture
     * 		
     * @param label
     * 		
     * @return the instance
     */
    static android.gesture.Instance createInstance(int sequenceType, int orientationType, android.gesture.Gesture gesture, java.lang.String label) {
        float[] pts;
        android.gesture.Instance instance;
        if (sequenceType == android.gesture.GestureStore.SEQUENCE_SENSITIVE) {
            pts = android.gesture.Instance.temporalSampler(orientationType, gesture);
            instance = new android.gesture.Instance(gesture.getID(), pts, label);
            instance.normalize();
        } else {
            pts = android.gesture.Instance.spatialSampler(gesture);
            instance = new android.gesture.Instance(gesture.getID(), pts, label);
        }
        return instance;
    }

    private static float[] spatialSampler(android.gesture.Gesture gesture) {
        return android.gesture.GestureUtils.spatialSampling(gesture, android.gesture.Instance.PATCH_SAMPLE_SIZE, false);
    }

    private static float[] temporalSampler(int orientationType, android.gesture.Gesture gesture) {
        float[] pts = android.gesture.GestureUtils.temporalSampling(gesture.getStrokes().get(0), android.gesture.Instance.SEQUENCE_SAMPLE_SIZE);
        float[] center = android.gesture.GestureUtils.computeCentroid(pts);
        float orientation = ((float) (java.lang.Math.atan2(pts[1] - center[1], pts[0] - center[0])));
        float adjustment = -orientation;
        if (orientationType != android.gesture.GestureStore.ORIENTATION_INVARIANT) {
            int count = android.gesture.Instance.ORIENTATIONS.length;
            for (int i = 0; i < count; i++) {
                float delta = android.gesture.Instance.ORIENTATIONS[i] - orientation;
                if (java.lang.Math.abs(delta) < java.lang.Math.abs(adjustment)) {
                    adjustment = delta;
                }
            }
        }
        android.gesture.GestureUtils.translate(pts, -center[0], -center[1]);
        android.gesture.GestureUtils.rotate(pts, adjustment);
        return pts;
    }
}

