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
 * GestureLibrary maintains gesture examples and makes predictions on a new
 * gesture
 */
// 
// File format for GestureStore:
// 
// Nb. bytes   Java type   Description
// -----------------------------------
// Header
// 2 bytes     short       File format version number
// 4 bytes     int         Number of entries
// Entry
// X bytes     UTF String  Entry name
// 4 bytes     int         Number of gestures
// Gesture
// 8 bytes     long        Gesture ID
// 4 bytes     int         Number of strokes
// Stroke
// 4 bytes     int         Number of points
// Point
// 4 bytes     float       X coordinate of the point
// 4 bytes     float       Y coordinate of the point
// 8 bytes     long        Time stamp
// 
public class GestureStore {
    public static final int SEQUENCE_INVARIANT = 1;

    // when SEQUENCE_SENSITIVE is used, only single stroke gestures are currently allowed
    public static final int SEQUENCE_SENSITIVE = 2;

    // ORIENTATION_SENSITIVE and ORIENTATION_INVARIANT are only for SEQUENCE_SENSITIVE gestures
    public static final int ORIENTATION_INVARIANT = 1;

    // at most 2 directions can be recognized
    public static final int ORIENTATION_SENSITIVE = 2;

    // at most 4 directions can be recognized
    static final int ORIENTATION_SENSITIVE_4 = 4;

    // at most 8 directions can be recognized
    static final int ORIENTATION_SENSITIVE_8 = 8;

    private static final short FILE_FORMAT_VERSION = 1;

    private static final boolean PROFILE_LOADING_SAVING = false;

    private int mSequenceType = android.gesture.GestureStore.SEQUENCE_SENSITIVE;

    private int mOrientationStyle = android.gesture.GestureStore.ORIENTATION_SENSITIVE;

    private final java.util.HashMap<java.lang.String, java.util.ArrayList<android.gesture.Gesture>> mNamedGestures = new java.util.HashMap<java.lang.String, java.util.ArrayList<android.gesture.Gesture>>();

    private android.gesture.Learner mClassifier;

    private boolean mChanged = false;

    public GestureStore() {
        mClassifier = new android.gesture.InstanceLearner();
    }

    /**
     * Specify how the gesture library will handle orientation.
     * Use ORIENTATION_INVARIANT or ORIENTATION_SENSITIVE
     *
     * @param style
     * 		
     */
    public void setOrientationStyle(int style) {
        mOrientationStyle = style;
    }

    public int getOrientationStyle() {
        return mOrientationStyle;
    }

    /**
     *
     *
     * @param type
     * 		SEQUENCE_INVARIANT or SEQUENCE_SENSITIVE
     */
    public void setSequenceType(int type) {
        mSequenceType = type;
    }

    /**
     *
     *
     * @return SEQUENCE_INVARIANT or SEQUENCE_SENSITIVE
     */
    public int getSequenceType() {
        return mSequenceType;
    }

    /**
     * Get all the gesture entry names in the library
     *
     * @return a set of strings
     */
    public java.util.Set<java.lang.String> getGestureEntries() {
        return mNamedGestures.keySet();
    }

    /**
     * Recognize a gesture
     *
     * @param gesture
     * 		the query
     * @return a list of predictions of possible entries for a given gesture
     */
    public java.util.ArrayList<android.gesture.Prediction> recognize(android.gesture.Gesture gesture) {
        android.gesture.Instance instance = android.gesture.Instance.createInstance(mSequenceType, mOrientationStyle, gesture, null);
        return mClassifier.classify(mSequenceType, mOrientationStyle, instance.vector);
    }

    /**
     * Add a gesture for the entry
     *
     * @param entryName
     * 		entry name
     * @param gesture
     * 		
     */
    public void addGesture(java.lang.String entryName, android.gesture.Gesture gesture) {
        if ((entryName == null) || (entryName.length() == 0)) {
            return;
        }
        java.util.ArrayList<android.gesture.Gesture> gestures = mNamedGestures.get(entryName);
        if (gestures == null) {
            gestures = new java.util.ArrayList<android.gesture.Gesture>();
            mNamedGestures.put(entryName, gestures);
        }
        gestures.add(gesture);
        mClassifier.addInstance(android.gesture.Instance.createInstance(mSequenceType, mOrientationStyle, gesture, entryName));
        mChanged = true;
    }

    /**
     * Remove a gesture from the library. If there are no more gestures for the
     * given entry, the gesture entry will be removed.
     *
     * @param entryName
     * 		entry name
     * @param gesture
     * 		
     */
    public void removeGesture(java.lang.String entryName, android.gesture.Gesture gesture) {
        java.util.ArrayList<android.gesture.Gesture> gestures = mNamedGestures.get(entryName);
        if (gestures == null) {
            return;
        }
        gestures.remove(gesture);
        // if there are no more samples, remove the entry automatically
        if (gestures.isEmpty()) {
            mNamedGestures.remove(entryName);
        }
        mClassifier.removeInstance(gesture.getID());
        mChanged = true;
    }

