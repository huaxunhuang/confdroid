package android.bordeaux.services;


public final class StringString implements android.os.Parcelable {
    public java.lang.String key;

    public java.lang.String value;

    public static final android.os.Parcelable.Creator<android.bordeaux.services.StringString> CREATOR = new android.os.Parcelable.Creator<android.bordeaux.services.StringString>() {
        public android.bordeaux.services.StringString createFromParcel(android.os.Parcel in) {
            return new android.bordeaux.services.StringString(in);
        }

        public android.bordeaux.services.StringString[] newArray(int size) {
            return new android.bordeaux.services.StringString[size];
        }
    };

    public StringString() {
    }

    private StringString(android.os.Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(key);
        out.writeString(value);
    }

    public void readFromParcel(android.os.Parcel in) {
        key = in.readString();
        value = in.readString();
    }
}

