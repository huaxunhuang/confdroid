/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.textclassifier;


/**
 * Helper object for setting and getting entity scores for classified text.
 *
 * @unknown 
 */
final class EntityConfidence implements android.os.Parcelable {
    private final android.util.ArrayMap<java.lang.String, java.lang.Float> mEntityConfidence = new android.util.ArrayMap();

    private final java.util.ArrayList<java.lang.String> mSortedEntities = new java.util.ArrayList<>();

    EntityConfidence() {
    }

    EntityConfidence(@android.annotation.NonNull
    android.view.textclassifier.EntityConfidence source) {
        com.android.internal.util.Preconditions.checkNotNull(source);
        mEntityConfidence.putAll(source.mEntityConfidence);
        mSortedEntities.addAll(source.mSortedEntities);
    }

    /**
     * Constructs an EntityConfidence from a map of entity to confidence.
     *
     * Map entries that have 0 confidence are removed, and values greater than 1 are clamped to 1.
     *
     * @param source
     * 		a map from entity to a confidence value in the range 0 (low confidence) to
     * 		1 (high confidence).
     */
    EntityConfidence(@android.annotation.NonNull
    java.util.Map<java.lang.String, java.lang.Float> source) {
        com.android.internal.util.Preconditions.checkNotNull(source);
        // Prune non-existent entities and clamp to 1.
        mEntityConfidence.ensureCapacity(source.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.Float> it : source.entrySet()) {
            if (it.getValue() <= 0)
                continue;

            mEntityConfidence.put(it.getKey(), java.lang.Math.min(1, it.getValue()));
        }
        resetSortedEntitiesFromMap();
    }

    /**
     * Returns an immutable list of entities found in the classified text ordered from
     * high confidence to low confidence.
     */
    @android.annotation.NonNull
    public java.util.List<java.lang.String> getEntities() {
        return java.util.Collections.unmodifiableList(mSortedEntities);
    }

    /**
     * Returns the confidence score for the specified entity. The value ranges from
     * 0 (low confidence) to 1 (high confidence). 0 indicates that the entity was not found for the
     * classified text.
     */
    @android.annotation.FloatRange(from = 0.0, to = 1.0)
    public float getConfidenceScore(java.lang.String entity) {
        if (mEntityConfidence.containsKey(entity)) {
            return mEntityConfidence.get(entity);
        }
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mEntityConfidence.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mEntityConfidence.size());
        for (java.util.Map.Entry<java.lang.String, java.lang.Float> entry : mEntityConfidence.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeFloat(entry.getValue());
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textclassifier.EntityConfidence> CREATOR = new android.os.Parcelable.Creator<android.view.textclassifier.EntityConfidence>() {
        @java.lang.Override
        public android.view.textclassifier.EntityConfidence createFromParcel(android.os.Parcel in) {
            return new android.view.textclassifier.EntityConfidence(in);
        }

        @java.lang.Override
        public android.view.textclassifier.EntityConfidence[] newArray(int size) {
            return new android.view.textclassifier.EntityConfidence[size];
        }
    };

    private EntityConfidence(android.os.Parcel in) {
        final int numEntities = in.readInt();
        mEntityConfidence.ensureCapacity(numEntities);
        for (int i = 0; i < numEntities; ++i) {
            mEntityConfidence.put(in.readString(), in.readFloat());
        }
        resetSortedEntitiesFromMap();
    }

    private void resetSortedEntitiesFromMap() {
        mSortedEntities.clear();
        mSortedEntities.ensureCapacity(mEntityConfidence.size());
        mSortedEntities.addAll(mEntityConfidence.keySet());
        mSortedEntities.sort(( e1, e2) -> {
            float score1 = mEntityConfidence.get(e1);
            float score2 = mEntityConfidence.get(e2);
            return java.lang.Float.compare(score2, score1);
        });
    }
}

