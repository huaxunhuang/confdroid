/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.content;


/**
 * Representation of an owner of {@link UndoOperation} objects in an {@link UndoManager}.
 *
 * @unknown 
 */
public class UndoOwner {
    final java.lang.String mTag;

    final android.content.UndoManager mManager;

    java.lang.Object mData;

    int mOpCount;

    // For saving/restoring state.
    int mStateSeq;

    int mSavedIdx;

    UndoOwner(java.lang.String tag, android.content.UndoManager manager) {
        if (tag == null) {
            throw new java.lang.NullPointerException("tag can't be null");
        }
        if (manager == null) {
            throw new java.lang.NullPointerException("manager can't be null");
        }
        mTag = tag;
        mManager = manager;
    }

    /**
     * Return the unique tag name identifying this owner.  This is the tag
     * supplied to {@link UndoManager#getOwner(String, Object) UndoManager.getOwner}
     * and is immutable.
     */
    public java.lang.String getTag() {
        return mTag;
    }

    /**
     * Return the actual data object of the owner.  This is the data object
     * supplied to {@link UndoManager#getOwner(String, Object) UndoManager.getOwner}.  An
     * owner may have a null data if it was restored from a previously saved state with
     * no getOwner call to associate it with its data.
     */
    public java.lang.Object getData() {
        return mData;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((("UndoOwner:[mTag=" + mTag) + " mManager=") + mManager) + " mData=") + mData) + " mData=") + mData) + " mOpCount=") + mOpCount) + " mStateSeq=") + mStateSeq) + " mSavedIdx=") + mSavedIdx) + "]";
    }
}

