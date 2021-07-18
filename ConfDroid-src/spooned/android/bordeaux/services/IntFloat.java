package android.bordeaux.services;


public final class IntFloat implements android.os.Parcelable {
    public int index;

    public float value;

    public static final android.os.Parcelable.Creator<android.bordeaux.services.IntFloat> CREATOR = new android.os.Parcelable.Creator<android.bordeaux.services.IntFloat>() {
        public android.bordeaux.services.IntFloat createFromParcel(android.os.Parcel in) {
            return new android.bordeaux.services.IntFloat(in);
        }

        public android.bordeaux.services.IntFloat[] newArray(int size) {
            return new android.bordeaux.services.IntFloat[size];
        }
    };

    public IntFloat() {
    }

    private IntFloat(android.os.Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(index);
        out.writeFloat(value);
    }

    public void readFromParcel(android.os.Parcel in) {
        index = in.readInt();
        value = in.readFloat();
    }
}

