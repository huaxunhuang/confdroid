package android.support.v4.app;


final class FragmentManagerState implements android.os.Parcelable {
    android.support.v4.app.FragmentState[] mActive;

    int[] mAdded;

    android.support.v4.app.BackStackState[] mBackStack;

    public FragmentManagerState() {
    }

    public FragmentManagerState(android.os.Parcel in) {
        mActive = in.createTypedArray(android.support.v4.app.FragmentState.CREATOR);
        mAdded = in.createIntArray();
        mBackStack = in.createTypedArray(android.support.v4.app.BackStackState.CREATOR);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeTypedArray(mActive, flags);
        dest.writeIntArray(mAdded);
        dest.writeTypedArray(mBackStack, flags);
    }

    public static final android.os.Parcelable.Creator<android.support.v4.app.FragmentManagerState> CREATOR = new android.os.Parcelable.Creator<android.support.v4.app.FragmentManagerState>() {
        @java.lang.Override
        public android.support.v4.app.FragmentManagerState createFromParcel(android.os.Parcel in) {
            return new android.support.v4.app.FragmentManagerState(in);
        }

        @java.lang.Override
        public android.support.v4.app.FragmentManagerState[] newArray(int size) {
            return new android.support.v4.app.FragmentManagerState[size];
        }
    };
}

