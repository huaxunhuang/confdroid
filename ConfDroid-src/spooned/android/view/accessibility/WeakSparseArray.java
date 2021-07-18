/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.accessibility;


final class WeakSparseArray<E> {
    private final java.lang.ref.ReferenceQueue<E> mRefQueue = new java.lang.ref.ReferenceQueue<>();

    private final android.util.SparseArray<android.view.accessibility.WeakSparseArray.WeakReferenceWithId<E>> mSparseArray = new android.util.SparseArray();

    public void append(int key, E value) {
        removeUnreachableValues();
        mSparseArray.append(key, new android.view.accessibility.WeakSparseArray.WeakReferenceWithId(value, mRefQueue, key));
    }

    public void remove(int key) {
        removeUnreachableValues();
        mSparseArray.remove(key);
    }

    public E get(int key) {
        removeUnreachableValues();
        android.view.accessibility.WeakSparseArray.WeakReferenceWithId<E> ref = mSparseArray.get(key);
        return ref != null ? ref.get() : null;
    }

    private void removeUnreachableValues() {
        for (java.lang.ref.Reference ref = mRefQueue.poll(); ref != null; ref = mRefQueue.poll()) {
            mSparseArray.remove(((android.view.accessibility.WeakSparseArray.WeakReferenceWithId) (ref)).mId);
        }
    }

    private static class WeakReferenceWithId<E> extends java.lang.ref.WeakReference<E> {
        final int mId;

        WeakReferenceWithId(E referent, java.lang.ref.ReferenceQueue<? super E> q, int id) {
            super(referent, q);
            mId = id;
        }
    }
}

