package android.nfc;


/**
 * Class to IPC data to be shared over Android Beam.
 * Allows bundling NdefMessage, Uris and flags in a single
 * IPC call. This is important as we want to reduce the
 * amount of IPC calls at "touch time".
 *
 * @unknown 
 */
public final class BeamShareData implements android.os.Parcelable {
    public final android.nfc.NdefMessage ndefMessage;

    public final android.net.Uri[] uris;

    public final android.os.UserHandle userHandle;

    public final int flags;

    public BeamShareData(android.nfc.NdefMessage msg, android.net.Uri[] uris, android.os.UserHandle userHandle, int flags) {
        this.ndefMessage = msg;
        this.uris = uris;
        this.userHandle = userHandle;
        this.flags = flags;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        int urisLength = (uris != null) ? uris.length : 0;
        dest.writeParcelable(ndefMessage, 0);
        dest.writeInt(urisLength);
        if (urisLength > 0) {
            dest.writeTypedArray(uris, 0);
        }
        dest.writeParcelable(userHandle, 0);
        dest.writeInt(this.flags);
    }

    public static final android.os.Parcelable.Creator<android.nfc.BeamShareData> CREATOR = new android.os.Parcelable.Creator<android.nfc.BeamShareData>() {
        @java.lang.Override
        public android.nfc.BeamShareData createFromParcel(android.os.Parcel source) {
            android.net.Uri[] uris = null;
            android.nfc.NdefMessage msg = source.readParcelable(android.nfc.NdefMessage.class.getClassLoader());
            int numUris = source.readInt();
            if (numUris > 0) {
                uris = new android.net.Uri[numUris];
                source.readTypedArray(uris, android.net.Uri.CREATOR);
            }
            android.os.UserHandle userHandle = source.readParcelable(android.os.UserHandle.class.getClassLoader());
            int flags = source.readInt();
            return new android.nfc.BeamShareData(msg, uris, userHandle, flags);
        }

        @java.lang.Override
        public android.nfc.BeamShareData[] newArray(int size) {
            return new android.nfc.BeamShareData[size];
        }
    };
}

