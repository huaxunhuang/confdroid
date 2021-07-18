package android.support.v4.os;


class ParcelableCompatCreatorHoneycombMR2<T> implements android.os.Parcelable.ClassLoaderCreator<T> {
    private final android.support.v4.os.ParcelableCompatCreatorCallbacks<T> mCallbacks;

    public ParcelableCompatCreatorHoneycombMR2(android.support.v4.os.ParcelableCompatCreatorCallbacks<T> callbacks) {
        mCallbacks = callbacks;
    }

    public T createFromParcel(android.os.Parcel in) {
        return mCallbacks.createFromParcel(in, null);
    }

    public T createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
        return mCallbacks.createFromParcel(in, loader);
    }

    public T[] newArray(int size) {
        return mCallbacks.newArray(size);
    }
}