    /**
     * Remove a entry of gestures
     *
     * @param entryName
     * 		the entry name
     */
    public void removeEntry(java.lang.String entryName) {
        mNamedGestures.remove(entryName);
        mClassifier.removeInstances(entryName);
        mChanged = true;
    }

    /**
     * Get all the gestures of an entry
     *
     * @param entryName
     * 		
     * @return the list of gestures that is under this name
     */
    public java.util.ArrayList<android.gesture.Gesture> getGestures(java.lang.String entryName) {
        java.util.ArrayList<android.gesture.Gesture> gestures = mNamedGestures.get(entryName);
        if (gestures != null) {
            return new java.util.ArrayList<android.gesture.Gesture>(gestures);
        } else {
            return null;
        }
    }

    public boolean hasChanged() {
        return mChanged;
    }

    /**
     * Save the gesture library
     */
    public void save(java.io.OutputStream stream) throws java.io.IOException {
        save(stream, false);
    }

    public void save(java.io.OutputStream stream, boolean closeStream) throws java.io.IOException {
        java.io.DataOutputStream out = null;
        try {
            long start;
            if (android.gesture.GestureStore.PROFILE_LOADING_SAVING) {
                start = android.os.SystemClock.elapsedRealtime();
            }
            final java.util.HashMap<java.lang.String, java.util.ArrayList<android.gesture.Gesture>> maps = mNamedGestures;
            out = new java.io.DataOutputStream(stream instanceof java.io.BufferedOutputStream ? stream : new java.io.BufferedOutputStream(stream, android.gesture.GestureConstants.IO_BUFFER_SIZE));
            // Write version number
            out.writeShort(android.gesture.GestureStore.FILE_FORMAT_VERSION);
            // Write number of entries
            out.writeInt(maps.size());
            for (java.util.Map.Entry<java.lang.String, java.util.ArrayList<android.gesture.Gesture>> entry : maps.entrySet()) {
                final java.lang.String key = entry.getKey();
                final java.util.ArrayList<android.gesture.Gesture> examples = entry.getValue();
                final int count = examples.size();
                // Write entry name
                out.writeUTF(key);
                // Write number of examples for this entry
                out.writeInt(count);
                for (int i = 0; i < count; i++) {
                    examples.get(i).serialize(out);
                }
            }
            out.flush();
            if (android.gesture.GestureStore.PROFILE_LOADING_SAVING) {
                long end = android.os.SystemClock.elapsedRealtime();
                android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, ("Saving gestures library = " + (end - start)) + " ms");
            }
            mChanged = false;
        } finally {
            if (closeStream)
                android.gesture.GestureUtils.closeStream(out);

        }
    }

    /**
     * Load the gesture library
     */
    public void load(java.io.InputStream stream) throws java.io.IOException {
        load(stream, false);
    }

    public void load(java.io.InputStream stream, boolean closeStream) throws java.io.IOException {
        java.io.DataInputStream in = null;
        try {
            in = new java.io.DataInputStream(stream instanceof java.io.BufferedInputStream ? stream : new java.io.BufferedInputStream(stream, android.gesture.GestureConstants.IO_BUFFER_SIZE));
            long start;
            if (android.gesture.GestureStore.PROFILE_LOADING_SAVING) {
                start = android.os.SystemClock.elapsedRealtime();
            }
            // Read file format version number
            final short versionNumber = in.readShort();
            switch (versionNumber) {
                case 1 :
                    readFormatV1(in);
                    break;
            }
            if (android.gesture.GestureStore.PROFILE_LOADING_SAVING) {
                long end = android.os.SystemClock.elapsedRealtime();
                android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, ("Loading gestures library = " + (end - start)) + " ms");
            }
        } finally {
            if (closeStream)
                android.gesture.GestureUtils.closeStream(in);

        }
    }

    private void readFormatV1(java.io.DataInputStream in) throws java.io.IOException {
        final android.gesture.Learner classifier = mClassifier;
        final java.util.HashMap<java.lang.String, java.util.ArrayList<android.gesture.Gesture>> namedGestures = mNamedGestures;
        namedGestures.clear();
        // Number of entries in the library
        final int entriesCount = in.readInt();
        for (int i = 0; i < entriesCount; i++) {
            // Entry name
            final java.lang.String name = in.readUTF();
            // Number of gestures
            final int gestureCount = in.readInt();
            final java.util.ArrayList<android.gesture.Gesture> gestures = new java.util.ArrayList<android.gesture.Gesture>(gestureCount);
            for (int j = 0; j < gestureCount; j++) {
                final android.gesture.Gesture gesture = android.gesture.Gesture.deserialize(in);
                gestures.add(gesture);
                classifier.addInstance(android.gesture.Instance.createInstance(mSequenceType, mOrientationStyle, gesture, name));
            }
            namedGestures.put(name, gestures);
        }
    }

    android.gesture.Learner getLearner() {
        return mClassifier;
    }
}

