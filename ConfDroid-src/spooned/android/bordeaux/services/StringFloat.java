package android.bordeaux.services;


public final class StringFloat implements android.os.Parcelable {
    public java.lang.String key;

    public float value;

    public static final android.os.Parcelable.Creator<android.bordeaux.services.StringFloat> CREATOR = new android.os.Parcelable.Creator<android.bordeaux.services.StringFloat>() {
        public android.bordeaux.services.StringFloat createFromParcel(android.os.Parcel in) {
            return new android.bordeaux.services.StringFloat(in);
        }

        public android.bordeaux.services.StringFloat[] newArray(int size) {
            return new android.bordeaux.services.StringFloat[size];
        }
    };

    public StringFloat() {
    }

    public StringFloat(java.lang.String newKey, float newValue) {
        key = newKey;
        value = newValue;
    }

    private StringFloat(android.os.Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(key);
        out.writeFloat(value);
    }

    public void readFromParcel(android.os.Parcel in) {
        key = in.readString();
        value = in.readFloat();
    }
}

