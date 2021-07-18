/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.app;


final class BackStackState implements android.os.Parcelable {
    final int[] mOps;

    final int mTransition;

    final int mTransitionStyle;

    final java.lang.String mName;

    final int mIndex;

    final int mBreadCrumbTitleRes;

    final java.lang.CharSequence mBreadCrumbTitleText;

    final int mBreadCrumbShortTitleRes;

    final java.lang.CharSequence mBreadCrumbShortTitleText;

    final java.util.ArrayList<java.lang.String> mSharedElementSourceNames;

    final java.util.ArrayList<java.lang.String> mSharedElementTargetNames;

    public BackStackState(android.support.v4.app.BackStackRecord bse) {
        int numRemoved = 0;
        android.support.v4.app.BackStackRecord.Op op = bse.mHead;
        while (op != null) {
            if (op.removed != null)
                numRemoved += op.removed.size();

            op = op.next;
        } 
        mOps = new int[(bse.mNumOp * 7) + numRemoved];
        if (!bse.mAddToBackStack) {
            throw new java.lang.IllegalStateException("Not on back stack");
        }
        op = bse.mHead;
        int pos = 0;
        while (op != null) {
            mOps[pos++] = op.cmd;
            mOps[pos++] = (op.fragment != null) ? op.fragment.mIndex : -1;
            mOps[pos++] = op.enterAnim;
            mOps[pos++] = op.exitAnim;
            mOps[pos++] = op.popEnterAnim;
            mOps[pos++] = op.popExitAnim;
            if (op.removed != null) {
                final int N = op.removed.size();
                mOps[pos++] = N;
                for (int i = 0; i < N; i++) {
                    mOps[pos++] = op.removed.get(i).mIndex;
                }
            } else {
                mOps[pos++] = 0;
            }
            op = op.next;
        } 
        mTransition = bse.mTransition;
        mTransitionStyle = bse.mTransitionStyle;
        mName = bse.mName;
        mIndex = bse.mIndex;
        mBreadCrumbTitleRes = bse.mBreadCrumbTitleRes;
        mBreadCrumbTitleText = bse.mBreadCrumbTitleText;
        mBreadCrumbShortTitleRes = bse.mBreadCrumbShortTitleRes;
        mBreadCrumbShortTitleText = bse.mBreadCrumbShortTitleText;
        mSharedElementSourceNames = bse.mSharedElementSourceNames;
        mSharedElementTargetNames = bse.mSharedElementTargetNames;
    }

    public BackStackState(android.os.Parcel in) {
        mOps = in.createIntArray();
        mTransition = in.readInt();
        mTransitionStyle = in.readInt();
        mName = in.readString();
        mIndex = in.readInt();
        mBreadCrumbTitleRes = in.readInt();
        mBreadCrumbTitleText = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mBreadCrumbShortTitleRes = in.readInt();
        mBreadCrumbShortTitleText = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mSharedElementSourceNames = in.createStringArrayList();
        mSharedElementTargetNames = in.createStringArrayList();
    }

    public android.support.v4.app.BackStackRecord instantiate(android.support.v4.app.FragmentManagerImpl fm) {
        android.support.v4.app.BackStackRecord bse = new android.support.v4.app.BackStackRecord(fm);
        int pos = 0;
        int num = 0;
        while (pos < mOps.length) {
            android.support.v4.app.BackStackRecord.Op op = new android.support.v4.app.BackStackRecord.Op();
            op.cmd = mOps[pos++];
            if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (((("Instantiate " + bse) + " op #") + num) + " base fragment #") + mOps[pos]);

            int findex = mOps[pos++];
            if (findex >= 0) {
                android.support.v4.app.Fragment f = fm.mActive.get(findex);
                op.fragment = f;
            } else {
                op.fragment = null;
            }
            op.enterAnim = mOps[pos++];
            op.exitAnim = mOps[pos++];
            op.popEnterAnim = mOps[pos++];
            op.popExitAnim = mOps[pos++];
            final int N = mOps[pos++];
            if (N > 0) {
                op.removed = new java.util.ArrayList<android.support.v4.app.Fragment>(N);
                for (int i = 0; i < N; i++) {
                    if (android.support.v4.app.FragmentManagerImpl.DEBUG)
                        android.util.Log.v(android.support.v4.app.FragmentManagerImpl.TAG, (("Instantiate " + bse) + " set remove fragment #") + mOps[pos]);

                    android.support.v4.app.Fragment r = fm.mActive.get(mOps[pos++]);
                    op.removed.add(r);
                }
            }
            bse.mEnterAnim = op.enterAnim;
            bse.mExitAnim = op.exitAnim;
            bse.mPopEnterAnim = op.popEnterAnim;
            bse.mPopExitAnim = op.popExitAnim;
            bse.addOp(op);
            num++;
        } 
        bse.mTransition = mTransition;
        bse.mTransitionStyle = mTransitionStyle;
        bse.mName = mName;
        bse.mIndex = mIndex;
        bse.mAddToBackStack = true;
        bse.mBreadCrumbTitleRes = mBreadCrumbTitleRes;
        bse.mBreadCrumbTitleText = mBreadCrumbTitleText;
        bse.mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
        bse.mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
        bse.mSharedElementSourceNames = mSharedElementSourceNames;
        bse.mSharedElementTargetNames = mSharedElementTargetNames;
        bse.bumpBackStackNesting(1);
        return bse;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeIntArray(mOps);
        dest.writeInt(mTransition);
        dest.writeInt(mTransitionStyle);
        dest.writeString(mName);
        dest.writeInt(mIndex);
        dest.writeInt(mBreadCrumbTitleRes);
        android.text.TextUtils.writeToParcel(mBreadCrumbTitleText, dest, 0);
        dest.writeInt(mBreadCrumbShortTitleRes);
        android.text.TextUtils.writeToParcel(mBreadCrumbShortTitleText, dest, 0);
        dest.writeStringList(mSharedElementSourceNames);
        dest.writeStringList(mSharedElementTargetNames);
    }

    public static final android.os.Parcelable.Creator<android.support.v4.app.BackStackState> CREATOR = new android.os.Parcelable.Creator<android.support.v4.app.BackStackState>() {
        @java.lang.Override
        public android.support.v4.app.BackStackState createFromParcel(android.os.Parcel in) {
            return new android.support.v4.app.BackStackState(in);
        }

        @java.lang.Override
        public android.support.v4.app.BackStackState[] newArray(int size) {
            return new android.support.v4.app.BackStackState[size];
        }
    };
}

