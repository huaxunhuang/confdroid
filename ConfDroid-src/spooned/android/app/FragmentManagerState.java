package android.app;


final class FragmentManagerState implements android.os.Parcelable {
    android.app.FragmentState[] mActive;

    int[] mAdded;

    android.app.BackStackState[] mBackStack;

    public FragmentManagerState() {
    }

    public FragmentManagerState(android.os.Parcel in) {
        mActive = in.createTypedArray(android.app.FragmentState.CREATOR);
        mAdded = in.createIntArray();
        mBackStack = in.createTypedArray(android.app.BackStackState.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeTypedArray(mActive, flags);
        dest.writeIntArray(mAdded);
        dest.writeTypedArray(mBackStack, flags);
    }

    public static final android.os.Parcelable.Creator<android.app.FragmentManagerState> CREATOR = new android.os.Parcelable.Creator<android.app.FragmentManagerState>() {
        public android.app.FragmentManagerState createFromParcel(android.os.Parcel in) {
            return new android.app.FragmentManagerState(in);
        }

        public android.app.FragmentManagerState[] newArray(int size) {
            return new android.app.FragmentManagerState[size];
        }
    };
}

