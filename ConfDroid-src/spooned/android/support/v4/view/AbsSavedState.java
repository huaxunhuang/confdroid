/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v4.view;


/**
 * A {@link Parcelable} implementation that should be used by inheritance
 * hierarchies to ensure the state of all classes along the chain is saved.
 */
public abstract class AbsSavedState implements android.os.Parcelable {
    public static final android.support.v4.view.AbsSavedState EMPTY_STATE = new android.support.v4.view.AbsSavedState() {};

    private final android.os.Parcelable mSuperState;

    /**
     * Constructor used to make the EMPTY_STATE singleton
     */
    private AbsSavedState() {
        mSuperState = null;
    }

    /**
     * Constructor called by derived classes when creating their SavedState objects
     *
     * @param superState
     * 		The state of the superclass of this view
     */
    protected AbsSavedState(android.os.Parcelable superState) {
        if (superState == null) {
            throw new java.lang.IllegalArgumentException("superState must not be null");
        }
        mSuperState = (superState != android.support.v4.view.AbsSavedState.EMPTY_STATE) ? superState : null;
    }

    /**
     * Constructor used when reading from a parcel. Reads the state of the superclass.
     *
     * @param source
     * 		parcel to read from
     */
    protected AbsSavedState(android.os.Parcel source) {
        this(source, null);
    }

    /**
     * Constructor used when reading from a parcel. Reads the state of the superclass.
     *
     * @param source
     * 		parcel to read from
     * @param loader
     * 		ClassLoader to use for reading
     */
    protected AbsSavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
        android.os.Parcelable superState = source.readParcelable(loader);
        mSuperState = (superState != null) ? superState : android.support.v4.view.AbsSavedState.EMPTY_STATE;
    }

    public final android.os.Parcelable getSuperState() {
        return mSuperState;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(mSuperState, flags);
    }

    public static final android.os.Parcelable.Creator<android.support.v4.view.AbsSavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.v4.view.AbsSavedState>() {
        @java.lang.Override
        public android.support.v4.view.AbsSavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
            android.os.Parcelable superState = in.readParcelable(loader);
            if (superState != null) {
                throw new java.lang.IllegalStateException("superState must be null");
            }
            return android.support.v4.view.AbsSavedState.EMPTY_STATE;
        }

        @java.lang.Override
        public android.support.v4.view.AbsSavedState[] newArray(int size) {
            return new android.support.v4.view.AbsSavedState[size];
        }
    });
}

