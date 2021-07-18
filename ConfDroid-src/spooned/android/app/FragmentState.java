/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.app;


final class FragmentState implements android.os.Parcelable {
    final java.lang.String mClassName;

    final int mIndex;

    final boolean mFromLayout;

    final int mFragmentId;

    final int mContainerId;

    final java.lang.String mTag;

    final boolean mRetainInstance;

    final boolean mDetached;

    final android.os.Bundle mArguments;

    final boolean mHidden;

    android.os.Bundle mSavedFragmentState;

    android.app.Fragment mInstance;

    public FragmentState(android.app.Fragment frag) {
        mClassName = frag.getClass().getName();
        mIndex = frag.mIndex;
        mFromLayout = frag.mFromLayout;
        mFragmentId = frag.mFragmentId;
        mContainerId = frag.mContainerId;
        mTag = frag.mTag;
        mRetainInstance = frag.mRetainInstance;
        mDetached = frag.mDetached;
        mArguments = frag.mArguments;
        mHidden = frag.mHidden;
    }

    public FragmentState(android.os.Parcel in) {
        mClassName = in.readString();
        mIndex = in.readInt();
        mFromLayout = in.readInt() != 0;
        mFragmentId = in.readInt();
        mContainerId = in.readInt();
        mTag = in.readString();
        mRetainInstance = in.readInt() != 0;
        mDetached = in.readInt() != 0;
        mArguments = in.readBundle();
        mHidden = in.readInt() != 0;
        mSavedFragmentState = in.readBundle();
    }

    public android.app.Fragment instantiate(android.app.FragmentHostCallback host, android.app.Fragment parent, android.app.FragmentManagerNonConfig childNonConfig) {
        if (mInstance == null) {
            final android.content.Context context = host.getContext();
            if (mArguments != null) {
                mArguments.setClassLoader(context.getClassLoader());
            }
            mInstance = android.app.Fragment.instantiate(context, mClassName, mArguments);
            if (mSavedFragmentState != null) {
                mSavedFragmentState.setClassLoader(context.getClassLoader());
                mInstance.mSavedFragmentState = mSavedFragmentState;
            }
            mInstance.setIndex(mIndex, parent);
            mInstance.mFromLayout = mFromLayout;
            mInstance.mRestored = true;
            mInstance.mFragmentId = mFragmentId;
            mInstance.mContainerId = mContainerId;
            mInstance.mTag = mTag;
            mInstance.mRetainInstance = mRetainInstance;
            mInstance.mDetached = mDetached;
            mInstance.mHidden = mHidden;
            mInstance.mFragmentManager = host.mFragmentManager;
            if (android.app.FragmentManagerImpl.DEBUG)
                android.util.Log.v(android.app.FragmentManagerImpl.TAG, "Instantiated fragment " + mInstance);

        }
        mInstance.mChildNonConfig = childNonConfig;
        return mInstance;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mClassName);
        dest.writeInt(mIndex);
        dest.writeInt(mFromLayout ? 1 : 0);
        dest.writeInt(mFragmentId);
        dest.writeInt(mContainerId);
        dest.writeString(mTag);
        dest.writeInt(mRetainInstance ? 1 : 0);
        dest.writeInt(mDetached ? 1 : 0);
        dest.writeBundle(mArguments);
        dest.writeInt(mHidden ? 1 : 0);
        dest.writeBundle(mSavedFragmentState);
    }

    public static final android.os.Parcelable.Creator<android.app.FragmentState> CREATOR = new android.os.Parcelable.Creator<android.app.FragmentState>() {
        public android.app.FragmentState createFromParcel(android.os.Parcel in) {
            return new android.app.FragmentState(in);
        }

        public android.app.FragmentState[] newArray(int size) {
            return new android.app.FragmentState[size];
        }
    };
}

